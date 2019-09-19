package ru.immmus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.immmus.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-graph
    // Благодаря этой аннотации мы будем подгружать "ленивое(Lazy)" поле comments в жадной манере, при вызове данного метода.
    @EntityGraph(attributePaths = { "comments" })
    Page<Message> findAll(Pageable pageable);
}
