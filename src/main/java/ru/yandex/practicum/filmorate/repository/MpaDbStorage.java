package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRawMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcOperations jdbc;
    private final MpaRawMapper mpaRawMapper;

    @Override
    public Optional<Mpa> getMpaById(Integer mpaId) {
        String mpaQuery = "SELECT * FROM MPARATING WHERE MPA_ID = ?";
        try {
            Mpa mpa = jdbc.queryForObject(mpaQuery, mpaRawMapper, mpaId);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        final String Find_ALL_QUERY = "SELECT * FROM mparating";
        return jdbc.query(Find_ALL_QUERY, mpaRawMapper);
    }


}
