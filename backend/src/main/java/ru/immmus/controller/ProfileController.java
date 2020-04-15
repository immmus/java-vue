package ru.immmus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import ru.immmus.domain.User;
import ru.immmus.domain.UserSubscription;
import ru.immmus.domain.Views;
import ru.immmus.service.ProfileService;
import ru.immmus.service.UserService;

import java.util.List;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("{id}")
    @JsonView(Views.FullProfile.class)
    public User get(@PathVariable ("id") User user) {
        return user;
    }

    @PostMapping("change-subscription/{channelId}")
    @JsonView(Views.FullProfile.class)
    public User changeSubscription(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @PathVariable("channelId") User channel
    ) {
        User subscriber = userService.getAuthUser(oAuth2User);
        if (subscriber.equals(channel)) {
            return channel;
        } else {
            return profileService.changeSubscription(channel, subscriber);
        }
    }

    @GetMapping({ "get-subscribers/{channelId}" })
    // сделал @JsonView(Views.IdName.class) т.к. все данные о пользователе нам не нужны
    // в данном случае достаточно id и name. тоже самое относится к методу changeSubscriptionStatus.
    @JsonView(Views.IdName.class)
    //метод возвращает все подписки текущего пользователя
    public List<UserSubscription> subscribers(@PathVariable("channelId") User channel) {
        return profileService.getSubscribers(channel);
    }

    @PostMapping({ "change-status-subscriber/{subscriberId}" })
    @JsonView(Views.IdName.class)
    //метод возвращает все подписки текущего пользователя
    public UserSubscription changeSubscriptionStatus(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @PathVariable("subscriberId") User subscriber
    ) {
        User channel = userService.getAuthUser(oAuth2User);
        return profileService.changeSubscriptionStatus(channel, subscriber);
    }
}
