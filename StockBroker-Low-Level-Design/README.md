# Stock Brokerage System (Low Level Design)

## Problem Statement
Design a simplified stock brokerage system that allows users to place buy/sell orders, matches orders using an order book, executes trades, updates wallet and portfolio, and notifies users upon successful transactions.

---

## Features
- Place Buy and Sell Orders
- Order Matching using Max Heap (Buy) & Min Heap (Sell)
- Trade Execution Engine
- Wallet with Balance Reservation
- Portfolio Management with Average Price Calculation
- Event-driven Notification System
- Support for Partial Order Execution

---

## System Design Overview
User → Order → Matching Engine → Trade → Wallet + Portfolio Update → Notification


---

## Class Overview

### User
Represents a system user with basic identity details.

### Wallet
Handles user balance with support for available and reserved funds.

### Portfolio
Maintains user stock holdings, quantity, and average buy price.

### Holding
Represents individual stock holdings inside a portfolio.

### Order
Represents a buy/sell request with price, quantity, type, and status.

### OrderType (ENUM)
Defines whether an order is BUY or SELL.

### OrderStatus (ENUM)
Tracks lifecycle of order (PENDING, PARTIAL, FILLED).

### OrderBook
Maintains buy and sell orders using:
- Max Heap → Buy Orders
- Min Heap → Sell Orders

### MatchingEngine
Core component responsible for matching buy and sell orders and generating trades.

### Trade
Represents an executed transaction between buyer and seller.

### TradeService
Handles trade execution, wallet settlement, portfolio updates, and event publishing.

### TradeListener (Interface)
Defines contract for listening to trade events.

### NotificationService
Listens to trade events and notifies users about executed trades.

### UserAccount
Aggregates user, wallet, and portfolio into a single unit.

---

## Design Patterns Used

### 1. Observer Pattern
- Used for notification system
- `TradeService` publishes events
- `NotificationService` listens and reacts

### 2. Strategy Pattern (Implicit)
- Buy and Sell behaviors handled via `OrderType`

### 3. Singleton Pattern (Optional Extension)
- Can be applied to services like MatchingEngine / OrderBook

### 4. Separation of Concerns
- Matching, trade execution, wallet, and notification are decoupled

---

## ⚡ Key Concepts

### Buy Orders → Max Heap
Highest price buyer gets priority.

### Sell Orders → Min Heap
Lowest price seller gets priority.

### Matching Condition
Buy Price >= Sell Price


### Wallet Handling
- Funds are **reserved** during order placement
- Settled after trade execution

### Portfolio Logic
- Maintains average price
- Supports partial updates

---

## Order Execution Flow

1. User places an order
2. Wallet balance is validated and reserved
3. Order is added to OrderBook
4. MatchingEngine attempts to match orders
5. Trade is created
6. TradeService:
   - Updates Wallet
   - Updates Portfolio
   - Publishes event
7. NotificationService notifies users

---

## Sample Scenario

- User A places BUY order for TCS @ ₹120
- User B places SELL order for TCS @ ₹110
- MatchingEngine matches orders
- Trade executes at ₹110
- Wallet & Portfolio updated
- Notification sent to both users

---

## Tech Stack
- Java
- Core Data Structures (PriorityQueue, HashMap)

---
