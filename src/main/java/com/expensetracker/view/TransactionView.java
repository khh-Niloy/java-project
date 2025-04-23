package com.expensetracker.view;

import com.expensetracker.controller.ExpenseTrackerController;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public class TransactionView extends BorderPane {
    
    private ExpenseTrackerController controller;
    
    // Input controls
    private TextField amountField;
    private ComboBox<Transaction.Type> typeComboBox;
    private ComboBox<Category> categoryComboBox;
    private DatePicker datePicker;
    private TextField descriptionField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button clearButton;
    
    // Transaction table
    private TableView<Transaction> transactionTable;
    
    // Summary information
    private Text incomeText;
    private Text expenseText;
    private Text balanceText;
    private VBox categoryBox;
    
    // Selected transaction for editing
    private Transaction selectedTransaction;
    
    public TransactionView() {
        controller = new ExpenseTrackerController();
        
        setupInputForm();
        setupTransactionTable();
        setupSummary();
        
        setTop(createHeader());
        setLeft(createInputForm());
        setCenter(createTransactionTable());
        setRight(createSummaryPanel());
        
        setPadding(new Insets(10));
        
        refreshData();
    }
    
    private void setupInputForm() {
        amountField = new TextField();
        amountField.setPromptText("Amount");
        
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(Transaction.Type.values());
        typeComboBox.setPromptText("Type");
        
        categoryComboBox = new ComboBox<>();
        categoryComboBox.setPromptText("Category");
        
        datePicker = new DatePicker(LocalDate.now());
        
        descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        
        addButton = new Button("Add");
        addButton.setOnAction(e -> handleAddTransaction());
        
        updateButton = new Button("Update");
        updateButton.setOnAction(e -> handleUpdateTransaction());
        updateButton.setDisable(true);
        
        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> handleDeleteTransaction());
        deleteButton.setDisable(true);
        
        clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clearForm());
    }
    
    private void setupTransactionTable() {
        transactionTable = new TableView<>();
        
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDate().toString()));
        
        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getType().toString()));
        
        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.format("$%.2f", cellData.getValue().getAmount())));
        
        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            if (transaction.getCategory() != null) {
                return new SimpleStringProperty(transaction.getCategory().getName());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        
        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDescription()));
        
        transactionTable.getColumns().addAll(dateCol, typeCol, amountCol, categoryCol, descriptionCol);
        
        transactionTable.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    selectedTransaction = row.getItem();
                    populateForm(selectedTransaction);
                    updateButton.setDisable(false);
                    deleteButton.setDisable(false);
                }
            });
            return row;
        });
    }
    
    private void setupSummary() {
        incomeText = new Text();
        expenseText = new Text();
        balanceText = new Text();
        categoryBox = new VBox(5);
    }
    
    private VBox createHeader() {
        VBox header = new VBox();
        header.setPadding(new Insets(0, 0, 10, 0));
        
        Text title = new Text("Personal Expense Tracker");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        header.getChildren().add(title);
        return header;
    }
    
    private VBox createInputForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setMinWidth(300);
        
        Text formTitle = new Text("Transaction Details");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 10, 0));
        
        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amountField, 1, 0);
        
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeComboBox, 1, 1);
        
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryComboBox, 1, 2);
        
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);
        
        grid.add(new Label("Description:"), 0, 4);
        grid.add(descriptionField, 1, 4);
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        
        form.getChildren().addAll(formTitle, grid, buttonBox);
        return form;
    }
    
    private VBox createTransactionTable() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        
        Text tableTitle = new Text("Transactions");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        transactionTable.setPrefHeight(400);
        
        container.getChildren().addAll(tableTitle, transactionTable);
        return container;
    }
    
    private VBox createSummaryPanel() {
        VBox summary = new VBox(10);
        summary.setPadding(new Insets(10));
        summary.setMinWidth(200);
        
        Text summaryTitle = new Text("Summary");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox balanceBox = new VBox(5);
        balanceBox.setPadding(new Insets(0, 0, 10, 0));
        
        Text incomeLabel = new Text("Total Income:");
        balanceBox.getChildren().addAll(incomeLabel, incomeText);
        
        Text expenseLabel = new Text("Total Expenses:");
        balanceBox.getChildren().addAll(expenseLabel, expenseText);
        
        Text balanceLabel = new Text("Balance:");
        balanceBox.getChildren().addAll(balanceLabel, balanceText);
        
        Text categoryTitle = new Text("Expenses by Category");
        categoryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        summary.getChildren().addAll(summaryTitle, balanceBox, categoryTitle, categoryBox);
        return summary;
    }
    
    private void refreshData() {
        // Update categories
        categoryComboBox.setItems(controller.getCategories());
        
        // Update transactions table
        transactionTable.setItems(controller.getTransactions());
        
        // Update summary
        updateSummary();
    }
    
    private void updateSummary() {
        double totalIncome = controller.getTotalIncome();
        double totalExpenses = controller.getTotalExpenses();
        double balance = controller.getBalance();
        
        incomeText.setText(String.format("$%.2f", totalIncome));
        incomeText.setFill(Color.GREEN);
        
        expenseText.setText(String.format("$%.2f", totalExpenses));
        expenseText.setFill(Color.RED);
        
        balanceText.setText(String.format("$%.2f", balance));
        balanceText.setFill(balance >= 0 ? Color.GREEN : Color.RED);
        
        // Update category breakdown
        categoryBox.getChildren().clear();
        
        Map<String, Double> expensesByCategory = controller.getExpensesByCategory();
        for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            Text categoryText = new Text(String.format("%s: $%.2f", entry.getKey(), entry.getValue()));
            categoryBox.getChildren().add(categoryText);
        }
    }
    
    private void clearForm() {
        amountField.clear();
        typeComboBox.setValue(null);
        categoryComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        descriptionField.clear();
        
        selectedTransaction = null;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    
    private void populateForm(Transaction transaction) {
        amountField.setText(String.valueOf(transaction.getAmount()));
        typeComboBox.setValue(transaction.getType());
        categoryComboBox.setValue(transaction.getCategory());
        datePicker.setValue(transaction.getDate());
        descriptionField.setText(transaction.getDescription());
    }
    
    private void handleAddTransaction() {
        if (validateForm()) {
            double amount = Double.parseDouble(amountField.getText());
            Transaction.Type type = typeComboBox.getValue();
            Category category = categoryComboBox.getValue();
            LocalDate date = datePicker.getValue();
            String description = descriptionField.getText();
            
            boolean success = controller.addTransaction(amount, type, category, date, description);
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction added successfully");
                clearForm();
                refreshData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add transaction");
            }
        }
    }
    
    private void handleUpdateTransaction() {
        if (selectedTransaction != null && validateForm()) {
            selectedTransaction.setAmount(Double.parseDouble(amountField.getText()));
            selectedTransaction.setType(typeComboBox.getValue());
            selectedTransaction.setCategory(categoryComboBox.getValue());
            selectedTransaction.setDate(datePicker.getValue());
            selectedTransaction.setDescription(descriptionField.getText());
            
            boolean success = controller.updateTransaction(selectedTransaction);
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction updated successfully");
                clearForm();
                refreshData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update transaction");
            }
        }
    }
    
    private void handleDeleteTransaction() {
        if (selectedTransaction != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, 
                                         "Are you sure you want to delete this transaction?", 
                                         ButtonType.YES, ButtonType.NO);
            confirmDialog.setTitle("Confirm Delete");
            Optional<ButtonType> result = confirmDialog.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.YES) {
                boolean success = controller.deleteTransaction(selectedTransaction);
                
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction deleted successfully");
                    clearForm();
                    refreshData();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete transaction");
                }
            }
        }
    }
    
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();
        
        // Validate amount
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                errorMessage.append("Amount must be greater than zero.\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Amount must be a valid number.\n");
        }
        
        // Validate type
        if (typeComboBox.getValue() == null) {
            errorMessage.append("Please select a transaction type.\n");
        }
        
        // Validate date
        if (datePicker.getValue() == null) {
            errorMessage.append("Please select a date.\n");
        }
        
        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", errorMessage.toString());
            return false;
        }
        
        return true;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 