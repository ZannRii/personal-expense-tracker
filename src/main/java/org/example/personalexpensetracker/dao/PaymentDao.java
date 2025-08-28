package org.example.personalexpensetracker.dao;

import org.example.personalexpensetracker.db.DBConfig;
import org.example.personalexpensetracker.entity.PaymentMethod;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class PaymentDao {

    private JdbcTemplate jdbcTemplate = DBConfig.getJdbcTemplate();
    public List<PaymentMethod> getAllMethod(){
        return jdbcTemplate.query("select * from PaymentMethod", new BeanPropertyRowMapper<>(PaymentMethod.class));
    }

    public PaymentMethod findByName(String name) {
        String sql = "SELECT * FROM PaymentMethod WHERE method_name = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PaymentMethod.class), name);
    }
}
