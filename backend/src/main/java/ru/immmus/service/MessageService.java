package ru.immmus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.immmus.domain.Message;
import ru.immmus.domain.User;
import ru.immmus.domain.UserSubscription;
import ru.immmus.domain.Views;
import ru.immmus.dto.EventType;
import ru.immmus.dto.MessagePageDto;
import ru.immmus.dto.MetaDto;
import ru.immmus.dto.ObjectType;
import ru.immmus.repository.MessageRepository;
import ru.immmus.repository.UserSubscriptionRepo;
import ru.immmus.util.WsSender;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";
    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final BiConsumer<EventType, Message> wsSender;
    private final UserSubscriptionRepo userSubscriptionRepo;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(
            WsSender wsSender,
            MessageRepository messageRepository,
            UserSubscriptionRepo userSubscriptionRepo
    ) {
        this.messageRepository = messageRepository;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

    public Message create(Message message, User user) throws IOException {
        Assert.notNull(message, "Message can't be null.");
        Assert.notNull(user, "User can't be null.");

        message.setCreationDate(LocalDateTime.now());
        message.setAuthor(user);
        fillMeta(message);

        Message createdMessage = messageRepository.save(message);
        wsSender.accept(EventType.CREATE, createdMessage);

        return createdMessage;
    }

    public MessagePageDto findForUser(Pageable pageable, User user) {
        // Т.к. нашего текущего юзера мы получаем через AuthenticationPrincipal, то получаем его из сессии
        // и там могут быть проблемы с актуальностью данных, поэтому вместо того чтобы получаеть список каналов
        // через - user.getSubscribers(), мы снова перестрахуемся и получим из из базы
        Set<User> channels = userSubscriptionRepo.findBySubscriber(user)
                .stream()
                // Отфильтровываем всех у кого не подтверждена подписка, чтобы не выводить его сообщения
                .filter(UserSubscription::isActive)
                .map(UserSubscription::getChannel)
                .collect(Collectors.toSet());
        // добавляем текущего пользователя, чтобы он свои сообщения тоже видел
        channels.add(user);
        Page<Message> page = messageRepository.findByAuthorIn(channels, pageable);
        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public Message update(
            User user,
            Message messageFromDb,
            Message messageFromUser
    ) throws IOException {
        if (isItMessageThisUser(messageFromDb, user)) {
        /*Данный метод копирует из message(который мы получаем от пользователя по id в виде json)
        в messageFromDb игнорируя id(из message)*/
            fillMeta(messageFromUser);
            messageFromDb.setText(messageFromUser.getText());
            Message updatedMessage = messageRepository.save(messageFromDb);
            wsSender.accept(EventType.UPDATE, updatedMessage);
            return updatedMessage;
        } else {
            return messageFromDb;
        }
    }

    public void delete(User user, Message message) {
        if (isItMessageThisUser(message, user)) {
            messageRepository.delete(message);
            wsSender.accept(EventType.REMOVE, message);
        }
    }

    private boolean isItMessageThisUser(Message message, User user) {
        if (user != null && message != null) {
            return message.getAuthor().getId().equals(user.getId());
        } else {
            return false;
        }
    }

    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);
        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());
            matcher = IMG_REGEX.matcher(url);
            message.setLink(url);
            if (matcher.find()) {
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
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }
}
