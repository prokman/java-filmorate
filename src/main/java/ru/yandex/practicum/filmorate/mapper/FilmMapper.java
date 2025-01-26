package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Component
public class FilmMapper {
    private MpaDbStorage mpaDbStorage;
    private GenreDbStorage genreDbStorage;

    @Autowired
    public FilmMapper(MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.mpaDbStorage=mpaDbStorage;
        this.genreDbStorage=genreDbStorage;
    }

    public Film mapFilmReqToFilm(NewFilmRequest newFilmRequest) {
        Film film = new Film();
        film.setName(newFilmRequest.getName());
        film.setDescription(newFilmRequest.getDescription());
        film.setReleaseDate(newFilmRequest.getReleaseDate());
        film.setDuration(newFilmRequest.getDuration());
        film.setMpa(mpaDbStorage.getMpaById(newFilmRequest.getMpa().getId()).get());
        for (Genre genre: newFilmRequest.getGenres()) {
            genre.setGenreName(genreDbStorage.getGenById(genre.getId()).get().getGenreName());
        }
        film.setGenres(newFilmRequest.getGenres());
        return film;
    }

}
