package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.mapper.LikeRawMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcOperations jdbc;
    private final LikeRawMapper likeRawMapper;


    @Override
    public List<Integer> getRatedFilmId(Integer limit) {
        String likeQuery = "SELECT film_id FROM film_likes GROUP BY film_id ORDER BY COUNT(USER_ID)  DESC LIMIT ?";
        List<Integer> ratedFilms = jdbc.query(likeQuery, likeRawMapper, limit);
        return ratedFilms;
    }
}
