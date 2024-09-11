# Airline Management System

## Overview

This Airline Management System is designed to simulate a flight reservation system. It allows users to search for available flights, select preferred seats, and complete their booking through different payment methods. The system uses various design patterns such as the **Decorator Design Pattern** for seat pricing.

## Features

1. **Flight Management**:
   - Flights contain fare details and available seats.
   - Users can search for flights based on origin, destination, and time filters.
   
2. **Airline Management**:
   - Each airline is associated with a flight manager responsible for maintaining flights and reservations.
   - An airline manager manages all available airlines and retrieves flights based on user needs.

3. **Reservation System**:
   - Users can reserve flights with preferred seats.
   - Seats are updated in real-time as reserved, booked, or available.
   
4. **Seat Pricing**:
   - The system uses the **Decorator Design Pattern** to calculate seat prices for different seat types (Economy, Business, First-Class).

5. **Payment**:
   - Supports multiple payment modes (UPI, Card).
   - Payments are processed to complete the reservation, or the reservation is canceled if payment fails.

## Class Structure

### 1. `Flight`
- Stores flight details such as flight number, source, destination, departure, and arrival times.
- Manages available seats and calculates the default flight price.

### 2. `FlightManager`
- Maintains the list of flights for an airline.
- Provides functionality to add or remove flights.
- Filters flights based on criteria like source, destination, and departure time.

### 3. `Airline`
- Contains a flight manager for managing flights.
- Handles the reservation process, including validation, creation, and cancellation.

### 4. `AirlineManager`
- Manages all available airlines and facilitates user flight search and booking.
- Books a flight for a user with selected seats and handles reservation cancellations.

### 5. `Seat`
- Abstract class representing a seat with properties such as seat number, seat type (Economy, Business, First-Class), and price.
- Updates seat status (Available, Reserved, Booked).

### 6. `User`
- Represents a user making a flight reservation.
- Stores user details like name, ID, contact number, and email.

### 7. `Reservation`
- Manages reservations for flights.
- Calculates the total price based on seat type and flight price.
- Cancels and completes reservations by updating seat statuses.

### 8. `Payment`
- Handles the payment process for reservations.
- Supports different payment modes (UPI, Card).

## Sample Output

```
Reservation ID: RES-D82C2CFA
User: jimmy (ID: BG6767JK)
Contact: 86765656655, Email: jimmy@gmail.com
Flight Details:
Flight Number: FL-EF57390E
Source: ontario, Destination: Brisbane
Departure: 2024-09-13T19:27:19.934285, Arrival: 2024-09-12T14:27:19.934303
Seats Reserved:
 - Seat Number: SEC-1 (ECONOMY) - Price: 200.0
 - Seat Number: SEB-2 (BUSINESS) - Price: 600.0
 - Seat Number: SEF-4 (FIRST_CLASS) - Price: 1200.0
Total Price: 9800.0
Reservation Status: Completed