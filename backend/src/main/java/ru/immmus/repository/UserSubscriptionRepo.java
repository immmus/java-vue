package ru.immmus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.immmus.domain.User;
import ru.immmus.domain.UserSubscription;
import ru.immmus.domain.UserSubscriptionId;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepo extends JpaRepository<UserSubscription, UserSubscriptionId> {
    List<UserSubscription> findBySubscriber(User user);

    List<UserSubscription> findByChannel(User channel);

    Optional<UserSubscription> findByChannelAndSubscriber(User channel, User subscriber);
}
