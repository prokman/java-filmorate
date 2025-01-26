package ru.yandex.practicum.filmorate.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.NewFilmUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;

@Component
public class FilmUpdateMapper {
    private MpaDbStorage mpaDbStorage;
    private GenreDbStorage genreDbStorage;

    @Autowired
    public FilmUpdateMapper(MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.mpaDbStorage=mpaDbStorage;
        this.genreDbStorage=genreDbStorage;
    }

    public Film mapFilmUpdateToFilm(NewFilmUpdate newFilmUpdate) {
        Film film = new Film();
        film.setId(newFilmUpdate.getId());
        film.setName(newFilmUpdate.getName());
        film.setDescription(newFilmUpdate.getDescription());
        film.setReleaseDate(newFilmUpdate.getReleaseDate());
        film.setDuration(newFilmUpdate.getDuration());
        film.setMpa(mpaDbStorage.getMpaById(newFilmUpdate.getMpa().getId()).get());
        if (newFilmUpdate.getGenres()!=null) {
            //||!newFilmUpdate.getGenres().isEmpty()
            for (Genre genre: newFilmUpdate.getGenres()) {
                genre.setGenreName(genreDbStorage.getGenById(genre.getId()).get().getGenreName());
            }
            film.setGenres(newFilmUpdate.getGenres());
        }
        return film;
    }
}
