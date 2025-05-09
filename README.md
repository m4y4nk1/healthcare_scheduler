# Healthcare Scheduler

**This is a full-fledged production-level Healthcare Scheduler** built using Spring Boot, Spring Security, JWT, Thymeleaf, and PostgreSQL. It supports role-based access for doctors and patients, appointment scheduling, availability management, medical records, and email notifications.

## 🌐 Live Demo
Hosted locally — Run instructions below.

## 📌 Features

- User Registration and Login (JWT secured)
- Role-based Dashboards (Doctor, Patient)
- Appointment Scheduling and Management
- Doctor Availability Management
- Patient Profile and Medical Record Views
- Email notifications (confirmation, reminders, verification)
- JWT Security and Exception Handling
- PostgreSQL Integration
- H2 Console support for dev mode

---

## 🚀 Technologies Used

- Java 17
- Spring Boot 3.4.5
- Spring Security (JWT)
- Thymeleaf (for HTML rendering)
- PostgreSQL (production DB)
- H2 (in-memory DB for development)
- Maven
- IntelliJ IDEA

---

---

## 🛠️ How to Run

### 1. Clone the Repository
```bash
git clone https://github.com/m4y4nk1/healthcare_scheduler.git
cd healthcare_scheduler
2. Configure Database
Update src/main/resources/application.properties:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/healthcare_scheduler
spring.datasource.username=postgres
spring.datasource.password=your_password
3. Run the App
Using Maven wrapper:

./mvnw spring-boot:run
Or using IntelliJ:

Open project

Run HealthcareSchedulerApplication.java

🔐 Login URLs
App Entry: http://localhost:8080/healthcare/

Login: http://localhost:8080/healthcare/auth/login

Register: http://localhost:8080/healthcare/auth/register

Doctor Dashboard: http://localhost:8080/healthcare/doctor/dashboard

Patient Dashboard: http://localhost:8080/healthcare/patient/dashboard

H2 Console (dev only): http://localhost:8080/healthcare/h2-console

✉️ Contact
Created by m4y4nk1 — feel free to drop a star ⭐

📝 License
This project is licensed under the MIT License.
