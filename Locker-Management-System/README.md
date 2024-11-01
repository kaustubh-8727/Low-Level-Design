# Locker Management System

## Overview
The **Locker Management System** is designed for e-commerce platforms, allowing users to choose a nearby locker for convenient package delivery and return. This system manages locker allocation, authentication, and notifications, providing users with secure, flexible, and reliable package pickup and return options.

### Key Features
- Random or size-based locker allocation
- OTP-based authentication for secure package access
- Notifications to users and delivery personnel
- Admin tools for monitoring and vacating lockers in extended use

---

## Table of Contents
1. [System Components](#system-components)
2. [Entity Descriptions](#entity-descriptions)
3. [Installation and Setup](#installation-and-setup)
4. [Usage](#usage)
5. [Future Enhancements](#future-enhancements)

---

## System Components

### Core Entities
- **Person**: Represents any individual using the system, including customers, delivery personnel, and admins.
- **Order**: Contains order-specific details and links to locker allocation.

### Locker and Management
- **Locker**: Represents an individual locker where packages are stored.
- **Locker Manager**: Manages locker availability and assignment.
- **Locker Allocation Service**: Handles the assignment of lockers based on availability and package size.

### Authentication and Notifications
- **Authentication**: Generates and verifies OTPs to secure locker access.
- **Notification Service**: Sends OTPs and other notifications to customers and delivery personnel.

### User Roles
- **Delivery Person**: Manages package drop-off and pickup.
- **Delivery Manager**: Oversees delivery personnel and processes.
- **Admin**: Monitors system status and locker occupancy.

---

## Entity Descriptions

### 1. Person
Represents an individual in the system, holding general information such as name, contact, and role.

### 2. Customer
A type of Person representing users who place orders and retrieve or return items from lockers. They receive OTPs for secure package access.

### 3. DeliveryPerson
A type of Person responsible for delivering packages to lockers and managing returns.

### 4. Order
Contains order details, such as package size, customer ID, locker ID, delivery status, and OTP. Manages locker assignment and OTP generation.

### 5. Locker
Represents a physical locker, containing details such as locker ID, size, location, current status, assigned order, and OTP.

### 6. Locker Manager
Handles locker availability, assignment, and vacancy checks, including methods to monitor usage durations.

### 7. Locker Allocation Service
Determines locker allocation based on size and availability. Extensible for future custom allocation criteria (e.g., proximity).

### 8. Authentication
Manages OTP generation and validation to secure locker access for both delivery personnel and customers.

### 9. Notification Service
Facilitates OTP delivery and other notifications for customers and delivery personnel.

### 10. Admin
A role with access to monitor and manage locker occupancy and availability. Can view occupied lockers and vacate those with extended usage.

---
