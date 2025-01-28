package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public Genre getGenreById(Integer genreId) {
        return genreDbStorage.getGenById(genreId)
                .orElseThrow(() -> new NotFoundException("жанр с ид" + genreId + " не найден"));
    }

    public List<Genre> getAllGenre() {
        return genreDbStorage.getAllGenres();
    }


}
