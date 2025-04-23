<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Personal Expense Tracker</title>
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
    </style>
</head>
<body>
    <h1>Personal Expense Tracker</h1>
    
    <div class="container">
        <!-- Form Container -->
        <div class="form-container">
            <h2>${transaction != null ? 'Edit Transaction' : 'Add New Transaction'}</h2>
            <form action="${pageContext.request.contextPath}/transactions" method="post">
                <input type="hidden" name="action" value="${transaction != null ? 'update' : 'add'}">
                <c:if test="${transaction != null}">
                    <input type="hidden" name="id" value="${transaction.id}">
                </c:if>
                
                <div class="form-group">
                    <label for="amount">Amount:</label>
                    <input type="number" id="amount" name="amount" step="0.01" required 
                           value="${transaction != null ? transaction.amount : ''}">
                </div>
                
                <div class="form-group">
                    <label for="type">Type:</label>
                    <select id="type" name="type" required>
                        <option value="">-- Select Type --</option>
                        <option value="INCOME" ${transaction != null && transaction.type == 'INCOME' ? 'selected' : ''}>Income</option>
                        <option value="EXPENSE" ${transaction != null && transaction.type == 'EXPENSE' ? 'selected' : ''}>Expense</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="category">Category:</label>
                    <select id="category" name="category" required>
                        <option value="">-- Select Category --</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}" ${transaction != null && transaction.category.id == category.id ? 'selected' : ''}>
                                ${category.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="date">Date:</label>
                    <input type="date" id="date" name="date" required 
                           value="${transaction != null ? transaction.date : ''}">
                </div>
                
                <div class="form-group">
                    <label for="description">Description:</label>
                    <input type="text" id="description" name="description" 
                           value="${transaction != null ? transaction.description : ''}">
                </div>
                
                <button type="submit">${transaction != null ? 'Update' : 'Add'} Transaction</button>
                <c:if test="${transaction != null}">
                    <a href="${pageContext.request.contextPath}/transactions">Cancel</a>
                </c:if>
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
                <c:forEach var="transaction" items="${transactions}">
                    <tr>
                        <td>${transaction.date}</td>
                        <td>${transaction.type}</td>
                        <td class="${transaction.type == 'INCOME' ? 'income' : 'expense'}">
                            <fmt:formatNumber value="${transaction.amount}" type="currency"/>
                        </td>
                        <td>${transaction.category.name}</td>
                        <td>${transaction.description}</td>
                        <td class="action-links">
                            <a href="${pageContext.request.contextPath}/transactions?action=edit&id=${transaction.id}">Edit</a>
                            <a href="${pageContext.request.contextPath}/transactions?action=delete&id=${transaction.id}" 
                               onclick="return confirm('Are you sure you want to delete this transaction?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <!-- Summary Container -->
        <div class="summary-container">
            <h2>Summary</h2>
            <div class="summary-item">
                <span>Total Income:</span>
                <span class="income"><fmt:formatNumber value="${totalIncome}" type="currency"/></span>
            </div>
            <div class="summary-item">
                <span>Total Expenses:</span>
                <span class="expense"><fmt:formatNumber value="${totalExpenses}" type="currency"/></span>
            </div>
            <div class="summary-item">
                <span>Balance:</span>
                <span class="${balance >= 0 ? 'income' : 'expense'}">
                    <fmt:formatNumber value="${balance}" type="currency"/>
                </span>
            </div>
            
            <h3>Expenses by Category</h3>
            <c:forEach var="entry" items="${expensesByCategory}">
                <div class="summary-item">
                    <span>${entry.key}:</span>
                    <span class="expense"><fmt:formatNumber value="${entry.value}" type="currency"/></span>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html> 