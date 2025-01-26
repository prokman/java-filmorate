package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRawMapper;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcOperations jdbc;
    private final GenreRawMapper genreRawMapper;

    @Override
    public Optional<Genre> getGenById(Integer genere_id) {
        String genreQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        try {
            Genre genre = jdbc.queryForObject(genreQuery, genreRawMapper, genere_id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String allGenreQuery = "SELECT * FROM GENRE";
        return jdbc.query(allGenreQuery, genreRawMapper);
    }
}
