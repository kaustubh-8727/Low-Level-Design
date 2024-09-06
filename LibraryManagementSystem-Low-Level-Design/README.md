# Library Management System - Low-Level Design

## Overview

This project represents a low-level design for a Library Management System implemented in Java. The system includes core functionalities for managing books, members, reservations, and fines. It supports operations like book issuance, return, and member management.

## Classes and Responsibilities

### Enums

- **Role**: Defines user roles.
  - `ADMIN`
  - `MEMBER`

- **Status**: Defines the status of library cards.
  - `ACTIVE`
  - `NON_ACTIVE`

- **ReservationStatus**: Defines the status of a reservation.
  - `RESERVED`
  - `RETURNED`

- **Category**: Categorizes books (e.g., `FICTION`, `SCIENCE`).

- **BookStatus**: Defines the status of a book.
  - `AVAILABLE`
  - `ISSUED`

### User

- **Attributes**: `name`, `userId`, `address`, `role`
- **Methods**: Getters for user details

### Librarian

- **Inherits from**: `User`
- **Role**: Represents a librarian with administrative privileges.

### Member

- **Attributes**: `libraryCard`, `fines`
- **Methods**: Handles library card management, reservation history, and fines.

### LibraryCard

- **Attributes**: `issueDate`, `expiryDate`, `cardStatus`, `cardId`, `reservedHistoryList`, `activeReservedList`
- **Methods**: Manages card status, reservations, and expiration.

### Book

- **Attributes**: `ISBN`, `author`, `publisher`, `title`, `bookCategory`, `bookStatus`
- **Methods**: Updates book status and retrieves book details.

### BookService

- **Attributes**: `allBooksList`
- **Methods**: Adds, removes, and searches for books.

### SearchBookService (Abstract)

- **Methods**: Defines abstract methods for searching books by different criteria.

#### SearchBookByAuthor, SearchBookByTitle, SearchBookByISBN, SearchBookByCategory

- **Inherits from**: `SearchBookService`
- **Methods**: Implements specific search criteria.

### Library

- **Attributes**: `libraryName`, `address`, `description`, `bookService`, `librarian`
- **Methods**: Sets and retrieves book service and librarian.

### LibraryManager

- **Attributes**: `library`, `membersList`, `reservationsList`, `MAX_BOOK_ALLOTMENT`
- **Methods**: Manages member registration, book issuance and return, and validates book allotment.

### Reservation

- **Attributes**: `reservationId`, `book`, `member`, `reservedDate`, `expiryDate`, `reservationStatus`, `fine`
- **Methods**: Creates and ends reservations, calculates fines, and prints reservation details.

### Fine

- **Attributes**: `amount`, `fineDate`, `isPaid`
- **Methods**: Handles fine details and payment status.

### Location

- **Attributes**: `country`, `state`, `city`, `pincode`
- **Methods**: Constructor for setting location details.

## Usage

1. **Create Books**: Instantiate `Book` objects with details like author, publisher, and title.
2. **Manage Books**: Use `BookService` to add or remove books and search by different criteria.
3. **Manage Members**: Register members, issue library cards, and manage fines using `LibraryManager`.
4. **Handle Reservations**: Issue and return books with reservations, including fine calculations for late returns.
