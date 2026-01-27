# Auction Management System (Low Level Design)

A Java-based in-memory Auction Management System that supports creating auctions, joining auctions, placing concurrent bids, tracking bid history, and notifying users in real-time. This project demonstrates object-oriented design, concurrency handling, and commonly used design patterns.

---

## Features

- Owner can create products and auctions  
- Users can join auctions  
- Owner can start and close auctions  
- Users can place bids concurrently  
- Highest bid & full bid history tracking  
- Thread-safe bid placement  
- Text/Email notification support  

---

## Core Entities

- User  
- Product  
- Auction  
- Bid  

---

## High Level Flow

1. Owner creates product  
2. Owner creates auction for product  
3. Users join auction  
4. Owner starts auction  
5. Users place bids concurrently  
6. System validates and stores bids  
7. Users are notified for every new bid  
8. Owner closes auction  
9. Highest bidder is declared winner  

---

## Class Responsibilities (One-liners)

### 1. `User`
Represents a system user who can own auctions and participate as a bidder.

### 2. `Product`
Represents an item that will be auctioned.

### 3. `Auction`
Represents an auction with lifecycle states (CREATED, RUNNING, CLOSED).

### 4. `Bid`
Represents a single bid placed by a user.

### 5. `AuctionService`
Manages auction creation, updates, start/close, and user participation.

### 6. `BidService`
Validates and processes bids in a thread-safe manner.

### 7. `AuctionStore` (Interface)
Abstraction for storing bids and highest bid per auction.

### 8. `InMemoryAuctionStore`
In-memory implementation of `AuctionStore`.

### 9. `AuctionLockManager`
Maintains per-auction locks to ensure concurrency safety.

### 10. `NotificationService` (Interface)
Abstraction for notifying users about new bids.

### 11. `TextNotificationService`
Sends bid notifications via console text messages.

### 12. `EmailNotificationService`
Placeholder for email-based notifications.

### 13. `AuctionModel`
DTO used for updating auction fields.

### 14. `AuctionManagementSystem`
Driver class containing main method and demo flow.

---

## Design Patterns Used

### 1. Singleton Pattern
Used for services that should have only one instance:
- AuctionService  
- BidService  
- AuctionLockManager  

Ensures global access and consistent state.

---

### 2. Strategy Pattern
Notification mechanism uses strategy pattern:

- NotificationService (interface)  
- TextNotificationService  
- EmailNotificationService  

Allows switching notification types without changing business logic.

---

### 3. Repository Pattern
AuctionStore acts as repository abstraction for bid storage.

---

### 4. Factory via Constructor
Objects like Auction, Product, Bid are created using constructors encapsulating initialization logic.

---

### 5. Command-like Service Layer
Services (AuctionService, BidService) encapsulate business operations as commands.

---

### 6. Concurrency Control (Lock per Resource)
Each auction has its own lock using `ReentrantLock` to avoid race conditions while bidding.

---

## Thread Safety

- ConcurrentHashMap used for shared maps  
- ReentrantLock per auction  
- Only one bid is processed at a time per auction  

---

## Sample Output
```
Auction Created: b6c484d1-5615-4724-a105-1cd5cc2d48da
Auction Started
[TEXT] Notification sent to User: e532f307-d353-4485-8e4c-e8cebc924025 | New Bid Placed: ₹54000 by User: d810b982-73f1-4eb4-aebc-96e6fde759ea at 2026-01-27T22:02:11.902650
[TEXT] Notification sent to User: d810b982-73f1-4eb4-aebc-96e6fde759ea | New Bid Placed: ₹54000 by User: d810b982-73f1-4eb4-aebc-96e6fde759ea at 2026-01-27T22:02:11.902650
[TEXT] Notification sent to User: 55bf98d8-9ca6-4dcf-8f7b-22e65c8f8010 | New Bid Placed: ₹54000 by User: d810b982-73f1-4eb4-aebc-96e6fde759ea at 2026-01-27T22:02:11.902650
[TEXT] Notification sent to User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e | New Bid Placed: ₹54000 by User: d810b982-73f1-4eb4-aebc-96e6fde759ea at 2026-01-27T22:02:11.902650
wrong bid by user e532f307-d353-4485-8e4c-e8cebc924025 bid must be higher than current highest bid
[TEXT] Notification sent to User: e532f307-d353-4485-8e4c-e8cebc924025 | New Bid Placed: ₹56000 by User: 55bf98d8-9ca6-4dcf-8f7b-22e65c8f8010 at 2026-01-27T22:02:11.902723
[TEXT] Notification sent to User: d810b982-73f1-4eb4-aebc-96e6fde759ea | New Bid Placed: ₹56000 by User: 55bf98d8-9ca6-4dcf-8f7b-22e65c8f8010 at 2026-01-27T22:02:11.902723
[TEXT] Notification sent to User: 55bf98d8-9ca6-4dcf-8f7b-22e65c8f8010 | New Bid Placed: ₹56000 by User: 55bf98d8-9ca6-4dcf-8f7b-22e65c8f8010 at 2026-01-27T22:02:11.902723
[TEXT] Notification sent to User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e | New Bid Placed: ₹56000 by User: 55bf98d8-9ca6-4dcf-8f7b-22e65c8f8010 at 2026-01-27T22:02:11.902723
[TEXT] Notification sent to User: e532f307-d353-4485-8e4c-e8cebc924025 | New Bid Placed: ₹58000 by User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e at 2026-01-27T22:02:11.902685
[TEXT] Notification sent to User: d810b982-73f1-4eb4-aebc-96e6fde759ea | New Bid Placed: ₹58000 by User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e at 2026-01-27T22:02:11.902685
[TEXT] Notification sent to User: 55bf98d8-9ca6-4dcf-8f7b-22e65c8f8010 | New Bid Placed: ₹58000 by User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e at 2026-01-27T22:02:11.902685
[TEXT] Notification sent to User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e | New Bid Placed: ₹58000 by User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e at 2026-01-27T22:02:11.902685
Auction Closed

Winner Details:
User: 04a5f033-fec3-4fdd-86bd-012bd39e1a4e | Amount: rs58000
```
