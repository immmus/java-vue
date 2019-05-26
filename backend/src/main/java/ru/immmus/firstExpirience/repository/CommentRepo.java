package ru.immmus.firstExpirience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.immmus.firstExpirience.domain.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
