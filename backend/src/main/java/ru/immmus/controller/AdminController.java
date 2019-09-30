package ru.immmus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;
import ru.immmus.dto.UsersPageDto;
import ru.immmus.service.UserService;

@RestController
@RequestMapping("admin_panel")
public class AdminController {
    private final UserService userService;
    private final static int USERS_PER_PAGE = 20;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("change-isBan")
    @JsonView(Views.AdminPanel.class)
    public User banned(@RequestBody User user) {
        return userService.banned(user);
    }
}
