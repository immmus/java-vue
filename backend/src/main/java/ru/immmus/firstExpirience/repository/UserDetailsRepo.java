package ru.immmus.firstExpirience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.immmus.firstExpirience.domain.User;

@Repository
public interface UserDetailsRepo extends JpaRepository<User, String> {
}
