package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRawMapper;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage{
    private final JdbcOperations jdbc;
    private final MpaRawMapper mpaRawMapper;

    @Override
    public Optional<Mpa> getMpaById(Integer Mpa_id) {
        String MpaQuery = "SELECT * FROM MPARATING WHERE MPA_ID = ?";
        try {
            Mpa mpa = jdbc.queryForObject(MpaQuery, mpaRawMapper, Mpa_id);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
}
