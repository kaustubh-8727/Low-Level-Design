# **Restaurant Management System (Low-Level Design)**

## **📖 Overview**
The **Restaurant Management System** is a **Low-Level Design (LLD)** that models the core functionalities of a restaurant, including:

- **🍽️ Menu Management**: Adding, updating, retrieving, and deleting food items.
- **👨‍🍳 Staff Management**: Managing different roles such as chefs, waiters, cleaners, and billing staff.
- **📝 Order Management**: Placing orders, updating order statuses, and retrieving order details.
- **📦 Inventory Management**: Handling ingredients required for food preparation.
- **💳 Billing & Payment**: Generating bills and processing payments via multiple payment methods.

---

## **🏗️ Design Components**

### **🔹 1. FoodItem**
**Represents the details of a food item in the menu.**

### **🔹 2. Menu**
**Manages CRUD operations for food items.**

### **🔹 3. Inventory**
**Manages the ingredients required for cooking.**

### **🔹 4. StaffPerson (Interface)**
**Represents restaurant staff (chefs, waiters, cleaners, etc.).**

### **🔹 5. StaffService**
**Handles CRUD operations for staff members.**

### **🔹 6. Order**
**Represents an order placed by a customer.**

### **🔹 7. OrderService**
**Manages orders, including order placement and status updates.**

### **🔹 8. Bill**
**Handles bill generation for an order.**

### **🔹 9. Payment Service**
**Manages payments using various methods (UPI, Card, Credit Card, etc.).**

---

## **🏷️ Enumerations**

### **📌 1. FoodCategory**
`INDIAN`, `MEXICAN`, `CHINESE`, `THAI`.

### **📌 2. FoodStatus**
`AVAILABLE`, `NOT_AVAILABLE`.

### **📌 3. StaffRole**
`CLEANING`, `CHEF`, `WAITER`, `ADMIN`, `BILLING`.

### **📌 4. StaffStatus**
`AVAILABLE`, `ABSENT`, `ON_LEAVE`.

### **📌 5. OrderStatus**
`IN_PROGRESS`, `COOKED`, `SERVED`.

### **📌 6. PaymentStatus**
`PENDING`, `FAILED`, `SUCCESS`.

---
