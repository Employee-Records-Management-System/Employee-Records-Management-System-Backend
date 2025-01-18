# Employee Records Management System

## Overview

The **Employee Records Management System (ERMS)** is a centralized application for managing employee data across departments. It provides role-based access control for HR personnel, managers, and administrators, alongside features like an audit trail, search and filtering capabilities, and reporting.

The system is built using **Java 17**, **Oracle SQL**, and **Docker** for a robust backend, with a **Swing-based desktop UI** for user interaction. API endpoints are documented using **Swagger OpenAPI**.

---

## Features

1. **Employee Data Management**:
    - Manage employee attributes: Full Name, Employee ID, Job Title, Department, Hire Date, Employment Status, Contact Information, and Address.

2. **Role-Based Access Control**:
    - **HR Personnel**: Full CRUD access to all employee data.
    - **Managers**: Limited edit access for employees in their department.
    - **Administrators**: Full system access, including configuration and permissions management.

3. **Audit Trail**:
    - Logs all changes to employee records, including details of the changes and the user who made them.

4. **Search and Filtering**:
    - Search employees by name, ID, department, or job title.
    - Filter by employment status, department, and hire date.

5. **Validation Rules**:
    - Enforces predefined validation rules (e.g., valid email format, unique employee ID).

6. **Reporting**:
    - Generate basic reports for employee data and actions.

7. **User Interface**:
    - Built with **Swing** using **MigLayout** and **GridBagLayout**.

8. **Dockerized Application**:
    - Fully Dockerized for easy deployment and local testing.

---

## Getting Started

### Prerequisites

- **Java 17**
- **Oracle SQL** database
- **Docker** and **Docker Compose**
- **Postman** for API testing

### Installation Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/zzach666/Employee-Records-Management-System
   cd Employee-Records-Management-System
    ```
2. **Set Up the Database**
   Configure the application.properties file with Oracle SQL credentials.
   Use the provided SQL scripts in the /database directory to create tables and seed initial data.

3. **Build and Run the Application**
   ```bash
   mvn clean install
   docker-compose up --build
   ```
4. **Access Swagger Documentation**
   Navigate to http://localhost:8080/swagger-ui.html to explore API endpoints.
5. **Run the Desktop Application**
   ```bash
   java -jar target/erms-ui.jar
   ```
## Postman API Collection

Use the following Postman collection to validate the API:

[Postman Collection Link](https://hahn-software-team-8036.postman.co/workspace/My-Workspace~1e2eafa2-3d81-4b1c-8033-c730d525f957/collection/22686153-0f81124d-7a24-4a38-b528-36b5ea2d6e29?action=share&creator=22686153)

---

## Testing

### Unit and Integration Tests

- Written using **JUnit** and **Mockito**.
- To run tests:

```bash
mvn test
```
## Documentation
### API Documentation
- Available via Swagger UI.