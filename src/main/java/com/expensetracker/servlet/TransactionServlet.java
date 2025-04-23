package com.expensetracker.servlet;

import com.expensetracker.controller.ExpenseTrackerController;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        
        try {
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
        } catch (Exception e) {
            getServletContext().log("Error in TransactionServlet: " + e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/static.html");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "add";
        }
        
        try {
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
        } catch (Exception e) {
            getServletContext().log("Error in TransactionServlet: " + e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/static.html");
        }
    }
    
    private void listTransactions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Transaction> transactions = controller.getTransactions();
        List<Category> categories = controller.getCategories();
        
        // Debug log for categories
        System.out.println("TransactionServlet: Loaded " + categories.size() + " categories");
        if (categories.isEmpty()) {
            System.out.println("WARNING: No categories loaded! Manually inserting defaults...");
            controller.addCategory("Food");
            controller.addCategory("Transportation");
            controller.addCategory("Housing");
            controller.addCategory("Utilities");
            controller.addCategory("Entertainment");
            controller.addCategory("Shopping");
            controller.addCategory("Healthcare");
            controller.addCategory("Salary");
            controller.addCategory("Other");
            
            // Reload categories
            categories = controller.getCategories();
            System.out.println("After manual insert: " + categories.size() + " categories");
        } else {
            // Log all categories for debugging
            for (Category cat : categories) {
                System.out.println(" - Category: " + cat.getId() + " - " + cat.getName());
            }
        }
        
        double totalIncome = controller.getTotalIncome();
        double totalExpenses = controller.getTotalExpenses();
        double balance = controller.getBalance();
        Map<String, Double> expensesByCategory = controller.getExpensesByCategory();
        
        // Generate HTML directly
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            // Begin HTML
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Personal Expense Tracker</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("h1 { color: #2c3e50; }");
            out.println(".container { display: flex; gap: 20px; }");
            out.println(".form-container { flex: 1; padding: 15px; background-color: #f5f5f5; border-radius: 5px; }");
            out.println(".data-container { flex: 2; }");
            out.println(".summary-container { flex: 1; padding: 15px; background-color: #f5f5f5; border-radius: 5px; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
            out.println("th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println(".form-group { margin-bottom: 10px; }");
            out.println("label { display: block; margin-bottom: 5px; }");
            out.println("input, select { width: 100%; padding: 8px; box-sizing: border-box; margin-bottom: 10px; }");
            out.println("button { background-color: #3498db; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }");
            out.println("button:hover { background-color: #2980b9; }");
            out.println(".action-links a { margin-right: 10px; text-decoration: none; }");
            out.println(".income { color: green; }");
            out.println(".expense { color: red; }");
            out.println(".summary-item { display: flex; justify-content: space-between; margin-bottom: 10px; padding: 5px; border-bottom: 1px solid #ddd; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            // Header
            out.println("<h1>Personal Expense Tracker</h1>");
            
            // Main container
            out.println("<div class=\"container\">");
            
            // Form
            out.println("<div class=\"form-container\">");
            out.println("<h2>Add New Transaction</h2>");
            out.println("<form action=\"" + request.getContextPath() + "/transactions\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"action\" value=\"add\">");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"amount\">Amount:</label>");
            out.println("<input type=\"number\" id=\"amount\" name=\"amount\" step=\"0.01\" required>");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"type\">Type:</label>");
            out.println("<select id=\"type\" name=\"type\" required>");
            out.println("<option value=\"\">-- Select Type --</option>");
            out.println("<option value=\"INCOME\">Income</option>");
            out.println("<option value=\"EXPENSE\">Expense</option>");
            out.println("</select>");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"category\">Category:</label>");
            out.println("<select id=\"category\" name=\"category\" required>");
            out.println("<option value=\"\">-- Select Category --</option>");
            
            for (Category category : categories) {
                out.println("<option value=\"" + category.getId() + "\">" + category.getName() + "</option>");
            }
            
            out.println("</select>");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"date\">Date:</label>");
            out.println("<input type=\"date\" id=\"date\" name=\"date\" required value=\"" + LocalDate.now() + "\">");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"description\">Description:</label>");
            out.println("<input type=\"text\" id=\"description\" name=\"description\">");
            out.println("</div>");
            
            out.println("<button type=\"submit\">Add Transaction</button>");
            out.println("</form>");
            out.println("</div>");
            
            // Transactions table
            out.println("<div class=\"data-container\">");
            out.println("<h2>Transaction List</h2>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Date</th>");
            out.println("<th>Type</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Category</th>");
            out.println("<th>Description</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            
            if (!transactions.isEmpty()) {
                for (Transaction transaction : transactions) {
                    out.println("<tr>");
                    out.println("<td>" + transaction.getDate() + "</td>");
                    out.println("<td>" + transaction.getType() + "</td>");
                    
                    String cssClass = transaction.getType().toString().equals("INCOME") ? "income" : "expense";
                    out.println("<td class=\"" + cssClass + "\">" + df.format(transaction.getAmount()) + "</td>");
                    
                    String categoryName = transaction.getCategory() != null ? transaction.getCategory().getName() : "N/A";
                    out.println("<td>" + categoryName + "</td>");
                    
                    String description = transaction.getDescription() != null ? transaction.getDescription() : "";
                    out.println("<td>" + description + "</td>");
                    
                    out.println("<td class=\"action-links\">");
                    out.println("<a href=\"" + request.getContextPath() + "/transactions?action=edit&id=" + transaction.getId() + "\">Edit</a>");
                    out.println("<a href=\"" + request.getContextPath() + "/transactions?action=delete&id=" + transaction.getId() + "\" onclick=\"return confirm('Are you sure you want to delete this transaction?')\">Delete</a>");
                    out.println("</td>");
                    
                    out.println("</tr>");
                }
            } else {
                out.println("<tr><td colspan=\"6\" style=\"text-align:center;\">No transactions found.</td></tr>");
            }
            
            out.println("</table>");
            out.println("</div>");
            
            // Summary
            out.println("<div class=\"summary-container\">");
            out.println("<h2>Summary</h2>");
            
            out.println("<div class=\"summary-item\">");
            out.println("<span>Total Income:</span>");
            out.println("<span class=\"income\">" + df.format(totalIncome) + "</span>");
            out.println("</div>");
            
            out.println("<div class=\"summary-item\">");
            out.println("<span>Total Expenses:</span>");
            out.println("<span class=\"expense\">" + df.format(totalExpenses) + "</span>");
            out.println("</div>");
            
            String balanceCssClass = balance >= 0 ? "income" : "expense";
            out.println("<div class=\"summary-item\">");
            out.println("<span>Balance:</span>");
            out.println("<span class=\"" + balanceCssClass + "\">" + df.format(balance) + "</span>");
            out.println("</div>");
            
            out.println("<h3>Expenses by Category</h3>");
            
            if (!expensesByCategory.isEmpty()) {
                for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
                    out.println("<div class=\"summary-item\">");
                    out.println("<span>" + entry.getKey() + ":</span>");
                    out.println("<span class=\"expense\">" + df.format(entry.getValue()) + "</span>");
                    out.println("</div>");
                }
            } else {
                out.println("<div class=\"summary-item\"><span>No expense data available.</span></div>");
            }
            
            out.println("</div>");
            
            // End container
            out.println("</div>");
            
            // Script
            out.println("<script>");
            out.println("document.getElementById('date').valueAsDate = new Date();");
            out.println("</script>");
            
            // End HTML
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Transaction transaction = findTransactionById(id);
        List<Category> categories = controller.getCategories();
        
        // Debug log for categories
        System.out.println("Edit form: Loaded " + categories.size() + " categories");
        if (categories.isEmpty()) {
            System.out.println("WARNING: No categories available for edit form!");
        }
        
        // Generate HTML edit form
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            // HTML header and styles similar to listTransactions but with selected transaction values
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Edit Transaction - Personal Expense Tracker</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("h1 { color: #2c3e50; }");
            out.println(".form-container { max-width: 500px; padding: 15px; background-color: #f5f5f5; border-radius: 5px; margin: 0 auto; }");
            out.println(".form-group { margin-bottom: 10px; }");
            out.println("label { display: block; margin-bottom: 5px; }");
            out.println("input, select { width: 100%; padding: 8px; box-sizing: border-box; margin-bottom: 10px; }");
            out.println("button { background-color: #3498db; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }");
            out.println("button:hover { background-color: #2980b9; }");
            out.println(".button-group { display: flex; gap: 10px; }");
            out.println(".cancel-button { background-color: #95a5a6; }");
            out.println(".cancel-button:hover { background-color: #7f8c8d; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Edit Transaction</h1>");
            
            out.println("<div class=\"form-container\">");
            out.println("<form action=\"" + request.getContextPath() + "/transactions\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"action\" value=\"update\">");
            out.println("<input type=\"hidden\" name=\"id\" value=\"" + transaction.getId() + "\">");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"amount\">Amount:</label>");
            out.println("<input type=\"number\" id=\"amount\" name=\"amount\" step=\"0.01\" required value=\"" + transaction.getAmount() + "\">");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"type\">Type:</label>");
            out.println("<select id=\"type\" name=\"type\" required>");
            out.println("<option value=\"\">-- Select Type --</option>");
            
            boolean isIncomeSelected = transaction.getType() == Transaction.Type.INCOME;
            boolean isExpenseSelected = transaction.getType() == Transaction.Type.EXPENSE;
            
            out.println("<option value=\"INCOME\"" + (isIncomeSelected ? " selected" : "") + ">Income</option>");
            out.println("<option value=\"EXPENSE\"" + (isExpenseSelected ? " selected" : "") + ">Expense</option>");
            
            out.println("</select>");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"category\">Category:</label>");
            out.println("<select id=\"category\" name=\"category\" required>");
            out.println("<option value=\"\">-- Select Category --</option>");
            
            int selectedCategoryId = transaction.getCategory() != null ? transaction.getCategory().getId() : 0;
            
            for (Category category : categories) {
                boolean isSelected = category.getId() == selectedCategoryId;
                out.println("<option value=\"" + category.getId() + "\"" + (isSelected ? " selected" : "") + ">" + category.getName() + "</option>");
            }
            
            out.println("</select>");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"date\">Date:</label>");
            out.println("<input type=\"date\" id=\"date\" name=\"date\" required value=\"" + transaction.getDate() + "\">");
            out.println("</div>");
            
            out.println("<div class=\"form-group\">");
            out.println("<label for=\"description\">Description:</label>");
            String description = transaction.getDescription() != null ? transaction.getDescription() : "";
            out.println("<input type=\"text\" id=\"description\" name=\"description\" value=\"" + description + "\">");
            out.println("</div>");
            
            out.println("<div class=\"button-group\">");
            out.println("<button type=\"submit\">Update Transaction</button>");
            out.println("<a href=\"" + request.getContextPath() + "/transactions\" class=\"button cancel-button\" style=\"text-decoration: none; display: inline-block;\">Cancel</a>");
            out.println("</div>");
            
            out.println("</form>");
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
        }
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
        List<Category> allCategories = controller.getCategories();
        System.out.println("Finding category by ID " + id + " from " + allCategories.size() + " categories");
        
        Category found = allCategories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
                
        if (found == null) {
            System.out.println("WARNING: Could not find category with ID " + id);
        } else {
            System.out.println("Found category: " + found.getId() + " - " + found.getName());
        }
        
        return found;
    }
} 