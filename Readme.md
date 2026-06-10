# Expense Tracker Backend

A Spring Boot backend application for tracking expenses, managing monthly budgets, generating summaries, and exporting expense data to CSV.

## Live Demo
- Frontend: https://dss70tm98gi6z.cloudfront.net

## AWS Architecture
- Frontend: S3 + CloudFront (HTTPS)
- Backend: EC2 (t3.micro) + CloudFront
- Database: RDS PostgreSQL (Multi-database architecture)
- Security: VPC Security Groups, JWT Authentication

## Environment Variables
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- JWT_SECRET
- CORS_ALLOWED_ORIGINS

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
- Authorization Token(JWT): Bearer <token>(For Protected API's)
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
- Aws S3 Bucket for frontend
- Aws Elastic Compute Cloud (EC2) for backend
- Aws Relational Database Service (Amazon RDS) for Database Services
- Aws Cloudfront for fast content delivery network(CDN) service that securely delivers data, API's to customers globally with low latency and high transfer speeds.

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