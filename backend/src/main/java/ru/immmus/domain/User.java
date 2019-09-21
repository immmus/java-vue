package ru.immmus.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usr")
@EqualsAndHashCode(of = "id")
// Без конкретного определения toString, он пытается подтянуть все поля
// А именно те которые должны подтягиваться гибернейтом лениво
// И когда сущность попадает в логи, случается бесконечная рекурсия
// и тогда гибернейт палает с LazyInitializationException
@ToString(of = { "id", "name" })
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "English")
    private LocalDateTime lastVisit;

    @JsonView(Views.FullProfile.class)
    @OneToMany(mappedBy = "subscriber", orphanRemoval = true)
    // Подписки текущего пользователя.
    // Поэтому в mappedBy связи мы выступаем как подписчик и пристыковываемся к полю subscriber
    private Set<UserSubscription> subscriptions = new HashSet<>();

    @JsonView(Views.FullProfile.class)
    @OneToMany(mappedBy = "channel", orphanRemoval = true, cascade = CascadeType.ALL)
    // Подписчики текущего пользователя
    // CascadeType.ALL - здесь нужен, чтобы все действия которые происходят с данным пользователем
    // распростронялись дальше на всех его подписчиков, потому что мы модифицируем это поле,
    // а поле подписок нет и поэтому там каскад указывать не нужно
    private Set<UserSubscription> subscribers = new HashSet<>();
}
