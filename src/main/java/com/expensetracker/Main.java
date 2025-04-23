package com.expensetracker;

import com.expensetracker.util.DatabaseUtil;
import com.expensetracker.view.TransactionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        TransactionView root = new TransactionView();
        Scene scene = new Scene(root, 1000, 600);
        
        primaryStage.setTitle("Personal Expense Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop() {
        // Close database connection when application exits
        DatabaseUtil.getInstance().closeConnection();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 