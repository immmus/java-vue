package ru.immmus.firstExpirience.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.immmus.firstExpirience.domain.Comment;
import ru.immmus.firstExpirience.domain.User;
import ru.immmus.firstExpirience.domain.Views;
import ru.immmus.firstExpirience.dto.EventType;
import ru.immmus.firstExpirience.dto.ObjectType;
import ru.immmus.firstExpirience.repository.CommentRepo;
import ru.immmus.firstExpirience.util.WsSender;

import java.util.function.BiConsumer;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final BiConsumer<EventType, Comment> wsSender;

    @Autowired
    public CommentService(CommentRepo commentRepo, WsSender wsSender) {
        this.commentRepo = commentRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }

    public Comment create(Comment comment, User user) {
        Assert.notNull(comment, "Comment can't be null.");

        comment.setAuthor(user);
        Comment createdComment = commentRepo.save(comment);

        wsSender.accept(EventType.CREATE, createdComment);
        return createdComment;
    }
}
