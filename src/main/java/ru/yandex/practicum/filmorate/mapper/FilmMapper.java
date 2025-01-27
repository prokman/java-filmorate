package ru.yandex.practicum.filmorate.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;

import java.util.List;

@Component
public class FilmMapper {
    private MpaDbStorage mpaDbStorage;
    private GenreDbStorage genreDbStorage;

    @Autowired
    public FilmMapper(MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;

    }

    public Film mapFilmReqToFilm(NewFilmRequest newFilmRequest) {
        Film film = new Film();
        film.setName(newFilmRequest.getName());
        film.setDescription(newFilmRequest.getDescription());
        film.setReleaseDate(newFilmRequest.getReleaseDate());
        film.setDuration(newFilmRequest.getDuration());
        Mpa mpa = mpaDbStorage.getMpaById(newFilmRequest.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("мпа с ид " + newFilmRequest.getMpa().getId() + " отсутствует"));
        film.setMpa(mpa);
        if (newFilmRequest.getGenres() != null) {
            //для нового фильма получаем названия жанров согласно имеющимся id жанров,
            //жанры в запросе приходят без названий
            List<Genre> genreList = genreDbStorage.getAllGenres();
            for (Genre genre : newFilmRequest.getGenres()) {
                genre.setName(genreList.get(genre.getId()).getName());
            }
            film.setGenres(newFilmRequest.getGenres());
        }
        return film;
    }

}
