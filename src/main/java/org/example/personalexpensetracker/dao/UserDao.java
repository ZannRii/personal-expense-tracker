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
        jdbcTemplate.update("insert into user(username,password,email,phone,address,create_time) values(?,?,?,?,?,now())",user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getAddress());
    }

    public void update(User user) {
        String sql = "UPDATE user SET email = ?, phone = ?, address = ?, update_time = now() WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getUser_id()
        );
    }

    public User getById(int userId) {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), userId);
    }

    public User findByEmail(String email) {
        try {
            return (User) jdbcTemplate.queryForObject("select * from user where email = ?", new BeanPropertyRowMapper<>(User.class), email);
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
        }
}
