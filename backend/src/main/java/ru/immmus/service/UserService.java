package ru.immmus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.immmus.domain.Role;
import ru.immmus.domain.User;
import ru.immmus.dto.UsersPageDto;
import ru.immmus.repository.UserDetailsRepo;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

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

    //TODO сделать так чтобы выбивал юзера из его сессии
    public User banned(User user) {
        Assert.notNull(user, "This user is not found!");
        user.setBanned(!user.isBanned());
        return userDetailsRepo.save(user);
    }

    public User getAuthUser(OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            final String id = oAuth2User.getName();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            User user = userDetailsRepo.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(id);
                newUser.setName((String) attributes.get("name"));
                newUser.setUserPicture((String) attributes.get("picture"));
                newUser.setEmail((String) attributes.get("email"));
                newUser.setLocale((String) attributes.get("locale"));
                newUser.setGender((String) attributes.get("gender"));
                newUser.setRoles(Set.of(Role.USER));
                return newUser;
            });

            user.setLastVisit(LocalDateTime.now());
            return userDetailsRepo.save(user);
        }
        return null;
    }
}