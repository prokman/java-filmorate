package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getAllFilms();

    Film getFilmById(Integer id);

    void addFilm(Film film);

}
