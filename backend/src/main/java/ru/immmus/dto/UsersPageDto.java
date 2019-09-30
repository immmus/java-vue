package ru.immmus.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.AdminPanel.class)
public class UsersPageDto {
    private List<User> users;
    private int currentPage;
    private int totalPages;
}
