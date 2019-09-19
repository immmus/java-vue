package ru.immmus.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usr")
@EqualsAndHashCode(of = "id")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    /*@GeneratedValue не указываем, потому что id будут приходить от google в формате String*/
    @JsonView(Views.IdName.class)
    private String id;
    /*Далее указываем поля, которые будут прилетать от гугла*/
    @JsonView(Views.IdName.class)
    private String name;
    @JsonView(Views.IdName.class)
    private String userPicture;
    private String email;
    @JsonView(Views.FullProfile.class)
    private String gender;
    @JsonView(Views.FullProfile.class)
    private String locale;
    @JsonView(Views.FullProfile.class)
    private LocalDateTime lastVisit;

    @ManyToMany
    @JsonView(Views.FullProfile.class)
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    /* Если в потоке сериализуемый класс встречается более 2х раз, то все последующие обьекты заменяются
    *  и передаются в виде назначеного свойства(property - в данном случае это поле "id" от этого обьекта) */
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private Set<User> subscriptions = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    @JsonView(Views.FullProfile.class)
    /* Если в потоке сериализуемый класс встречается более 2х раз, то все последующие обьекты заменяются
     *  и передаются в виде назначеного свойства(property - в данном случае это поле "id" от этого обьекта) */
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private Set<User> subscribers = new HashSet<>();
}
