package org.example.personalexpensetracker.dao;

import org.example.personalexpensetracker.db.DBConfig;
import org.example.personalexpensetracker.entity.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDao {

    private JdbcTemplate jdbcTemplate = DBConfig.getJdbcTemplate();
    public List<Category> getAllCategory(){
        return jdbcTemplate.query("select * from category", new BeanPropertyRowMapper<>(Category.class));
    }

    public Category findByName(String name) {
        String sql = "SELECT * FROM Category WHERE category_name = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Category.class), name);
    }
    public List<String> getCategoryNamesByType(String type) {
        String sql = "SELECT category_name FROM Category WHERE type = ?";
        return jdbcTemplate.queryForList(sql, String.class, type);
    }
}
