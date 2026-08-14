# Library Management System - LLD

## Problem Statement

Design and implement a **Library Management System** that allows a library to manage books, users, book borrowing, returning, reservations, and fines.

### Core Requirements

The system should support:

- Add books and physical copies of books.
- Register different types of users.
- Search and identify books.
- Borrow an available book copy.
- Return a borrowed book.
- Track loan and due dates.
- Calculate fines for overdue books.
- Reserve a book when all its copies are unavailable.
- Notify users when a reserved book becomes available.
- Support different borrowing limits for different user types.

---

# Design Overview

The design separates the system into:

1. **Domain entities** - represent the core library objects.
2. **Repositories** - handle data storage and retrieval.
3. **Services** - contain business logic and orchestrate operations.
4. **Policies / Strategies** - encapsulate business rules that can change independently.

---

## Main Classes

| Class | Responsibility |
| :--- | :--- |
| **Book** | Represents logical book information |
| **BookCopy** | Represents a physical copy and its availability |
| **User** | Base class for library users |
| **Student** | Represents a student user |
| **Faculty** | Represents a faculty user |
| **Librarian** | Represents a librarian |
| **Loan** | Represents a book borrowing transaction |
| **Reservation** | Represents a reservation for an unavailable book |
| **Fine** | Represents an overdue fine |
| **LibraryService** | Orchestrates core library operations |
| **BorrowingPolicy** | Defines borrowing eligibility rules |
| **FineCalculator** | Calculates overdue fines |
| **NotificationService** | Handles user notifications |
| **UserRepository** | Stores and retrieves users |
| **BookRepository** | Stores books and finds available copies |
| **LoanRepository** | Stores and retrieves loans |
| **ReservationRepository** | Stores and retrieves reservations |

## Design Patterns Used

### 1. Strategy Pattern
Used for business rules that can vary.

* **BorrowingPolicy** → handles different borrowing limits.
* **FineCalculator** → handles different fine calculation rules.

### 2. Repository Pattern
Used to separate business logic from data storage.

* **UserRepository**
* **BookRepository**
* **LoanRepository**
* **ReservationRepository**

*Note: The current implementation uses in-memory repositories, which can later be replaced with database implementations.*

### 3. Dependency Injection
`LibraryService` receives its dependencies through the constructor instead of creating them internally. This keeps the classes loosely coupled and makes testing easier.

