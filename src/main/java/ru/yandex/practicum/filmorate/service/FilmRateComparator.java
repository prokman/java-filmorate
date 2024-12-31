package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;


public class FilmRateComparator implements Comparator<Film> {

    @Override
    public int compare(Film o1, Film o2) {
        if (o1.getRating() == o2.getRating()) {
            return o2.getId() - o1.getId();
        } else {
            return o2.getRating() - o1.getRating();
        }
    }
}
