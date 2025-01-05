# Concert Booking System

## Overview

The Concert Booking System is a low-level design implementation aimed at providing a seamless platform for users to book tickets for concerts. It includes features for user management, concert scheduling, seat reservations, payments, and notifications. The system incorporates multiple seat types, reservation statuses, and billing processes.

## Features and Components

### 1. Show
Represents the concert or event to be booked. Each show includes details like:
- Name
- Date
- Venue
- Artist
- Ticket price

### 2. User
Manages user information including:
- Username
- Email
- Contact number
- Address

### 3. User Management
Handles user-related operations such as:
- Creating accounts
- Retrieving user information
- Notifying users about their reservations

### 4. Show Manager
Enables administrators to:
- Add concerts
- Update concert details
- Remove concerts from the system

### 5. Artist
Represents artists performing in concerts. Includes details like:
- Artist name
- Genre
- Description

### 6. Seat
Supports multiple seat types, such as:
- First Class
- Second Class
- Third Class

Each seat type has a unique price and status (e.g., Available, Reserved, Booked).

### 7. Reservation
Manages the reservation process, including:
- Seat selection
- Reservation status
- Cancellation

### 8. Payment
Generates bills based on seat selection and applies additional charges like taxes.

### 9. Bill
Contains details like:
- Bill ID
- Concert name
- Total price
- Date of billing
- Bill status (e.g., Pending, Success)

### 10. Notification
Notifies users about:
- Reservation updates
- Cancellations
- Payment status

## Classes and Enums

### Enums
```java
public enum SeatStatus {
    AVAILABLE,
    RESERVED,
    BOOKED,
    UNDER_MAINTENANCE
}

public enum ReservationStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}

public enum BillStatus {
    PENDING,
    SUCCESS,
    FAILED
}
```

### Key Classes
- **User**: Stores user details and sends notifications.
- **Location**: Represents geographical details of users or concert venues.
- **Concert**: Manages concert information.
- **ConcertController**: Handles operations like adding, updating, and removing concerts.
- **Artist**: Stores artist details.
- **Seat**: Abstract class for different seat types.
- **FirstClassSeat, SecondClassSeat, ThirdClassSeat**: Specific seat types with predefined prices.
- **Reservation**: Manages reservation status and associated seats.
- **Bill**: Generates and manages billing for reservations.

## How to Run

```bash
# Clone the repository
git clone https://github.com/yourusername/concert-booking-system.git

# Navigate to the project directory
cd concert-booking-system

# Compile and run the program
javac *.java
java Main
```
