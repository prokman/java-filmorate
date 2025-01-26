package ru.yandex.practicum.filmorate.repository;

import java.util.Optional;

public class LikeDbStorage implements LikeStorage {
    @Override
    public Optional<Integer> getLikebyFilmId(Integer film_id) {
        String LikeQuery = "SELECT film_id FROM film_likes GROUP BY film_id ORDER BY COUNT(USER_ID) DESC";
        return Optional.empty();
    }
}
