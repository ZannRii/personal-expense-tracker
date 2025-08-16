package org.example.personalexpensetracker.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class DBConfig {
    private static JdbcTemplate jdbcTemplate;
    static{
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/personal_tracker");
        config.setUsername("root");
        config.setPassword("121050#@");
        HikariDataSource dataSource = new HikariDataSource(config);
        jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("Connected to database");
    }
    public static JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }
}
