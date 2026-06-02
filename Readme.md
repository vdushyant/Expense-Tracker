# Expense Tracker Backend

A Spring Boot backend application for tracking expenses, managing monthly budgets, generating summaries, and exporting expense data to CSV.

## Features

- Add, update, delete, and view expenses
- View all expenses with pagination and sorting
- Filter expenses by category
- View total expense summary
- View monthly expense summary
- Set monthly budget
- Check monthly budget status and warnings
- Export expenses to CSV
- Request validation
- Global exception handling
- Audit fields: `createdAt` and `updatedAt`

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Hibernate
- Lombok
- Maven
- Postman

## Project Structure

```text
src/main/java/com/dushyant/expensetracker
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
└── ExpenseTrackerApplication.java