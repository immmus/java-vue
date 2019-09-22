package ru.immmus.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.immmus.domain.User;
import ru.immmus.domain.UserSubscription;
import ru.immmus.domain.Views;
import ru.immmus.service.ProfileService;

import java.util.List;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("{id}")
    @JsonView(Views.FullProfile.class)
    public User get(@PathVariable ("id") User user) {
        return user;
    }

    @PostMapping("change-subscription/{channelId}")
    @JsonView(Views.FullProfile.class)
    public User changeSubscription(
            @AuthenticationPrincipal User subscriber,
            @PathVariable("channelId") User channel
    ) {
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
            @AuthenticationPrincipal User channel,
            @PathVariable("subscriberId") User subscriber
    ) {
        return profileService.changeSubscriptionStatus(channel, subscriber);
    }
}
