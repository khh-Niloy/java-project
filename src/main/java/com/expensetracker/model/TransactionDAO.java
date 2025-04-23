package com.expensetracker.model;

import com.expensetracker.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDAO {
    
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.id, t.amount, t.type, t.category_id, t.date, t.description, c.name as category_name " +
                     "FROM transactions t LEFT JOIN categories c ON t.category_id = c.id " +
                     "ORDER BY t.date DESC";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    public Transaction getTransactionById(int id) {
        String sql = "SELECT t.id, t.amount, t.type, t.category_id, t.date, t.description, c.name as category_name " +
                     "FROM transactions t LEFT JOIN categories c ON t.category_id = c.id " +
                     "WHERE t.id = ?";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTransactionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean insertTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (amount, type, category_id, date, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setDouble(1, transaction.getAmount());
            pstmt.setString(2, transaction.getType().toString());
            
            if (transaction.getCategory() != null) {
                pstmt.setInt(3, transaction.getCategory().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.setString(4, transaction.getDate().toString());
            pstmt.setString(5, transaction.getDescription());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        transaction.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting transaction: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean updateTransaction(Transaction transaction) {
        String sql = "UPDATE transactions SET amount = ?, type = ?, category_id = ?, date = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, transaction.getAmount());
            pstmt.setString(2, transaction.getType().toString());
            
            if (transaction.getCategory() != null) {
                pstmt.setInt(3, transaction.getCategory().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.setString(4, transaction.getDate().toString());
            pstmt.setString(5, transaction.getDescription());
            pstmt.setInt(6, transaction.getId());
            
            int affected = pstmt.executeUpdate();
            
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
        
        return false;
    }
    
    public double getTotalIncome() {
        return getTotal(Transaction.Type.INCOME);
    }
    
    public double getTotalExpenses() {
        return getTotal(Transaction.Type.EXPENSE);
    }
    
    private double getTotal(Transaction.Type type) {
        String sql = "SELECT SUM(amount) FROM transactions WHERE type = ?";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, type.toString());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public Map<String, Double> getExpensesByCategory() {
        Map<String, Double> expensesByCategory = new HashMap<>();
        String sql = "SELECT c.name, SUM(t.amount) as total " +
                     "FROM transactions t JOIN categories c ON t.category_id = c.id " +
                     "WHERE t.type = 'EXPENSE' " +
                     "GROUP BY c.name " +
                     "ORDER BY total DESC";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                expensesByCategory.put(rs.getString("name"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving expenses by category: " + e.getMessage());
        }
        
        return expensesByCategory;
    }
    
    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setType(Transaction.Type.valueOf(rs.getString("type")));
        
        int categoryId = rs.getInt("category_id");
        if (!rs.wasNull()) {
            Category category = new Category();
            category.setId(categoryId);
            category.setName(rs.getString("category_name"));
            transaction.setCategory(category);
        }
        
        transaction.setDate(LocalDate.parse(rs.getString("date")));
        transaction.setDescription(rs.getString("description"));
        
        return transaction;
    }
} 