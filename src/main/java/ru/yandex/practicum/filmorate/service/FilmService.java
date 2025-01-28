package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.NewFilmUpdate;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.FilmUpdateMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;
import ru.yandex.practicum.filmorate.repository.LikeDbStorage;
import ru.yandex.practicum.filmorate.repository.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final FilmMapper filmMapper;
    private final FilmUpdateMapper filmUpdateMapper;


    public Film addFilm(NewFilmRequest newFilmRequest) {
        Film film = filmMapper.mapFilmReqToFilm(newFilmRequest);
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm(NewFilmUpdate newfilm) {
        if (newfilm.getId() == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        Film film = filmUpdateMapper.mapFilmUpdateToFilm(newfilm);
        return filmDbStorage.updateFilm(film);
    }

    public List<FilmDto> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public List<FilmDto> getPopularFilms(int limit) {
        List<Integer> ratedFilmId = likeDbStorage.getRatedFilmId(limit);
        return ratedFilmId.stream()
                .map(FilmId -> filmDbStorage.getFilmById(FilmId).get())
                .collect(Collectors.toList());
    }

    public void addLike(Integer filmId, Integer userId) {
        if (filmId == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }

        if (filmDbStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("фильм с ид " + filmId + " отсутствует");
        }

        if (userDbStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }
        filmDbStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (filmId == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }

        if (filmDbStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("фильм с ид " + filmId + " отсутствует");
        }

        if (userDbStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }
        filmDbStorage.removeLike(filmId, userId);
    }

    public FilmDto getFilmById(Integer filmId) {
        return filmDbStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("фильма с ID " + filmId + " не найден"));
    }


}

