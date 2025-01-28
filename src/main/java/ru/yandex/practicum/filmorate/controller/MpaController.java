package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;
    private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MpaController.class);


    @GetMapping("/{mpaId}")
    public Mpa getMpaNameById(@PathVariable Integer mpaId) {
        Mpa mpa = mpaService.getMpaById(mpaId);
        log.info("mpa " + mpaId + " получен");
        return mpa;
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        List<Mpa> mpaList = mpaService.getAllMpa();
        log.info("mpa list получен");
        return mpaList;
    }


}
