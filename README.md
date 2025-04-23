# Personal Expense Tracker

A simple JavaFX application for tracking personal income and expenses.

## Features

- Add, view, update, and delete income/expense transactions
- Categorize transactions
- View transaction history in a table
- See summary information (total income, expenses, balance)
- View expenses breakdown by category

## Requirements

- Java 11 or higher
- Maven

## How to Run

1. Clone or download this repository
2. Navigate to the project directory
3. Build the project using Maven:
   ```
   mvn clean package
   ```
4. Run the application:
   ```
   mvn javafx:run
   ```

## Database

The application uses SQLite as the database, which will be created automatically in the project directory as `expense_tracker.db` when you first run the application.

## Project Structure

- `model`: Contains data model classes and database access objects
- `view`: Contains JavaFX UI components
- `controller`: Contains business logic to connect models and views
- `util`: Contains utility classes like database connection handler

## Usage

1. **Add Transaction**:

   - Fill in the transaction details (amount, type, category, date, description)
   - Click the "Add" button

2. **Update Transaction**:

   - Select a transaction from the table
   - Modify the details in the form
   - Click the "Update" button

3. **Delete Transaction**:

   - Select a transaction from the table
   - Click the "Delete" button
   - Confirm the deletion in the dialog

4. **View Summary**:
   - Total income, expenses, and balance are displayed in the summary panel
   - Expenses by category are shown in the bottom right
