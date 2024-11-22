# Stock Brokerage System

This **Stock Brokerage System** simulates a basic stock trading platform, enabling users to manage their accounts, portfolios, and orders effectively. Below is an overview of the system's architecture, objects, and key functionalities.

---

## 📚 **System Components**

### 1. **User**
- Represents an individual user with details like name, email, and contact number.

### 2. **UserAccount**
- Associates a user with a financial account and portfolio.
- Manages account balance operations (credit, debit).

### 3. **Portfolio**
- Tracks stocks owned by a user, including:
  - Stocks currently held
  - Order history (buy/sell)
  - Current profit, loss, and total investment.

### 4. **UserStock**
- Represents a specific stock owned by a user.
- Tracks stock quantity and purchase price.

### 5. **Stock**
- Represents a specific stock available for trading.
- Attributes include stock ID, name, and current value.

### 6. **StockManagement**
- Handles all stocks in the system:
  - Adding and removing stocks
  - Updating stock prices
  - Filtering stocks based on criteria.

### 7. **Order**
- Abstract base class for all orders.
- Subclasses:
  - **BuyOrder**: Represents a stock purchase.
  - **SellOrder**: Represents a stock sale.
- Validates and processes orders, updating the user’s account and portfolio.

### 8. **OrderManagement**
- Manages the lifecycle of orders:
  - Queues orders for processing.
  - Executes orders in sequence.

### 9. **StockBrokerService**
- Acts as the central service layer:
  - Handles user and stock management.
  - Processes buy/sell operations.
  - Provides portfolio insights.

---

## 🎯 **Features**

1. **User Management**  
   - Add or remove users.
   - Manage user accounts and associated portfolios.

2. **Portfolio Management**  
   - Maintain a record of owned stocks.
   - Calculate current profit, loss, and total investment.

3. **Stock Management**  
   - Add, remove, or update stock details.
   - Filter stocks based on various criteria.

4. **Order Execution**  
   - Place buy/sell orders.
   - Validate and process orders through a queue system.

5. **Performance Tracking**  
   - Display detailed portfolio reports.
   - Track stock performance over time.

---

## 🧩 **Code Structure**

### **Core Objects**
| Object              | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| `User`             | Represents user details.                                                   |
| `UserAccount`      | Manages a user’s balance and portfolio.                                     |
| `Portfolio`        | Tracks stocks held, order history, profit, and loss.                       |
| `Stock`            | Stores stock information (ID, name, value).                                |
| `Order`            | Abstract class for buy and sell orders.                                    |
| `BuyOrder`         | Processes stock purchase orders.                                           |
| `SellOrder`        | Processes stock sale orders.                                               |

### **Manager Classes**
| Manager Class       | Responsibilities                                                          |
|---------------------|-----------------------------------------------------------------------------|
| `UserManagement`    | Manages user accounts.                                                    |
| `StockManagement`   | Manages stock details and updates.                                        |
| `OrderManagement`   | Processes and executes orders in a queue.                                 |

---

## Example Output

```plaintext
Portfolio for user: jack
Balance: 19400.0
Total Amount Invested: 600.0
Current Profit: 0.0
Current Loss: 0.0

Stocks Holding:
Stock Name: TATA
Quantity: 5.0
Price Bought: 120.0
Current Price: 120.0
-----------------------------


Portfolio for user: jack
Balance: 19400.0
Total Amount Invested: 600.0
Current Profit: 100.0
Current Loss: 0.0

Stocks Holding:
Stock Name: TATA
Quantity: 5.0
Price Bought: 120.0
Current Price: 140.0
-----------------------------


Portfolio for user: jack
Balance: 18000.0
Total Amount Invested: 1400.0
Current Profit: 0.0
Current Loss: 0.0

Stocks Holding:
Stock Name: JIO
Quantity: 10.0
Price Bought: 140.0
Current Price: 140.0
-----------------------------


Portfolio for user: jack
Balance: 18000.0
Total Amount Invested: 1400.0
Current Profit: 0.0
Current Loss: 1350.0

Stocks Holding:
Stock Name: JIO
Quantity: 10.0
Price Bought: 140.0
Current Price: 5.0
-----------------------------
