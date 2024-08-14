# Car Booking System

This repository contains a simple implementation of a Car Booking System in Java. The system allows users to reserve vehicles, manage inventory, and generate bills for completed reservations. It includes classes for managing vehicles, users, stores, reservations, and billing.

## Introduction

The Car Booking System simulates a basic vehicle rental service. Users can book vehicles, and the system tracks vehicle availability, reservation status, and billing.

## Features

- **Vehicle Management**: Add and remove vehicles, check vehicle availability.
- **Reservation Management**: Create, complete, and cancel reservations.
- **Billing**: Generate and display invoices for completed reservations.
- **Location-Based Store Management**: Users can select vehicles from different stores based on their location.

## Class Structure

- **Vehicle**: Represents a vehicle with attributes like company name, type, model, seats, rental cost, and status.
- **Car/Bike**: Inherits from Vehicle, allowing for future extensions with additional attributes.
- **VehicleInventoryManagement**: Manages a list of vehicles, adding, removing, and filtering by availability.
- **Reservation**: Handles the reservation process, including creation, completion, and cancellation.
- **Location**: Represents the geographical location of a store with attributes like country, state, city, and pincode.
- **Store**: Contains a vehicle inventory and manages reservations at a specific location.
- **User**: Represents a user with attributes like name, age, license ID, and contact number.
- **CarBookingManagement**: Manages the overall system, including users, stores, and the vehicle-store mapping.
- **Bill**: Generates and displays the invoice for a completed reservation.
- **Payment**: Processes the payment for the generated bill.

## Usage

The main flow of the system is as follows:
1. **Initialize the System**: Create an instance of `CarBookingManagement`.
2. **Create Vehicles and Users**: Instantiate vehicles and users.
3. **Set Up Stores**: Create stores with vehicle inventories and associate them with specific locations.
4. **Booking Process**:
   - User selects a location and views available stores.
   - User fetches available vehicles from a store.
   - User creates a reservation for a selected vehicle.
   - A bill is generated for the reservation.
   - User completes the payment.
   
Refer to the `main` method in the `CarBookingsystem` class for an example of how to use the system.

