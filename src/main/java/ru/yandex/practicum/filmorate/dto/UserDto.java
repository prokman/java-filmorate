package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Integer id;
    private String name;
    private String login;
    private String email;
    private LocalDate birthday;
}
