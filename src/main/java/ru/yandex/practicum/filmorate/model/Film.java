package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;

/**
 * Film.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
}
