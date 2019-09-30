package ru.immmus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.immmus.domain.User;
import ru.immmus.dto.UsersPageDto;
import ru.immmus.repository.UserDetailsRepo;

@Service
public class UserService {

    private final UserDetailsRepo userDetailsRepo;

    @Autowired
    public UserService(UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }

    public UsersPageDto users(Pageable pageable) {
        final Page<User> usersPage = userDetailsRepo.findAll(pageable);
        return new UsersPageDto(
                usersPage.getContent(),
                pageable.getPageNumber(),
                usersPage.getTotalPages()
        );
    }

    public User banned(User user) {
        Assert.notNull(user, "This user is not found!");
        user.setBanned(!user.isBanned());
        return userDetailsRepo.save(user);
    }
}