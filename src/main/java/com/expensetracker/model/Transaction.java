package com.expensetracker.model;

import java.time.LocalDate;

public class Transaction {
    public enum Type {
        INCOME, EXPENSE
    }
    
    private int id;
    private double amount;
    private Type type;
    private Category category;
    private LocalDate date;
    private String description;
    
    public Transaction() {
        this.date = LocalDate.now();
    }
    
    public Transaction(int id, double amount, Type type, Category category, LocalDate date, String description) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.description = description;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("%s: $%.2f - %s (%s)", 
                type, amount, category != null ? category.getName() : "No Category", date);
    }
} 