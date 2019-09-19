package ru.immmus.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
@ToString(of = {"id", "text"})
@EqualsAndHashCode(of = "id")
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class Message {
    public static final int MESSAGE_SEQ = 100000;

    @Id
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1, initialValue = MESSAGE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @JsonView(Views.Id.class) // показываем только id
    private Long id;
    // Аннотация JsonView рпедназначена для того, чтобы пометить интерфейсом поля и методы, которые мы будем
    // показывать(Например в консоли браузера), Views.IdName.class - будет показывать только ID и Name
    @JsonView(Views.IdName.class)
    private String text;

    @ManyToOne
    @JoinColumn(name = "author")
    @JsonView(Views.FullMessage.class)
    private User author;

    // orphanRemoval = true - означает, что при удалении message все зависимые комменты должны удалиться
    @OneToMany(mappedBy = "message", orphanRemoval = true)
    @JsonView(Views.FullMessage.class)
    private List<Comment> comments;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "English")
    // А Views.FullMessage.class - будет показывать все свои поля и поля класса от которых он унаследован
    // При пометке этой аннотацие мы будем получать в консоли браузера Id, name и creationDate
    // с помощью запроса fetch('/message/2').then(response => response.json().then(console.log))
    // Все запросы можно посмотреть в https://gist.github.com/drucoder/a1d8576e1d15be38aae5bac3f914b874
    // Этими аннотациями, мы буде помечать методы в MessageController
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;
    @JsonView(Views.FullMessage.class)
    private String link;

    // Эти три поля будут заполняться только для ссылок ведущих не на ютуб
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;
}
