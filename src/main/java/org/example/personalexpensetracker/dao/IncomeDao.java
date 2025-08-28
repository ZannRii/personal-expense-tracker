package org.example.personalexpensetracker.dao;

import javafx.scene.control.TextField;
import org.example.personalexpensetracker.db.DBConfig;
import org.example.personalexpensetracker.entity.Income;
import org.example.personalexpensetracker.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeDao {

    private JdbcTemplate jdbcTemplate = DBConfig.getJdbcTemplate();
    public List<Income> getAllStudents(){
        return jdbcTemplate.query("select * from income", new BeanPropertyRowMapper<>(Income.class));
    }

    public void insert(Income income) {
        jdbcTemplate.update("insert into income(user_id,category_id,payment_method_id,amount,date,create_time) values(?,?,?,?,?,now())",income.getUser_id(),income.getCategory_id(),income.getPayment_method_id(),income.getAmount(),income.getDate());
    }

    public double getTotalIncome(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Income WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    public Map<String, Double> getIncomeByCategory(int userId) {
        String sql = "SELECT c.category_name, COALESCE(SUM(i.amount), 0) AS total " +
                "FROM Income i " +
                "JOIN Category c ON i.category_id = c.category_id " +
                "WHERE i.user_id = ? " +
                "GROUP BY c.category_name";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId);

        Map<String, Double> incomeMap = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String category = (String) row.get("category_name");
            Double total = ((Number) row.get("total")).doubleValue();
            incomeMap.put(category, total);
        }

        return incomeMap;
    }

    public Map<String, Double> getIncomeByCategoryByMonth(int userId, int month) {
        String sql = "SELECT c.category_name, COALESCE(SUM(i.amount), 0) AS total " +
                "FROM Income i " +
                "JOIN Category c ON i.category_id = c.category_id " +
                "WHERE i.user_id = ? AND MONTH(i.date) = ? " +
                "GROUP BY c.category_name";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId, month);

        Map<String, Double> expenseMap = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String category = (String) row.get("category_name");
            Double total = ((Number) row.get("total")).doubleValue();
            expenseMap.put(category, total);
        }

        return expenseMap;
    }

    public double getTotalIncomeByMonth(int userId, int month) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM income WHERE user_id=? AND MONTH(date)=?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId, month);
    }
}
