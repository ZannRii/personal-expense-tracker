package org.example.personalexpensetracker.dao;

import javafx.scene.control.TextField;
import org.example.personalexpensetracker.db.DBConfig;
import org.example.personalexpensetracker.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate = DBConfig.getJdbcTemplate();
    public List<User> getAllStudents(){
        return jdbcTemplate.query("select * from user", new BeanPropertyRowMapper<>(User.class));
    }

    public void insert(User user) {
        jdbcTemplate.update("insert into user(username,password,email,create_time) values(?,?,?,now())",user.getName(),user.getPassword(),user.getEmail());
    }

    public User findByEmail(String email) {
        try {
            return (User) jdbcTemplate.queryForObject("select * from user where email = ?", new BeanPropertyRowMapper<>(User.class), email);
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
        }
}
