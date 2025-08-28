package org.example.personalexpensetracker.dao;

import javafx.scene.control.TextField;
import org.example.personalexpensetracker.db.DBConfig;
import org.example.personalexpensetracker.entity.Expense;
import org.example.personalexpensetracker.entity.Income;
import org.example.personalexpensetracker.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseDao {

    private JdbcTemplate jdbcTemplate = DBConfig.getJdbcTemplate();
    public List<Income> getAllStudents(){
        return jdbcTemplate.query("select * from expense", new BeanPropertyRowMapper<>(Income.class));
    }

    public void insert(Expense expense) {
        jdbcTemplate.update("insert into expense(user_id,category_id,payment_method_id,amount,date,create_time) values(?,?,?,?,?,now())",expense.getUser_id(),expense.getCategory_id(),expense.getPayment_method_id(),expense.getAmount(),expense.getDate());
    }

    public double getTotalExpense(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Expense WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }


    public Map<String, Double> getExpenseByCategory(int userId) {
        String sql = "SELECT c.category_name, COALESCE(SUM(e.amount), 0) AS total " +
                "FROM Expense e " +
                "JOIN Category c ON e.category_id = c.category_id " +
                "WHERE e.user_id = ? " +
                "GROUP BY c.category_name";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId);

        Map<String, Double> expenseMap = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String category = (String) row.get("category_name");
            Double total = ((Number) row.get("total")).doubleValue();
            expenseMap.put(category, total);
        }

        return expenseMap;
    }

    public Map<String, Double> getExpenseByCategoryByMonth(int userId, int month) {
        String sql = "SELECT c.category_name, COALESCE(SUM(e.amount), 0) AS total " +
                "FROM Expense e " +
                "JOIN Category c ON e.category_id = c.category_id " +
                "WHERE e.user_id = ? AND MONTH(e.date) = ? " +
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

    public double getTotalExpenseByMonth(int userId, int month) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM expense WHERE user_id=? AND MONTH(date)=?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId, month);
    }
}
