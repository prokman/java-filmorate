package ru.yandex.practicum.filmorate.repository;

import java.util.List;


public interface LikeStorage {
    List<Integer> getRatedFilmId(Integer limit);
}
