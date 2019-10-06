package ru.immmus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.immmus.domain.Message;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;
import ru.immmus.dto.MessagePageDto;
import ru.immmus.service.MessageService;

import java.io.IOException;

@RestController
@RequestMapping(value = "message")
public class MessageController {

    public static final int MESSAGES_PER_PAGE = 3;
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    // для того, чтобы не выводить дату создания сообщения пометим аннотацией JsonView
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @AuthenticationPrincipal User user,
            @PageableDefault(
                    size = MESSAGES_PER_PAGE,
                    sort = {"id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return messageService.findAllVisibleMessagesForUser(pageable, user);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    /*Добавляем к PathVariable - "id", это показывает спрингу, что по пришедшему id надо найти сущность message*/
    public Message getOneMessage(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Message create(@RequestBody Message message, @AuthenticationPrincipal User user) throws IOException {
        return messageService.create(message, user);
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message update(
            @AuthenticationPrincipal User user,
            //Так мы получаем сообщение по id из базы данных
            @PathVariable("id") Message messageFromDb,
            //Так получаем сообщение от пользователя
            @RequestBody Message message
    ) throws IOException {
        return messageService.update(user, messageFromDb, message);
    }

    @DeleteMapping("{id}")
    public void delete(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Message message
    ) {
        messageService.delete(user, message);
    }
}
