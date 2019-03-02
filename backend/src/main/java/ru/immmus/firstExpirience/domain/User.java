package ru.immmus.firstExpirience.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "usr")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    /*@GeneratedValue не указываем, потому что id будут приходить от google в формате String*/
    private String id;
    /*Далее указываем поля, которые будут прилетать от гугла*/
    private String name;
    private String userPicture;
    private String email;
    private String gender;
    private String locale;
    private LocalDateTime lastVisit;
}
