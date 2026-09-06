# Hospital Management System

Basic Patient CRUD application using Spring Boot, Spring Data JPA, MySQL and Thymeleaf.

## Before running

1. Create MySQL database:
   CREATE DATABASE hospital_db;
2. Open `src/main/resources/application.properties`
3. Replace `YOUR_MYSQL_PASSWORD` with the local MySQL password.
4. Run `HospitalManagementApplication.java`
5. Open: http://localhost:8080/patients

## Architecture

Controller -> Service -> Repository -> Database
