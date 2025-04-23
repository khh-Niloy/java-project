# Personal Expense Tracker

A simple web application for tracking personal income and expenses.

## Features

- Add, view, update, and delete income/expense transactions
- Categorize transactions
- View transaction history in a table
- See summary information (total income, expenses, balance)
- View expenses breakdown by category

## Requirements

- Java 11 or higher
- Internet connection to download dependencies (first run only)

## How to Run (Simplest Method)

1. Run the `download_embedded_tomcat.bat` script to download required libraries (first time only)
2. Run the `run_embedded.bat` script to compile and start the application
3. Open your browser and navigate to http://localhost:8080/expense-tracker

## Project Structure

- `model`: Contains data model classes and database access objects
- `servlet`: Contains servlet components to handle HTTP requests
- `controller`: Contains business logic to connect models and views
- `util`: Contains utility classes like database connection handler
- `webapp`: Contains JSP pages and web resources

## Database

The application uses SQLite as the database, which will be created automatically in the project directory as `expense_tracker.db` when you first run the application.

## Alternative Installation Methods

### Using Apache Tomcat (Manual installation)

1. Run the `build_and_run.bat` script
2. Follow the instructions to install the WAR file to your Tomcat installation
3. Start Tomcat
4. Access the application at http://localhost:8080/expense-tracker

## Usage

1. **Add Transaction**:

   - Fill in the transaction details (amount, type, category, date, description)
   - Click the "Add Transaction" button

2. **Update Transaction**:

   - Click the "Edit" link next to the transaction
   - Modify the details in the form
   - Click the "Update Transaction" button

3. **Delete Transaction**:

   - Click the "Delete" link next to the transaction
   - Confirm the deletion in the dialog

4. **View Summary**:
   - Total income, expenses, and balance are displayed in the summary panel
   - Expenses by category are shown in the bottom right
