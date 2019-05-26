package ru.immmus.firstExpirience.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Table
@Entity
@EqualsAndHashCode(of = { "id" })
public class Comment {

    @Id
    @GeneratedValue
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String text;

    @ManyToOne
    @JoinColumn(name = "message_id")
    @JsonView(Views.FullComment.class)
    private Message message;

    @ManyToOne
    // updatable = false - означает, что автора мы устанавливаем один раз и навсегда
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    // автор будет отображаться в тот момент, когда мы запрашиваем комментарии к сообщению
    @JsonView(Views.IdName.class)
    private User author;

}
