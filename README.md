# Library Management System
A modern desktop application built with JavaFX for managing a library's inventory, users, and transactions. This project showcases advanced software engineering principles including Role-Based Access Control (RBAC), Design Patterns (Builder, Decorator, Strategy), and PDF Report Generation.

## Overview
The system supports three distinct user roles (Administrator, Employee, Customer) with specific permissions. Built on a Layered Architecture, the application separates domain entities, business logic, data access, and presentation layers to ensure maintainability and scalability. 

##  Key Features
- Role-Based Access Control (RBAC):
  - Admin: Manage users, generate reports, view all data
  - Employee: Manage books, process sales
  - Customer: Browse books
- Advanced Design Patterns:
  	- Builder: For constructing complex objects 
    -  Strategy: Implemented through repository interfaces, allowing interchangeable data access strategies without modifying business logic
    - Decorator: The BookRepositoryCacheDecorator class is implemented to wrap repository calls with caching logic
- PDF Reporting: Generates activity reports using iText library
- Notification Pattern: Notification<T> class for robust error handling 

##  Technologies Used
- Language: Java
- GUI: JavaFX
- Database: MySQL
- PDF Generation: iText
- Design Patterns: Builder, Decorator, Strategy
- Architecture: Layered 

##  Architecture
- Model: Domain entities
- Repository: Data access layer 
- Service: Business logic layer 
- Controller: Handles UI events
- View: JavaFX UI
