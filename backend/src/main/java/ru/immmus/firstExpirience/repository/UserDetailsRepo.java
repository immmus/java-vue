package ru.immmus.firstExpirience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.immmus.firstExpirience.domain.User;

public interface UserDetailsRepo extends JpaRepository<User, String> {
}
