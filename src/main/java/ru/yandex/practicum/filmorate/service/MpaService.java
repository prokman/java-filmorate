package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public Mpa getMpaById(Integer mpaId) {
        return mpaDbStorage.getMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("рейтинг с ид" + mpaId + " не найден"));
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

}
