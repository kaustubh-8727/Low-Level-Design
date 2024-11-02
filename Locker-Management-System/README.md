# Locker Management System

This project simulates a Locker Management System, handling end-to-end order processing with locker allocations. Key components include user and order management, locker allocations based on user needs, and delivery personnel assignments. The code organizes classes, enums, and interfaces into an effective, modular design for managing order workflows and locker allocation services.

## Table of Contents
1. [Overview](#overview)
2. [Classes and Core Components](#classes-and-core-components)
3. [Key Services](#key-services)
4. [Enums](#enums)
5. [Usage and Flow](#usage-and-flow)
6. [Example Code Execution](#example-code-execution)

## Overview

The Locker Management System enables users to create orders, assigns delivery personnel, and allocates lockers to securely store delivered items. Key features include:
- User registration and order creation
- Cart management
- Delivery personnel assignment and management
- Locker allocation based on size and availability
- Order authentication and retrieval from lockers

## Classes and Core Components

### 1. **User**
   - Represents a customer with unique `userId`, address, `cart`, and OTP.
   - Methods include adding and removing products from the cart, and setting OTP for locker authentication.

### 2. **DeliveryMan**
   - Manages details of the delivery personnel such as ID, status, and contact information.
   - `DeliveryManStatus` indicates availability.

### 3. **Location**
   - Stores location details including `country`, `state`, `city`, `area`, and `pincode`.

### 4. **Locker**
   - Represents a locker with a unique `lockerId`, `lockerCategory` (size), and `lockerStatus`.
   - Can generate and store an OTP for order retrieval.

### 5. **Order**
   - Encapsulates an order's information, including ID, status, associated user, and a list of products.
   - Methods for checkout, setting locker requirements, and payment processing.

### 6. **Product**
   - Represents an individual product with name, ID, and price.

### 7. **Cart**
   - Maintains a mapping of `productId` to quantity within a user's cart.

## Key Services

### 1. **DeliveryManService**
   - Manages a list of delivery personnel and provides methods to assign an available delivery man to an order.

### 2. **LockerService**
   - Provides methods to allocate lockers based on locker allocation strategies (e.g., by size or location).
   - Initializes a grid of lockers and provides OTP-based access.

### 3. **OrderController**
   - Main controller managing order creation, checkout, and delivery workflows.
   - Coordinates between `LockerService` and `DeliveryManService` to handle locker allocation and delivery.

## Enums

- **DeliveryManStatus**: Tracks if delivery personnel are `AVAILABLE` or `NOT_AVAILABLE`.
- **OrderStatus**: Represents the status of an order (`INITIATED`, `IN_DELIVERY`, `DELIVERED`, etc.).
- **LockerCategory**: Defines locker sizes (`SMALL`, `LARGE`, `EXTRA_LARGE`).
- **LockerStatus**: Indicates if a locker is `EMPTY` or `OCCUPIED`.

## Usage and Flow

1. **User and Product Setup**
   - Users can be created with a name and address.
   - Products are added to a user’s cart, specifying quantity.

2. **Order Creation and Checkout**
   - A user’s cart can be converted into an order through the `OrderController`.
   - During checkout, users may request locker storage, generating a secure OTP for locker access.

3. **Locker Assignment and Authentication**
   - The `LockerService` assigns an available locker based on a specified strategy.
   - The system generates an OTP for locker access; only the user with the correct OTP can retrieve their order.

4. **Delivery Assignment**
   - The `DeliveryManService` assigns an available delivery person to the order.
   - Delivery status updates are provided, along with order details and delivery contact info.

## Example 

```
Order Checkout and Delivery:

Order Details:
Order ID: 2050
Status: COMPLETED
User: Alice
Delivery Address: City, Area, 100001

Products:
Product ID: 5088, Quantity: 1
Product ID: 9819, Quantity: 1

Invoice Summary:
Total Item Price: 400.0
Tax: 0.0%
Total Amount Paid: 400.0

Delivery Information:
Delivery Person: John Doe
Contact: 1234567890
Status: On Delivery

Retrieving Order from Locker:
Order ID: 2050 is delivered and stored in Locker ID: 8327
Order retrieved successfully from locker.
