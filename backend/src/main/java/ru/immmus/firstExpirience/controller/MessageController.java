package ru.immmus.firstExpirience.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.immmus.firstExpirience.domain.Message;
import ru.immmus.firstExpirience.dto.EventType;
import ru.immmus.firstExpirience.dto.MetaDto;
import ru.immmus.firstExpirience.dto.ObjectType;
import ru.immmus.firstExpirience.repository.MessageRepository;
import ru.immmus.firstExpirience.domain.User;
import ru.immmus.firstExpirience.domain.Views;
import ru.immmus.firstExpirience.util.WsSender;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "message")
public class MessageController {
    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";
    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);
    private final MessageRepository messageRepository;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepository messageRepository, WsSender wsSender) {
        this.messageRepository = messageRepository;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @GetMapping
    // для того, чтобы не выводить дату создания сообщения пометим аннотацией JsonView
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    /*Добавляем к PathVariable - "id", это показывает спрингу, что по пришедшему id надо найти сущность message*/
    public Message getOneMessage(@PathVariable("id") Message message){
        return message;
    }

    @PostMapping
    public Message create (@RequestBody Message message, @AuthenticationPrincipal User user) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        message.setAuthor(user.getName());
        fillMeta(message);
        Message createdMessage = messageRepository.save(message);
        wsSender.accept(EventType.CREATE, createdMessage);
        return createdMessage;
    }

    @PutMapping("{id}")
    public Message update(
            //Так мы получаем сообщение по id из базы данных
            @PathVariable("id") Message messageFromDb,
            //Так получаем сообщение от пользователя
            @RequestBody Message message
    ) throws IOException {
        /*Данный метод копирует из message(который мы получаем от пользователя по id в виде json)
        в messageFromDb игнорируя id(из message)*/
        fillMeta(message);
        BeanUtils.copyProperties(message, messageFromDb, "id", "author", "user_id");
        Message updatedMessage = messageRepository.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message){
        messageRepository.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }
    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);
        if (matcher.find()){
            String url = text.substring(matcher.start(), matcher.end());
            matcher = IMG_REGEX.matcher(url);
            message.setLink(url);
            if (matcher.find()){
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {
                MetaDto meta = getMeta(url);
                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }
        }
    }
    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        // Используя css селекторы ищем теги meta в html странице по url и в них атрибуты
        Elements title = doc.select("meta[name$=title], meta[property$=title]");
        Elements description = doc.select("meta[name$=description], meta[property$=description]");
        Elements cover = doc.select("meta[name$=image], meta[property$=image]");

        return new MetaDto(
                getConten(title.first()),
                getConten(description.first()),
                getConten(cover.first())
        );
    }

    private String getConten(Element element) {
        return element == null ? "" : element.attr("content");
    }
}
