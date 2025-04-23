package com.expensetracker.controller;

import com.expensetracker.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExpenseTrackerController {
    
    private TransactionDAO transactionDAO;
    private CategoryDAO categoryDAO;
    private ObservableList<Transaction> transactions;
    private ObservableList<Category> categories;
    
    public ExpenseTrackerController() {
        transactionDAO = new TransactionDAO();
        categoryDAO = new CategoryDAO();
        loadData();
    }
    
    public void loadData() {
        loadTransactions();
        loadCategories();
    }
    
    public void loadTransactions() {
        List<Transaction> transactionList = transactionDAO.getAllTransactions();
        transactions = FXCollections.observableArrayList(transactionList);
    }
    
    public void loadCategories() {
        List<Category> categoryList = categoryDAO.getAllCategories();
        categories = FXCollections.observableArrayList(categoryList);
    }
    
    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }
    
    public ObservableList<Category> getCategories() {
        return categories;
    }
    
    public boolean addTransaction(Transaction transaction) {
        boolean success = transactionDAO.insertTransaction(transaction);
        if (success) {
            loadTransactions();
        }
        return success;
    }
    
    public boolean addTransaction(double amount, Transaction.Type type, Category category, 
                                LocalDate date, String description) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setDate(date);
        transaction.setDescription(description);
        
        return addTransaction(transaction);
    }
    
    public boolean updateTransaction(Transaction transaction) {
        boolean success = transactionDAO.updateTransaction(transaction);
        if (success) {
            loadTransactions();
        }
        return success;
    }
    
    public boolean deleteTransaction(Transaction transaction) {
        boolean success = transactionDAO.deleteTransaction(transaction.getId());
        if (success) {
            loadTransactions();
        }
        return success;
    }
    
    public double getTotalIncome() {
        return transactionDAO.getTotalIncome();
    }
    
    public double getTotalExpenses() {
        return transactionDAO.getTotalExpenses();
    }
    
    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }
    
    public Map<String, Double> getExpensesByCategory() {
        return transactionDAO.getExpensesByCategory();
    }
    
    public boolean addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        
        boolean success = categoryDAO.insertCategory(category);
        if (success) {
            loadCategories();
        }
        return success;
    }
    
    public boolean updateCategory(Category category) {
        boolean success = categoryDAO.updateCategory(category);
        if (success) {
            loadCategories();
        }
        return success;
    }
    
    public boolean deleteCategory(Category category) {
        boolean success = categoryDAO.deleteCategory(category.getId());
        if (success) {
            loadCategories();
        }
        return success;
    }
} 