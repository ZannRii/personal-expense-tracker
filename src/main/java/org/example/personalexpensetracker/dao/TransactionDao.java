package org.example.personalexpensetracker.dao;

import org.example.personalexpensetracker.db.DBConfig;
import org.example.personalexpensetracker.entity.Transaction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

public class TransactionDao {

    private JdbcTemplate jdbcTemplate = DBConfig.getJdbcTemplate();

    // Get all transactions
    public List<Transaction> getAllTransactions(int userId) {
        String sql = """
           SELECT i.income_id AS id, 'Income' AS type, i.amount, c.category_name, i.date, i.category_id
        FROM Income i
        LEFT JOIN Category c ON i.category_id = c.category_id
        WHERE i.user_id = ?
        UNION ALL
        SELECT e.expense_id AS id, 'Expense' AS type, e.amount, c.category_name, e.date, e.category_id
        FROM Expense e
        LEFT JOIN Category c ON e.category_id = c.category_id
        WHERE e.user_id = ?
        ORDER BY date DESC
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class),userId,userId);
    }
    // Delete transaction
    public void deleteTransaction(int txnId, String type) {
        String sql;
        if ("Income".equalsIgnoreCase(type)) {
            sql = "DELETE FROM income WHERE income_id = ?";
        } else {
            sql = "DELETE FROM expense WHERE expense_id = ?";
        }
        jdbcTemplate.update(sql, txnId);
    }

    // Update transaction (example: amount)
    public void updateTransaction(Transaction txn) {
        String sql;
        if ("Income".equalsIgnoreCase(txn.getType())) {
            sql = "UPDATE income SET amount = ?, date = ?, category_id = " +
                    "(SELECT category_id FROM Category WHERE category_name = ? AND type = 'Income') " +
                    "WHERE income_id = ?";
        } else {
            sql = "UPDATE expense SET amount = ?, date = ?, category_id = " +
                    "(SELECT category_id FROM Category WHERE category_name = ? AND type = 'Expense') " +
                    "WHERE expense_id = ?";
        }

        jdbcTemplate.update(
                sql,
                txn.getAmount(),
                java.sql.Date.valueOf(txn.getDate().toLocalDate()),
                txn.getCategory_name(),
                txn.getId()
        );
    }


    public double getCurrentBalance(int userId) {
        double income = jdbcTemplate.queryForObject(
                "SELECT IFNULL(SUM(amount),0) FROM income WHERE user_id = ?", Double.class, userId);
        double expense = jdbcTemplate.queryForObject(
                "SELECT IFNULL(SUM(amount),0) FROM expense WHERE user_id = ?", Double.class, userId);
        return income - expense;
    }

}
