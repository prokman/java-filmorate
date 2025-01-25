package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class UserRawMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("USER_ID"));
        user.setName(rs.getString("NAME"));
        user.setLogin(rs.getString("LOGIN"));
        user.setEmail(rs.getString("EMAIL"));
        //Timestamp Bdate = rs.getTimestamp("BIRTHDAY");
        //Bdate.toLocalDateTime().toLocalDate()
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());

        return user;
    }
}
