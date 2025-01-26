package ru.yandex.practicum.filmorate.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.repository.mapper.FilmGenreRawMapper;
import ru.yandex.practicum.filmorate.repository.mapper.FilmRawMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcOperations jdbc;
    private final FilmGenreRawMapper filmGenreRawMapper;
    private final FilmRawMapper filmRawMapper;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Film addFilm(Film film) {
        String insertFilmDbQuery = "INSERT INTO FILMS(NAME, DESCRIPTIONS, RELEASEDATE, DURATION, MPA_ID) " +
                "VALUES(?, ?, ?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertFilmDbQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            film.setId(id);

        } else {
            throw new RuntimeException("не удалось сохранить данные фильма");
        }

        String insertFilmGenreDbQuery = "INSERT INTO FILM_GENRE (FILM_ID,GENRE_ID) " +
                "VALUES("+film.getId()+", ?)";

        LinkedHashSet<Genre> genres =  film.getGenres();
        jdbc.batchUpdate(insertFilmGenreDbQuery, genres, 50,
                (PreparedStatement ps, Genre genre) -> {
                    ps.setObject(1, genre.getId());
                });
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String updateFilmDbQuery = "UPDATE FILMS SET NAME=?, DESCRIPTIONS=?, RELEASEDATE=?, DURATION=?, MPA_ID=? " +
                " WHERE FILM_ID = ?";
        int rowsUpdated = jdbc.update(updateFilmDbQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (rowsUpdated == 0) {
            throw new RuntimeException("не удалось обновить данные фильма");
        }

        if (film.getGenres()!=null) {
//            ||!film.getGenres().isEmpty()
            String DeleteFilmGenreDbQuery = "DELETE FROM film_genre WHERE film_id = ?";
            rowsUpdated = jdbc.update(DeleteFilmGenreDbQuery, film.getId());
            if (rowsUpdated <= 0) {
                throw new RuntimeException("не удалось удалить данные жанра");
            }

            String insertFilmGenreDbQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) " +
                    "VALUES("+film.getId()+", ?)";

            LinkedHashSet<Genre> genres =  film.getGenres();
            jdbc.batchUpdate(insertFilmGenreDbQuery, genres, 50,
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setObject(1, genre.getId());
                    });
        } else {
            String query = "SELECT Genre_ID FROM FILM_genre WHERE film_id = ?";
            List<Integer> listOfGenre =  jdbc.query(query, filmGenreRawMapper, film.getId());
            LinkedHashSet<Genre> genres= listOfGenre.stream()
                    .map(integer -> {return genreDbStorage.getGenById(integer).get();})
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        }

        return film;
    }

    @Override
    public List<FilmDto> getAllFilms() {
        String getAllFilmsQuery  = "SELECT * FROM films as f " +
                "LEFT OUTER JOIN MPArating as MPA ON f.MPA_ID=MPA.MPA_ID";
        List<FilmDto> filmDtoList =jdbc.query(getAllFilmsQuery,filmRawMapper);

        String query = "SELECT Genre_ID FROM FILM_genre WHERE film_id = ?";
        for (FilmDto film:filmDtoList) {
            List<Integer> listOfGenre =  jdbc.query(query, filmGenreRawMapper, film.getId());
            LinkedHashSet<Genre> genres= listOfGenre.stream()
                    .map(integer -> {return genreDbStorage.getGenById(integer).get();})
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        }
        return filmDtoList;
    }

    @Override
    public Optional<FilmDto> getFilmById(Integer film_id) {
        String findByIdquery  = "SELECT * FROM films as f " +
                "LEFT OUTER JOIN MPArating as MPA ON f.MPA_ID=MPA.MPA_ID WHERE f.film_id=? ";
        FilmDto filmDtoList = new FilmDto();
        try {
            filmDtoList =jdbc.queryForObject(findByIdquery,filmRawMapper, film_id);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }

        String query = "SELECT Genre_ID FROM FILM_genre WHERE film_id = ?";

            List<Integer> listOfGenre =  jdbc.query(query, filmGenreRawMapper, filmDtoList.getId());
            LinkedHashSet<Genre> genres= listOfGenre.stream()
                    .map(integer -> {return genreDbStorage.getGenById(integer).get();})
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        filmDtoList.setGenres(genres);
        return Optional.ofNullable(filmDtoList);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String query = "INSERT INTO film_likes (film_Id, user_Id) VALUES(?, ?)";
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setObject(1, filmId);
            ps.setObject(2, userId);
            return ps;
        });

    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String query = "DELETE FROM film_likes WHERE film_Id = ? AND user_Id = ?";
        int rowsUpdated = jdbc.update(query, filmId, userId);
        if (rowsUpdated <= 0) {
            throw new RuntimeException("не удалось удалить данные");
        }
    }
}
