package ru.immmus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.immmus.domain.Comment;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;
import ru.immmus.service.CommentService;
import ru.immmus.service.UserService;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    @JsonView(Views.FullComment.class)
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) {
        User authUser = userService.getAuthUser(oAuth2User);
        return commentService.create(comment, authUser);
    }
}
