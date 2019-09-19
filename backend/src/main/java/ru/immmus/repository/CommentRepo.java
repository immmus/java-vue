package ru.immmus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.immmus.domain.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
}
