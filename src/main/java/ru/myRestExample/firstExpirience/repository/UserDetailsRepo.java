package ru.myRestExample.firstExpirience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.myRestExample.firstExpirience.domain.User;

public interface UserDetailsRepo extends JpaRepository<User, String> {
}
