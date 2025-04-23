package com.expensetracker.util;

import java.sql.*;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:expense_tracker.db";
    private static DatabaseUtil instance;
    private Connection connection;

    private DatabaseUtil() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeDatabase() {
        try (Statement statement = connection.createStatement()) {
            // Create categories table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE)"
            );

            // Create transactions table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, " +
                "type TEXT NOT NULL, " +  // 'INCOME' or 'EXPENSE'
                "category_id INTEGER, " +
                "date TEXT NOT NULL, " +
                "description TEXT, " +
                "FOREIGN KEY (category_id) REFERENCES categories(id))"
            );
            
            // Insert default categories if they don't exist
            String[] defaultCategories = {"Food", "Transportation", "Housing", "Utilities", "Entertainment", "Shopping", "Healthcare", "Salary", "Other"};
            
            for (String category : defaultCategories) {
                try {
                    statement.execute("INSERT INTO categories (name) VALUES ('" + category + "')");
                } catch (SQLException e) {
                    // Category already exists, ignore
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
} 