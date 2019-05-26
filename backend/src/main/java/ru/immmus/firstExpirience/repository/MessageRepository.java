package ru.immmus.firstExpirience.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.immmus.firstExpirience.domain.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-graph
    // Благодаря этой аннотации мы будем подгружать "ленивое(Lazy)" поле comments в жадной манере, при вызове данного метода.
    @EntityGraph(attributePaths = { "comments" })
    List<Message> findAll();
}
