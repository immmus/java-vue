package ru.immmus.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/** Сущность для создания составного ключа*/
@Data
// Данная аннотоция означет, что этот класс используется
// как встраиваемая сущность для других сушностей,
// для составных ключей в ДБ
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionId implements Serializable {
    // Так как данная сущность у нас предназначена
    //  для использования в качестве индификатора
    // делаем @JsonView на уровне ID
    @JsonView(Views.Id.class)
    private String channelId;
    @JsonView(Views.Id.class)
    private String subscriberId;
}
