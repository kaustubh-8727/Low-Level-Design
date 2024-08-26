# Parking Lot Low-Level Design

This repository contains the implementation of a low-level design for a Parking Lot system in Java. The system is designed to manage parking spots for different types of vehicles, handle reservations, generate bills, and process payments.

## Overview

The Parking Lot system is designed to manage vehicles, parking spots, reservations, and payments in an organized manner. It supports different types of vehicles, such as cars, bikes, and trucks, and provides a simple interface to reserve parking spots, calculate bills, and handle payments.

## Key Components

### 1. Vehicle
The `Vehicle` class represents a vehicle that enters the parking lot. It includes details like the vehicle number and the type of vehicle (`CAR`, `BIKE`, `TRUCK`). Specialized classes (`Car`, `Bike`, `Truck`) extend the `Vehicle` class for different vehicle types.

### 2. Parking Spot
The `ParkingSpot` class represents a parking spot in the parking lot. It includes details like the type of vehicle it can accommodate, its availability status, and the associated parking fee. Subclasses like `CarParkingSpot`, `BikeParkingSpot`, and `TruckParkingSpot` extend the `ParkingSpot` class to specify different types of parking spots.

### 3. Reservation
The `Reservation` class handles the process of reserving a parking spot for a vehicle. It tracks the reservation status (`PENDING`, `COMPLETED`, `CANCELLED`), the vehicle being parked, and the duration of the parking.

### 4. Bill
The `Bill` class generates a bill for the parking reservation based on the parking duration and the type of parking spot. It calculates the total amount and can print a detailed receipt.

### 5. Payment
The `Payment` class handles the payment process. It supports different payment methods via the `PaymentMode` interface, with implementations like `UpiPayment` and `CardPayment`.

### 6. Parking Spot Controller
The `ParkingSpotController` class manages the parking spots in the parking lot. It can add or remove parking spots, find an available spot for a specific vehicle type, and manage the reservation process.

## How It Works

- **Initialization**: The parking lot is initialized with a set of parking spots for different vehicle types.
- **Parking a Vehicle**: When a vehicle enters the parking lot, the system finds an available parking spot for the vehicle type. If a spot is available, the system reserves the spot and generates a bill.
- **Payment**: The system processes the payment using the selected payment method. If the payment is successful, the reservation is completed; otherwise, it is canceled.
- **Bill Generation**: A bill is generated based on the parking duration and printed for the user.

## Future Enhancements

- Implementing additional features such as dynamic pricing based on the time of day or vehicle type.
- Adding support for more payment methods.
- Expanding the system to handle multiple parking lots.

## Conclusion

This Parking Lot system is a simplified version of a real-world parking management system. It demonstrates key concepts of object-oriented design, including encapsulation, inheritance, and polymorphism, while providing a practical solution for managing a parking lot.

## Sample Output

```
welcome to parking lot low level design

----- Parking Lot Bill -----
Bill Number: 6330
Date: Mon Aug 26 18:31:42 GMT 2024
Vehicle Number: 1647
Vehicle Type: CAR
Parking Spot ID: 1001
Duration: 145 minutes
Total Amount: $750.0
----------------------------

----- Parking Lot Bill -----
Bill Number: 4900
Date: Mon Aug 26 18:31:42 GMT 2024
Vehicle Number: 9916
Vehicle Type: TRUCK
Parking Spot ID: 1003
Duration: 208 minutes
Total Amount: $1680.0
----------------------------
