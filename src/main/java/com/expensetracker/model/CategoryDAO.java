package com.expensetracker.model;

import com.expensetracker.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM categories ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("Fetching categories from database...");
            int count = 0;
            
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
                count++;
                System.out.println("Found category: " + category.getId() + " - " + category.getName());
            }
            
            System.out.println("Total categories found: " + count);
            
            if (categories.isEmpty()) {
                System.out.println("No categories found in the database! Checking if table exists...");
                try (Statement checkStmt = conn.createStatement();
                     ResultSet tableCheck = checkStmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='categories'")) {
                    if (tableCheck.next()) {
                        System.out.println("Categories table exists. Trying to insert default categories.");
                        insertDefaultCategories(conn);
                        // Try to fetch again after inserting defaults
                        try (Statement retryStmt = conn.createStatement();
                             ResultSet retryRs = retryStmt.executeQuery(sql)) {
                            while (retryRs.next()) {
                                Category category = new Category();
                                category.setId(retryRs.getInt("id"));
                                category.setName(retryRs.getString("name"));
                                categories.add(category);
                                System.out.println("Added category after retry: " + category.getName());
                            }
                        }
                    } else {
                        System.out.println("Categories table does not exist!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return categories;
    }
    
    private void insertDefaultCategories(Connection conn) throws SQLException {
        String[] defaultCategories = {"Food", "Transportation", "Housing", "Utilities", "Entertainment", "Shopping", "Healthcare", "Salary", "Other"};
        
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO categories (name) VALUES (?)")) {
            for (String categoryName : defaultCategories) {
                try {
                    pstmt.setString(1, categoryName);
                    pstmt.executeUpdate();
                    System.out.println("Inserted default category: " + categoryName);
                } catch (SQLException e) {
                    System.out.println("Could not insert category '" + categoryName + "': " + e.getMessage());
                }
            }
        }
    }
    
    public Category getCategoryById(int id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                return category;
            } else {
                System.out.println("No category found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean insertCategory(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, category.getName());
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getId());
            int affected = pstmt.executeUpdate();
            
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
} 