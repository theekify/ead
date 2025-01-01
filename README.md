# EAD-Coursework

# Learning Management System (LMS)

This Learning Management System (LMS) is a project developed as part of an **Enterprise Application Development** module for a university assignment. The project aims to showcase a well-structured application with clean architecture, robust features, and professional software engineering principles. 

---

## 📋 Key Features

### 1. **Transactional UI**
- A dedicated user interface for critical operations, such as enrolling students in courses.

### 2. **Input UIs**
- Intuitive forms to add:
  - Users (e.g., students, instructors, and administrators).
  - Courses (complete with details like title, description, and schedule).
  - Assignments (linked to courses with submission deadlines).

### 3. **Design Patterns**
- **Singleton Pattern:** Ensures a single instance for managing database connections efficiently and safely.
- **MVC (Model-View-Controller):** Organizes the project into logical components:
  - **Model:** Handles data logic and interaction with the database.
  - **View:** Represents the user interface layer.
  - **Controller:** Mediates communication between Model and View.

### 4. **Dashboard**
- Offers a visual overview of key metrics:
  - Total students registered.
  - Number of available courses.
  - Recent enrollment activities.

### 5. **Reports**
- Dynamic report generation to display:
  - Enrollment records.
  - Grades and student performance metrics.
  - Data aggregated from multiple database tables.

### 6. **Exception Handling and Validations**
- Implements robust checks and error handling for:
  - Invalid user inputs (e.g., empty fields or mismatched data types).
  - Database errors (e.g., connection timeouts or failed queries).

### 7. **Database**
- **Schema Design:** Built to handle various entities of the LMS system, ensuring smooth relationships between tables.
- **CRUD Operations:** Supports creating, reading, updating, and deleting records for all entities, such as users, courses, and assignments.

### 8. **Setup File**
- The application is packaged as a `.jar` file or installer:
  - Easy for distribution and execution across different systems.

---

## 🛠️ Tech Stack

- **Programming Language:** Java.
- **UI Framework:** Swing.
- **Database:** MySQL.
- **IDE:** Visual Studio Code.

---
