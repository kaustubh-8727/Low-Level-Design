# Food Delivery App - Low-Level Design

## Overview

This repository contains the low-level design (LLD) of a food delivery application implemented in Java. The system simulates the core functionalities of a food delivery app, including user management, restaurant management, order placement, and delivery handling.

## Features

### User Management
- **User**: Represents a customer in the system. Users can browse restaurants, add items to their cart, place orders, and receive notifications about their order status.

### Restaurant Management
- **Restaurant**: Represents a restaurant in the system. Restaurants can add food items to their menu, manage orders, and receive ratings from users.
- **Food Item**: Represents a dish offered by a restaurant. Each food item has a unique ID, name, type (e.g., Chinese, Italian, Indian), price, and availability status.

### Order Management
- **Order**: Represents a customer's order. An order includes the user, the restaurant, the ordered items, the delivery address, and the assigned delivery agent.
- **Order Status**: An enum representing the various stages of an order (e.g., Pending, Confirmed, Preparing, Out for Delivery, Delivered, Cancelled).
- **OrderItem**: Represents an individual item within an order, including the food item and its quantity.
- **Invoice**: Represents the invoice generated for an order, including the total price calculation.

### Delivery Management
- **Delivery Agent**: Represents a delivery person responsible for delivering orders. Agents have a unique ID, name, contact number, and status (Available/Not Available).
- **Delivery Agent Status**: An enum representing the availability status of a delivery agent.

### Rating System
- **Rating**: Manages the ratings provided by users for a restaurant. Each rating includes a score and optional comments.

## System Components

### Enums
- **FoodItemStatus**: Enum representing the availability status of a food item (`AVAILABLE`, `NOT_AVAILABLE`).
- **OrderStatus**: Enum representing the status of an order (`PENDING`, `CONFIRMED`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`).
- **FoodItemType**: Enum representing the type of food item (`CHINESE`, `ITALIAN`, `INDIAN`, `MEXICAN`).
- **DeliveryAgentStatus**: Enum representing the status of a delivery agent (`AVAILABLE`, `NOT_AVAILABLE`).

### Classes
- **User**: Manages user information, including user ID, name, email, contact number, location, and cart. Users can add items to their cart and place orders.
- **Location**: Represents a location with fields for country, state, city, and pincode.
- **Restaurant**: Manages restaurant information, food items, orders, and ratings. Restaurants can notify users about order status and manage their menu.
- **RestaurantController**: Manages the list of restaurants, allowing for restaurant addition, removal, and search by food item.
- **Rating**: Handles the ratings given to restaurants, calculating the average rating based on user inputs.
- **FoodItem**: Represents a food item with details like ID, name, type, price, and status.
- **Cart**: Manages the user's cart, including adding and removing order items.
- **DeliveryAgent**: Manages delivery agent information and order assignments.
- **Order**: Represents an order placed by a user, including details like order ID, user, restaurant, invoice, delivery agent, and status. Handles order creation, status updates, and agent assignment.
- **OrderController**: Manages orders, including placing orders, updating statuses, and allocating delivery agents.
- **OrderItem**: Represents an individual item in an order, including the food item and its quantity.
- **Invoice**: Generates an invoice for an order and calculates the total price of the ordered items.

## How to Run

1. **Compile the Java Code**: Compile all the Java files using a Java compiler (e.g., `javac`).
2. **Run the Main Class**: Execute the `FoodDeliveryApp` class, which contains the main method to simulate the app's functionalities.

### Example Simulation
The main class simulates the following workflow:

1. A user is created and a delivery agent is added to the system.
2. Food items are created and added to a restaurant.
3. The restaurant is added to the system.
4. The user searches for food items, adds them to the cart, and places an order.
5. The order status is updated, and the delivery agent is notified to deliver the order.

## Future Enhancements

- Add more detailed exception handling and validation.
- Implement a UI or API layer for user interaction.
- Introduce more complex features like discounts, loyalty points, and advanced search filters.
- Expand the rating system to include ratings for delivery agents and food items.

## Conclusion

This LLD provides a basic yet functional framework for a food delivery application. It is designed to be easily extendable and serves as a foundation for building more advanced features.
