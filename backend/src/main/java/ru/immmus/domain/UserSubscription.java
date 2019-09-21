package ru.immmus.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@Data
@ToString(of = "id")
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class UserSubscription {
    // Так мы помечаем нащ составной ключ
    @EmbeddedId
    // В json нам это поле не нужно т.к. мы хотим, чтобы там сразу были поля
    // channel и subscriber. Поэтому это поле мы игнорируем, а тем полям проставляем нужным нам JsonView
    @JsonIgnore
    private UserSubscriptionId id;

    @MapsId("channelId")
    @ManyToOne
    @JsonView(Views.IdName.class)
    /* Если в потоке сериализуемый класс встречается более 2х раз, то все последующие обьекты заменяются
     *  и передаются в виде назначеного свойства(property - в данном случае это поле "id" от этого обьекта)
     * Это делается, чтобы не получать бесконечную рекурсию и не тянуть все поля у пользлователей, с их подписками,
     * а у их подписок их подписки и т.д.  */
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private User channel;

    @MapsId("subscriberId")
    @ManyToOne
    @JsonView(Views.IdName.class)
    /* Если в потоке сериализуемый класс встречается более 2х раз, то все последующие обьекты заменяются
     *  и передаются в виде назначеного свойства(property - в данном случае это поле "id" от этого обьекта)
     * Это делается, чтобы не получать бесконечную рекурсию и не тянуть все поля у пользлователей, с их подписками,
     * а у их подписок их подписки и т.д.  */
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private User subscriber;

    @JsonView(Views.IdName.class)
    private boolean active;

    /* Поля channel и subscriber в базе не хранятся и используются лишь, как мэпинги для
    отображения более удобном виде. И по этому UserSubscriptionId при создании нужно
    заполнять самим в конструкторе */
    public UserSubscription(User channel, User subscriber) {
        this.channel = channel;
        this.subscriber = subscriber;
        this.id =  new UserSubscriptionId(channel.getId(), subscriber.getId());
    }
}
