package ru.immmus.firstExpirience.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.immmus.firstExpirience.domain.Comment;
import ru.immmus.firstExpirience.domain.User;
import ru.immmus.firstExpirience.repository.CommentRepo;

@Service
public class CommentService {

    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Comment create(Comment comment, User user) {
        Assert.notNull(comment, "Comment can't be null.");

        comment.setAuthor(user);
        commentRepo.save(comment);

        return comment;
    }
}
