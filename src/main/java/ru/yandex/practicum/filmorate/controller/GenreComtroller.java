package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreComtroller {
    private final GenreService genreService;
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(GenreComtroller.class);

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable Integer genreId) {
        Genre genre = genreService.getGenreById(genreId);
        log.info("genre " + genreId + " получен");
        return genre;
    }

    @GetMapping
    public List<Genre> getAllMpa() {
        List<Genre> genreList = genreService.getAllGenre();
        log.info("mpa list получен");
        return genreList;
    }


}
