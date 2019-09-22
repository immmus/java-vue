package ru.immmus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.immmus.domain.User;
import ru.immmus.domain.UserSubscription;
import ru.immmus.repository.UserDetailsRepo;
import ru.immmus.repository.UserSubscriptionRepo;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;
    private final UserSubscriptionRepo userSubscriptionRepo;

    @Autowired
    public ProfileService(UserDetailsRepo userDetailsRepo, UserSubscriptionRepo userSubscriptionRepo) {
        this.userDetailsRepo = userDetailsRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

    public User changeSubscription(User channel, User subscriber) {
        Optional<UserSubscription>  subscriptions = channel.getSubscribers()
                .stream()
                .filter(subscription -> subscription.getSubscriber().equals(subscriber))
                .findAny();

        if (subscriptions.isEmpty()) {
            UserSubscription subscription = new UserSubscription(channel, subscriber);
            channel.getSubscribers().add(subscription);
        } else {
            channel.getSubscribers().remove(subscriptions.get());
        }
        return userDetailsRepo.save(channel);
    }

    public List<UserSubscription> getSubscribers(User channel) {
        return userSubscriptionRepo.findByChannel(channel);
    }

    public UserSubscription changeSubscriptionStatus(User channel, User subscriber) {
        Optional<UserSubscription> userSubscription = userSubscriptionRepo.findByChannelAndSubscriber(channel, subscriber);
        userSubscription.ifPresent(it -> it.setActive(!it.isActive()));
        return userSubscriptionRepo.save(userSubscription.get());
    }
}
