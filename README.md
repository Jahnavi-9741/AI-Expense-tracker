# Simple AI Smart Expense Tracker

This is a beginner-friendly Spring Boot project with a simple structure and a clean frontend.

## Features
- Add expenses
- View expense history
- Total spending dashboard
- AI insight message
- PDF report download
- MySQL database
- Thymeleaf + Bootstrap UI

## Project Structure
src/main/java/com/example/expense
- ExpenseApplication.java
- Expense.java
- ExpenseRepository.java
- ExpenseController.java
- AIInsightService.java
- PDFService.java

src/main/resources
- templates/dashboard.html
- application.properties

## Database
Create this database in MySQL:

CREATE DATABASE expense_db;

## Run
Update DB username and password in:
src/main/resources/application.properties

Then run:
mvn spring-boot:run

Open:
http://localhost:8080
