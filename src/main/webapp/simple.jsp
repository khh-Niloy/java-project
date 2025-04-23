<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.expensetracker.model.Transaction" %>
<%@ page import="com.expensetracker.model.Category" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.DecimalFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Personal Expense Tracker (Simple Version)</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #2c3e50;
        }
        .container {
            display: flex;
            gap: 20px;
        }
        .form-container {
            flex: 1;
            padding: 15px;
            background-color: #f5f5f5;
            border-radius: 5px;
        }
        .data-container {
            flex: 2;
        }
        .summary-container {
            flex: 1;
            padding: 15px;
            background-color: #f5f5f5;
            border-radius: 5px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .form-group {
            margin-bottom: 10px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input, select {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            margin-bottom: 10px;
        }
        button {
            background-color: #3498db;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #2980b9;
        }
        .action-links a {
            margin-right: 10px;
            text-decoration: none;
        }
        .income {
            color: green;
        }
        .expense {
            color: red;
        }
        .summary-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 5px;
            border-bottom: 1px solid #ddd;
        }
        .note {
            padding: 10px;
            background-color: #d4edda;
            color: #155724;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <h1>Personal Expense Tracker (Simple Version)</h1>
    
    <div class="note">
        <p>This is a simplified version of the expense tracker without using JSTL tags.</p>
        <p>For the full version, please visit <a href="index.jsp">the main page</a>.</p>
    </div>
    
    <div class="container">
        <!-- Form Container -->
        <div class="form-container">
            <h2>Add New Transaction</h2>
            <form action="<%= request.getContextPath() %>/transactions" method="post">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label for="amount">Amount:</label>
                    <input type="number" id="amount" name="amount" step="0.01" required>
                </div>
                
                <div class="form-group">
                    <label for="type">Type:</label>
                    <select id="type" name="type" required>
                        <option value="">-- Select Type --</option>
                        <option value="INCOME">Income</option>
                        <option value="EXPENSE">Expense</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="category">Category:</label>
                    <select id="category" name="category" required>
                        <option value="">-- Select Category --</option>
                        <% 
                        List<Category> categories = (List<Category>)request.getAttribute("categories");
                        if (categories != null) {
                            for (Category category : categories) {
                        %>
                        <option value="<%= category.getId() %>"><%= category.getName() %></option>
                        <% 
                            }
                        }
                        %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="date">Date:</label>
                    <input type="date" id="date" name="date" required value="<%= LocalDate.now() %>">
                </div>
                
                <div class="form-group">
                    <label for="description">Description:</label>
                    <input type="text" id="description" name="description">
                </div>
                
                <button type="submit">Add Transaction</button>
            </form>
        </div>
        
        <!-- Data Container -->
        <div class="data-container">
            <h2>Transaction List</h2>
            <table>
                <tr>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Category</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
                <% 
                List<Transaction> transactions = (List<Transaction>)request.getAttribute("transactions");
                if (transactions != null && !transactions.isEmpty()) {
                    DecimalFormat df = new DecimalFormat("$#,##0.00");
                    for (Transaction transaction : transactions) {
                %>
                <tr>
                    <td><%= transaction.getDate() %></td>
                    <td><%= transaction.getType() %></td>
                    <td class="<%= transaction.getType().toString().equals("INCOME") ? "income" : "expense" %>">
                        <%= df.format(transaction.getAmount()) %>
                    </td>
                    <td><%= transaction.getCategory() != null ? transaction.getCategory().getName() : "N/A" %></td>
                    <td><%= transaction.getDescription() != null ? transaction.getDescription() : "" %></td>
                    <td class="action-links">
                        <a href="<%= request.getContextPath() %>/transactions?action=edit&id=<%= transaction.getId() %>">Edit</a>
                        <a href="<%= request.getContextPath() %>/transactions?action=delete&id=<%= transaction.getId() %>"
                           onclick="return confirm('Are you sure you want to delete this transaction?')">Delete</a>
                    </td>
                </tr>
                <% 
                    }
                } else {
                %>
                <tr>
                    <td colspan="6" style="text-align:center;">No transactions found.</td>
                </tr>
                <% } %>
            </table>
        </div>
        
        <!-- Summary Container -->
        <div class="summary-container">
            <h2>Summary</h2>
            <% 
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            Double totalIncome = (Double)request.getAttribute("totalIncome");
            Double totalExpenses = (Double)request.getAttribute("totalExpenses");
            Double balance = (Double)request.getAttribute("balance");
            
            if (totalIncome == null) totalIncome = 0.0;
            if (totalExpenses == null) totalExpenses = 0.0;
            if (balance == null) balance = 0.0;
            %>
            <div class="summary-item">
                <span>Total Income:</span>
                <span class="income"><%= df.format(totalIncome) %></span>
            </div>
            <div class="summary-item">
                <span>Total Expenses:</span>
                <span class="expense"><%= df.format(totalExpenses) %></span>
            </div>
            <div class="summary-item">
                <span>Balance:</span>
                <span class="<%= balance >= 0 ? "income" : "expense" %>">
                    <%= df.format(balance) %>
                </span>
            </div>
            
            <h3>Expenses by Category</h3>
            <% 
            Map<String, Double> expensesByCategory = (Map<String, Double>)request.getAttribute("expensesByCategory");
            if (expensesByCategory != null && !expensesByCategory.isEmpty()) {
                for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            %>
            <div class="summary-item">
                <span><%= entry.getKey() %>:</span>
                <span class="expense"><%= df.format(entry.getValue()) %></span>
            </div>
            <% 
                }
            } else {
            %>
            <div class="summary-item">
                <span>No expense data available.</span>
            </div>
            <% } %>
        </div>
    </div>
    
    <script>
        // Set current date as default
        document.getElementById('date').valueAsDate = new Date();
    </script>
</body>
</html> 