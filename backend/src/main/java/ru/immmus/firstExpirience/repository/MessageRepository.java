package ru.immmus.firstExpirience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.immmus.firstExpirience.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
