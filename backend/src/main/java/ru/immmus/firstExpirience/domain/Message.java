package ru.immmus.firstExpirience.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@ToString(of = {"id", "text"})
@EqualsAndHashCode(of = "id")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class) // показываем только id
    private Long id;
    // Аннотация JsonView рпедназначена для того, чтобы пометить интерфейсом поля и методы, которые мы будем
    // показывать(Например в консоли браузера), Views.IdName.class - будет показывать только ID и Name
    @JsonView(Views.IdName.class)
    private String text;
    @JsonView(Views.IdName.class)
    private String author;
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "English")
    // А Views.FullMessage.class - будет показывать все свои поля и поля класса от которых он унаследован
    // В данном случае это Id. Так как FullMessage унаследован только от Id при пометке этой аннотацие мы будем получать
    // в консоли браузера Id и creationDate с помощью запроса fetch('/message/2').then(response => response.json().then(console.log))
    //Все запросы можно посмотреть в https://gist.github.com/drucoder/a1d8576e1d15be38aae5bac3f914b874
    // Этими аннотациями, мы буде помечать методы в MessageController
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;
    @JsonView(Views.FullMessage.class)
    private String link;

    //Эти три поля будут заполняться только для ссылок ведущих не на ютуб
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;
}
