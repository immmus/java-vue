package ru.immmus.firstExpirience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.immmus.firstExpirience.domain.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
}
