package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;
import ru.yandex.practicum.filmorate.repository.UserDbStorage;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    FilmMapper filmMapper;
    FilmUpdateMapper filmUpdateMapper;


    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, FilmMapper filmMapper,
                       FilmUpdateMapper filmUpdateMapper, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.filmMapper = filmMapper;
        this.filmUpdateMapper=filmUpdateMapper;
        this.userDbStorage = userDbStorage;
    }

    public Film addFilm(NewFilmRequest newFilmRequest) {
        Film film = filmMapper.mapFilmReqToFilm(newFilmRequest);
        return filmDbStorage.addFilm(film);
        //return film;
    }

    public Film updateFilm(NewFilmUpdate newfilm) {
        if (newfilm.getId() == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        Film film = filmUpdateMapper.mapFilmUpdateToFilm(newfilm);
        return filmDbStorage.updateFilm(film);
//        Film existingFilm = filmStorage.getFilmById(newfilm.getId());
//        if (existingFilm != null) {
//            existingFilm.setName(newfilm.getName());
//            existingFilm.setDescription(newfilm.getDescription());
//            existingFilm.setDuration(newfilm.getDuration());
//            existingFilm.setReleaseDate(newfilm.getReleaseDate());
//            filmStorage.addFilm(existingFilm);
//            return existingFilm;
//        }
//        throw new NotFoundException("фильм с id = " + newfilm.getId() + " не найден");
    }

    public List<FilmDto> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

//    public Collection<Film> getPopularFilms(int count) {
//        return filmStorage.getAllFilms().values().stream()
//                .sorted(new FilmRateComparator())
//                .limit(count)
//                .collect(Collectors.toList());
//    }

    public void addLike(Integer filmId, Integer userId) {
        if (filmId == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }

        if (filmDbStorage.getFilmById(filmId).isEmpty() ) {
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

        if (filmDbStorage.getFilmById(filmId).isEmpty() ) {
            throw new NotFoundException("фильм с ид " + filmId + " отсутствует");
        }

        if (userDbStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }
        filmDbStorage.removeLike(filmId, userId);
    }
}

