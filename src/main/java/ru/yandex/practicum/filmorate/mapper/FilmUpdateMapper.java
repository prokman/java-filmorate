package ru.yandex.practicum.filmorate.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.NewFilmUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRawMapper;

import java.util.LinkedHashSet;

@Component
public class FilmUpdateMapper {
    private MpaDbStorage mpaDbStorage;
    private GenreDbStorage genreDbStorage;
    private final JdbcOperations jdbc;
    private final GenreRawMapper genreRawMapper;

    @Autowired
    public FilmUpdateMapper(MpaDbStorage mpaDbStorage, GenreRawMapper genreRawMapper, JdbcOperations jdbc) {
        this.mpaDbStorage = mpaDbStorage;
        this.genreRawMapper = genreRawMapper;
        this.jdbc = jdbc;
    }

    public Film mapFilmUpdateToFilm(NewFilmUpdate newFilmUpdate) {
        Film film = new Film();
        film.setId(newFilmUpdate.getId());
        film.setName(newFilmUpdate.getName());
        film.setDescription(newFilmUpdate.getDescription());
        film.setReleaseDate(newFilmUpdate.getReleaseDate());
        film.setDuration(newFilmUpdate.getDuration());
        film.setMpa(mpaDbStorage.getMpaById(newFilmUpdate.getMpa().getId()).get());
        if (newFilmUpdate.getGenres() != null) {
            String query = "SELECT f.film_id, g.genre_id, g.genre_name " +
                    "FROM FILM_GENRE as f JOIN GENRE as g " +
                    "ON F.GENRE_ID = G.GENRE_ID " +
                    "WHERE F.FILM_ID = ?";
            LinkedHashSet<Genre> genres =
                    new LinkedHashSet<>(jdbc.query(query, genreRawMapper, newFilmUpdate.getId()));
            film.setGenres(genres);
        }
        return film;
    }
}
