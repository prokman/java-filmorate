package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRawMapper implements RowMapper<FilmDto> {
    @Override
    public FilmDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(rs.getInt("film_id"));
        filmDto.setName(rs.getString("NAME"));
        filmDto.setDescription(rs.getString("DESCRIPTIONS"));
        filmDto.setReleaseDate(rs.getDate("RELEASEDATE").toLocalDate());
        filmDto.setDuration(rs.getInt("DURATION"));
        filmDto.setMpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_name")));
        return filmDto;
    }
}
