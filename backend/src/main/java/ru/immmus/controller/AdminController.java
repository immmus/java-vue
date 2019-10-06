package ru.immmus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;
import ru.immmus.dto.MessagePageDto;
import ru.immmus.dto.UsersPageDto;
import ru.immmus.service.MessageService;
import ru.immmus.service.UserService;

import static ru.immmus.controller.MessageController.MESSAGES_PER_PAGE;

@RestController
@RequestMapping("admin-panel")
public class AdminController {
    private final UserService userService;
    private final MessageService messageService;
    private final static int USERS_PER_PAGE = 20;

    @Autowired
    public AdminController(
            UserService userService,
            MessageService messageService
    ) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("users")
    @JsonView(Views.AdminPanel.class)
    public UsersPageDto users(
            @PageableDefault(
                    size = USERS_PER_PAGE,
                    sort = {"id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return userService.users(pageable);
    }

    @PostMapping("change-isBan/{userId}")
    @JsonView(Views.AdminPanel.class)
    public User banned(@PathVariable("userId") User user) {
        return userService.banned(user);
    }

    @GetMapping("user-messages/{userId}")
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @PathVariable("userId") User user,
            @PageableDefault(
                    size = MESSAGES_PER_PAGE,
                    sort = {"id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return messageService.getMessagesCurrentUser(pageable, user);
    }
}
