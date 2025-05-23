# Personal Expense Tracker

A web application for tracking personal income and expenses, designed to help you manage your finances effectively.

## Problem Statement

Managing personal finances effectively is a common challenge faced by individuals in their daily lives.
Many people struggle to track their expenses, identify spending patterns, and make informed decisions
about budgeting and saving. Without a proper system, financial oversight often leads to overspending,
lack of savings, and difficulty in understanding where the money is going.

The Expense Tracker application is designed to solve this problem by providing a simple yet powerful
tool to:

- Record daily income and expenses
- Categorize transactions for better organization
- View summarized financial data and breakdowns by category or transaction type
- Edit or delete transactions when necessary to maintain accurate records

By offering these features in an intuitive interface, this app empowers users to take control of their
financial activities, ultimately helping them build better money management skills and achieve financial
goals more efficiently.

## Features

- Add, view, update, and delete income/expense transactions
- Categorize transactions for better financial organization
- View complete transaction history in a sortable table
- See summary information (total income, expenses, balance)
- View expenses breakdown by category with visual representation
- Simple and intuitive user interface

## Project Structure

- `src/main/java/com/expensetracker/`
  - `model`: Contains data model classes and database access objects
  - `servlet`: Contains servlet components to handle HTTP requests
  - `controller`: Contains business logic to connect models and views
  - `util`: Contains utility classes like database connection handler
- `src/main/webapp`: Contains JSP pages, CSS, JavaScript and web resources

## Diagrams

The following diagrams provide a visual representation of the application's architecture and functionality:

### Class Diagram

<img src="https://drive.google.com/uc?export=view&id=1mcX2GH5x3ehqZCdFvFLXGmriUlTypjet" alt="Class Diagram" width="400">

_Class diagram showing the relationships between the key classes in the application._

### Use Case Diagram

<img src="https://drive.google.com/uc?export=view&id=10vrzy96yyDXSC95vcCKIgCOqHIHcrUFS" alt="Use Case Diagram" width="400">

_Use case diagram showing the interactions between users and the system._

### Activity Diagram

<img src="https://drive.google.com/uc?export=view&id=1JylLqxF6BuFnWnA61xyQIrx3ZcyntNnI" alt="Activity Diagram" width="400">

_Activity diagram illustrating the flow of operations within the application._

## Usage Guide

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
