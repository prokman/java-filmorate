package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Genre {
    private Integer id;
    private String name;
}
