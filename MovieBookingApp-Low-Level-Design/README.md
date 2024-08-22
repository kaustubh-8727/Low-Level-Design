# Movie Booking Application

## Overview

The Movie Booking Application is a simple Java-based console application that allows users to book movie tickets at various theaters across different locations. The application supports features like selecting movies, choosing theaters, booking seats, and making payments. Upon successful payment, the application will display the booking details; otherwise, it will show an error message.

## Features

- **Movie Management**: Add movies, manage movie details like genre and duration.
- **Theater Management**: Add theaters, manage halls and shows.
- **Seat Booking**: Select seats for a specific show and validate seat availability.
- **Payment Processing**: Make payments through different payment modes.
- **Booking Confirmation**: Display booking details upon successful payment.

## Usage

1. **Starting the Application**: 
   - Upon running the application, the user is presented with a list of available movies in different locations.
  
2. **Movie and Show Selection**:
   - The user selects a movie and then chooses a show at a specific theater.

3. **Seat Selection**:
   - The user picks the seats they want to book. The application checks seat availability in real-time.

4. **Payment**:
   - After selecting the seats, the user proceeds to payment. The application supports multiple payment methods, including UPI and card payments.

5. **Booking Confirmation**:
   - If the payment is successful, the application displays the booking details, including the movie name, theater, show time, seats booked, and total price.
   - If the payment fails, an error message is displayed, and the user can retry the booking process.

## Code Structure

- **`Main.java`**: The entry point of the application, handling the initial setup, movie selection, booking, and payment processes.
- **`Movie.java`**: Represents a movie with attributes like name, genre, and duration.
- **`MovieManager.java`**: Manages the list of movies and their locations.
- **`MovieTheater.java`**: Represents a movie theater with attributes like name, location, halls, and shows.
- **`TheaterManager.java`**: Manages the list of theaters and their locations.
- **`Hall.java`**: Represents a hall in a theater, managing seats.
- **`Show.java`**: Represents a movie show with attributes like movie, hall, booked seats, and start time.
- **`Seat.java`**: Represents a seat with a seat number and type (Premium, Middle, Lower).
- **`Location.java`**: Represents a location with attributes like country, state, and city.
- **`Booking.java`**: Handles the process of booking seats for a show.
- **`Payment.java`**: Handles the payment process.
- **`PaymentMode.java`**: Interface for different payment modes.
- **`UpiPayment.java`**: Implements the payment process using UPI.
- **`CardPayment.java`**: Implements the payment process using card payments.

## Sample Output

```
welcome to movie booking low level design

Payment Successful!
Movie: superman
Theater: pvr movies
Show Time: 150
Seats Booked: 
Seat Number: 2 (Type: PREMIUM)
Seat Number: 1 (Type: PREMIUM)
Total Price: $600.0
