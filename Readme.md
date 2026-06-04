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
- Authorization: Bearer <token>(For Protected API's)
- User-specific expenses and budgets
- Swagger Authorize button usage

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring JWT
- Spring Security
- PostgreSQL
- Hibernate
- Lombok
- Maven
- Postman
- Swagger
- Docker

## Project Structure

```text
src/main/java/com/dushyant/expensetracker
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── ExpenseTrackerApplication.java

## Run with Docker

Make sure Docker Desktop is running.

Build and start the application with PostgreSQL:

```bash
docker compose up --build