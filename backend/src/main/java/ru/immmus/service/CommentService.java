package ru.immmus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.immmus.domain.Comment;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;
import ru.immmus.dto.EventType;
import ru.immmus.dto.ObjectType;
import ru.immmus.repository.CommentRepo;
import ru.immmus.util.WsSender;

import java.time.LocalDateTime;
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

        comment.setCreationDate(LocalDateTime.now());
        comment.setAuthor(user);
        Comment createdComment = commentRepo.save(comment);

        wsSender.accept(EventType.CREATE, createdComment);
        return createdComment;
    }
}
