# Product Review System

A robust and modular Java-based system for managing users, products, and their reviews. This application is designed to provide an efficient way to handle product reviews, user management, and filtering features for various business needs.

---

## Features

### 1. User Management
- Create, remove, and fetch users.
- Manage user details such as name, email, and contact information.

### 2. Product Management
- Add, remove, and fetch products.
- Manage product details like name, type, description, and price.

### 3. Review Management
- Add, update, and remove reviews for products.
- Supports rating, comments, and review types (Positive, Negative, Neutral).
- Generate summary for reviews, including average rating and predominant sentiment.

### 4. Review Filtering
- Filter reviews based on criteria like:
  - Review type (Positive/Negative/Neutral)
  - Rating range
  - Text content

---

## Components

### Classes
- **`User`**: Represents a user in the system.
- **`UserManagementService`**: Handles user-related operations.
- **`Product`**: Represents a product in the system.
- **`ProductService`**: Manages product-related operations.
- **`Review`**: Represents a product review.
- **`ReviewService`**: Manages review operations.
- **`ReviewSummary`**: Provides a summary of reviews for a product.
- **`ReviewFilter`**: Allows filtering of reviews based on specified criteria.

### Enums
- **`ReviewType`**: Defines the type of review as `POSITIVE`, `NEGATIVE`, or `NEUTRAL`.

---

## Architecture

The system follows a **Singleton Design Pattern** for services:
- **`UserManagementService`**, **`ProductService`**, and **`ReviewService`** are singleton classes, ensuring only one instance per service.

All services are accessible via the **`ServiceFactory`**, providing centralized management and improved modularity.

---
