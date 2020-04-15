package ru.immmus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import ru.immmus.domain.Message;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;
import ru.immmus.dto.MessagePageDto;
import ru.immmus.service.MessageService;
import ru.immmus.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping(value = "message")
public class MessageController {

    public static final int MESSAGES_PER_PAGE = 3;
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping
    // для того, чтобы не выводить дату создания сообщения пометим аннотацией JsonView
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @PageableDefault(
                    size = MESSAGES_PER_PAGE,
                    sort = {"id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        User authUser = userService.getAuthUser(oAuth2User);
        return messageService.findAllVisibleMessagesForUser(pageable, authUser);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    /*Добавляем к PathVariable - "id", это показывает спрингу, что по пришедшему id надо найти сущность message*/
    public Message getOneMessage(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) throws IOException {
        User authUser = userService.getAuthUser(oAuth2User);
        return messageService.create(message, authUser);
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message update(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            //Так мы получаем сообщение по id из базы данных
            @PathVariable("id") Message messageFromDb,
            //Так получаем сообщение от пользователя
            @RequestBody Message message
    ) throws IOException {
        User authUser = userService.getAuthUser(oAuth2User);
        return messageService.update(authUser, messageFromDb, message);
    }

    @DeleteMapping("{id}")
    public void delete(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @PathVariable("id") Message message
    ) {
        User authUser = userService.getAuthUser(oAuth2User);
        messageService.delete(authUser, message);
    }
}
