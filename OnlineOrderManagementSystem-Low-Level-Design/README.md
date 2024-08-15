# Order Management System - Low-Level Design

This project presents the low-level design of an **Order Management System** implemented in Java. The system manages products, categories, inventory, warehouses, users, carts, orders, and payments. It supports different warehouse selection strategies and multiple payment methods.

## Overview

The Order Management System allows users to browse products, add items to a cart, select a warehouse for order fulfillment, and proceed with payment. It handles various order statuses and provides the flexibility to choose different warehouse selection strategies and payment methods.

## System Components

### 1. **Product and Category**
- **Product**: Represents a product in the system, identified by `productId` and `productName`.
- **ProductCategory**: Groups products into categories, managing stock and price.

### 2. **Inventory and Warehouse**
- **ProductInventory**: Manages the collection of product categories and handles adding/removing products.
- **Warehouse**: Contains an inventory and is associated with a physical address. Warehouses can add or remove products based on orders.

### 3. **User and Cart**
- **User**: Represents a user in the system, identified by `userId` and `userName`. Users can add or remove products from their cart.
- **Cart**: Manages the products selected by the user before placing an order.

### 4. **Order Management**
- **Order**: Represents an order placed by a user, tracking order status, products, warehouse, and payment.
- **OrderController**: Manages the creation, retrieval, and checkout of orders.

### 5. **Payment**
- **Payment**: Handles payment processing. 
- **PaymentMode**: Interface implemented by different payment methods like `UpiPayment` and `CardPayment`.

### 6. **Warehouse Selection Strategies**
- **WarehouseSelectionStrategy**: Interface for selecting warehouses based on different criteria.
- **CheapestWarehouseSelectionStrategy**: Selects the cheapest warehouse.
- **ClosestWarehouseSelectionStrategy**: Selects the closest warehouse based on the user’s address.

## Class Diagram

The class diagram outlines the relationships and interactions between various components of the system. You can visualize the hierarchy and associations through the UML diagrams provided in this project.

## Usage

The system is designed to simulate an online shopping experience. Here's a quick overview:

1. **Initialize the System**: Set up the system by creating products, categories, inventory, and warehouses.
2. **User Actions**:
   - Users browse and add products to their cart.
   - Select a warehouse based on a strategy.
   - Place an order and proceed with payment.
3. **Order Processing**:
   - The system processes the order, updates inventory, and completes the payment.

## Key Features

- **Modular Design**: Each component (Product, Warehouse, Order, etc.) is designed as a separate class, promoting modularity and reusability.
- **Strategy Pattern**: Implements different warehouse selection strategies using the Strategy pattern.
- **Payment Flexibility**: Supports multiple payment methods through the PaymentMode interface.
- **Order Lifecycle Management**: Handles different stages of an order's lifecycle, including initiation, payment processing, and completion.
