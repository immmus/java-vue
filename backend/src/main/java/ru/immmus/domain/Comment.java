package ru.immmus.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Table
@Entity
@ToString(of = { "id", "text" })
@EqualsAndHashCode(of = { "id" })
public class Comment {
    public static final int COMMENT_SEQ = 100000;

    @Id
    @SequenceGenerator(name = "comment_seq", sequenceName = "comment_seq", allocationSize = 1, initialValue = COMMENT_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
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
