package ru.immmus.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.immmus.domain.User;

import java.util.Optional;

@Repository
public interface UserDetailsRepo extends JpaRepository<User, String> {

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-graph
    // Благодаря этой аннотации мы будем подгружать "ленивое(Lazy)" поле comments в жадной манере, при вызове данного метода.
    @EntityGraph(attributePaths = { "subscriptions", "subscribers" })
    Optional<User> findById(String s);
}
