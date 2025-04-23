package com.expensetracker.servlet;

import com.expensetracker.controller.ExpenseTrackerController;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
    
    private ExpenseTrackerController controller;
    
    @Override
    public void init() throws ServletException {
        controller = new ExpenseTrackerController();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "delete":
                deleteTransaction(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            default:
                listTransactions(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "add";
        }
        
        switch (action) {
            case "add":
                addTransaction(request, response);
                break;
            case "update":
                updateTransaction(request, response);
                break;
            default:
                listTransactions(request, response);
                break;
        }
    }
    
    private void listTransactions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setAttribute("transactions", controller.getTransactions());
        request.setAttribute("categories", controller.getCategories());
        request.setAttribute("totalIncome", controller.getTotalIncome());
        request.setAttribute("totalExpenses", controller.getTotalExpenses());
        request.setAttribute("balance", controller.getBalance());
        request.setAttribute("expensesByCategory", controller.getExpensesByCategory());
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Transaction transaction = findTransactionById(id);
        
        request.setAttribute("transaction", transaction);
        request.setAttribute("categories", controller.getCategories());
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    private void addTransaction(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        double amount = Double.parseDouble(request.getParameter("amount"));
        Transaction.Type type = Transaction.Type.valueOf(request.getParameter("type"));
        
        int categoryId = Integer.parseInt(request.getParameter("category"));
        Category category = findCategoryById(categoryId);
        
        LocalDate date = LocalDate.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        
        controller.addTransaction(amount, type, category, date, description);
        
        response.sendRedirect(request.getContextPath() + "/transactions");
    }
    
    private void updateTransaction(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Transaction transaction = findTransactionById(id);
        
        double amount = Double.parseDouble(request.getParameter("amount"));
        Transaction.Type type = Transaction.Type.valueOf(request.getParameter("type"));
        
        int categoryId = Integer.parseInt(request.getParameter("category"));
        Category category = findCategoryById(categoryId);
        
        LocalDate date = LocalDate.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setDate(date);
        transaction.setDescription(description);
        
        controller.updateTransaction(transaction);
        
        response.sendRedirect(request.getContextPath() + "/transactions");
    }
    
    private void deleteTransaction(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Transaction transaction = findTransactionById(id);
        
        controller.deleteTransaction(transaction);
        
        response.sendRedirect(request.getContextPath() + "/transactions");
    }
    
    private Transaction findTransactionById(int id) {
        return controller.getTransactions().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    private Category findCategoryById(int id) {
        return controller.getCategories().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
} 