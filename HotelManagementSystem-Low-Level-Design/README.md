# Hotel Management System - Low Level Design

## Overview
This project provides a low-level design for a Hotel Management System. The system is responsible for managing hotel rooms, reservations, and billing operations. It allows users to book rooms, manage reservations, and process payments through various payment methods.

## Features
- **User Management**: Create and manage user details.
- **Room Management**: Handle different types of rooms (Single, Double, Suit, Delux) with status tracking (Available, Booked, Occupied, Maintenance).
- **Reservation System**: Reserve, cancel, check-in, and check-out of rooms.
- **Billing System**: Generate bills based on the duration of stay and process payments via multiple methods (UPI, Card, Cash).
- **Hotel Information**: Store and retrieve hotel details like name, address, contact info, and room inventory.

## Class Structure

### 1. `User`
Represents a user with attributes such as `userName`, `emailId`, `contactNumber`, and `userId`.

### 2. `Location`
Stores the location details including `country`, `state`, `city`, and `pincode`.

### 3. `Room`
An abstract class representing a room with attributes like `roomNumber`, `roomType`, `roomStatus`, and `roomPrice`. It has concrete subclasses:
- `SingleRoom`
- `DoubleRoom`
- `SuitRoom`
- `DeluxRoom`

### 4. `RoomController`
Manages the inventory of rooms, providing methods to add, remove, and retrieve rooms based on their type and availability.

### 5. `Hotel`
Represents the hotel entity, holding information such as `hotelName`, `hotelRating`, `hotelAddress`, `contactNumber`, `emailId`, and a `RoomController` instance.

### 6. `HotelManagement`
Handles overall hotel operations such as room booking, reservation cancellation, check-in/check-out procedures, and payment processing.

### 7. `Reservation`
Represents a reservation with attributes like `reservationId`, `room`, `checkInDate`, `checkOutDate`, `user`, and `reservationStatus`. It manages room booking, reservation cancellation, and validation.

### 8. `Bill`
Generates bills based on reservation details, calculates total stay costs, and handles bill status updates.

### 9. `Payment`
Processes payments via different payment modes by interacting with the `PaymentMode` interface.

### 10. `PaymentMode` Interface
Defines the contract for payment methods, implemented by:
- `UpiPayment`
- `CardPayment`
- `CashPayment`

## Usage

1. **Create Users**: Instantiate `User` objects with user details.
2. **Create and Manage Rooms**: Use `RoomController` to add and manage room inventory.
3. **Book a Room**: Use `HotelManagement` to create a reservation.
4. **Manage Reservations**: Reserve, cancel, check-in, and check-out of rooms.
5. **Process Payments**: Handle payments through UPI, Card, or Cash using the `Payment` class.

## Future Enhancements

- **Database Integration**: 
  - Integrate with a database for persistent storage of reservations and billing information to improve data management and retrieval.

- **User Interface**: 
  - Implement a user interface to enhance ease of use and provide a more interactive experience for users managing reservations and payments.

- **Additional Features**:
  - Add more features such as room service, customer feedback, and promotional offers to provide a comprehensive hotel management experience and improve customer satisfaction.

## Sample Output

```
----- Bill Details -----
Bill Number: BILL-0B7CA505
Reservation ID: RES-F60A8331
User Name: milly
Room Number: RDX-006
Room Type: DELUX
Check-In Date: 2024-08-28
Check-Out Date: 2024-09-01
Total Amount: $32000.0
Bill Status: COMPLETE
------------------------
