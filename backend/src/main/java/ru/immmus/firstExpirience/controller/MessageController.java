package ru.immmus.firstExpirience.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.immmus.firstExpirience.domain.Message;
import ru.immmus.firstExpirience.dto.EventType;
import ru.immmus.firstExpirience.dto.ObjectType;
import ru.immmus.firstExpirience.repository.MessageRepository;
import ru.immmus.firstExpirience.domain.User;
import ru.immmus.firstExpirience.domain.Views;
import ru.immmus.firstExpirience.util.WsSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping(value = "message")
public class MessageController {
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
    public Message create (@RequestBody Message message, @AuthenticationPrincipal User user){
        message.setCreationDate(LocalDateTime.now());
        message.setAuthor(user.getName());
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
    ){
        /*Данный метод копирует из message(который мы получаем от пользователя по id в виде json)
        в messageFromDb игнорируя id(из message)*/
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
}
