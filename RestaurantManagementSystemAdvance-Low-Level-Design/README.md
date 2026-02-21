# 🍽 Restaurant Management System - Low Level Design (LLD)

## 📌 Overview

This project demonstrates a **Low Level Design (LLD)** of a Restaurant Management System built using Object-Oriented Design principles and common design patterns.

The system supports:

- Staff Management (Waiters, Chefs)
- Table Management
- Menu Management
- Order Management
- Kitchen Workflow
- Billing with Dynamic Charges

---

# 1️⃣ Functional Requirements

## 1. Staff Management
- Support roles: Waiter, Chef
- Allocate staff based on availability

## 2. Table Management
- Track table status: `AVAILABLE`, `OCCUPIED`
- Allocate tables using strategy

## 3. Menu Management
- Maintain food items and pricing

## 4. Order Management
- Waiter creates order for a table
- Track status of each individual order item

## 5. Kitchen Workflow
- Chef prepares order items
- Notify waiter when item is ready

## 6. Billing
- Generate bill for order
- Dynamically apply taxes and service charges

---

# 2️⃣ Core Entities & Enums

---

## 👤 Staff

```java
enum StaffRole {
    WAITER,
    CHEF
}
```

```java
abstract class Employee {
    String employeeId;
    String name;
    StaffRole role;
    boolean isAvailable;
}
```

```java
class Waiter extends Employee {
    List<String> assignedTableIds;
}
```

```java
class Chef extends Employee {
    List<String> specialties;
}
```

---

## 🪑 Table

```java
enum TableStatus {
    AVAILABLE,
    OCCUPIED
}
```

```java
class Table {
    String tableId;
    int capacity;
    String type; // VIP, Regular
    TableStatus status;
}
```

---

## 🍲 Menu

```java
class FoodItem {
    String name;
    String description;
    String category;
}
```

```java
class MenuItem {
    FoodItem foodItem;
    double price;
    double gstPercentage;
}
```

---

## 🧾 Order

```java
enum OrderStatus {
    CREATED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
```

```java
enum OrderItemStatus {
    PLACED,
    PREPARING,
    READY,
    SERVED
}
```

```java
class OrderItem {
    MenuItem menuItem;
    int quantity;
    OrderItemStatus status;
}
```

```java
class Order {
    String orderId;
    String tableId;
    String waiterId;
    List<OrderItem> items;
    LocalDateTime createdAt;
    OrderStatus status;
}
```

---

## 💵 Billing

```java
enum BillStatus {
    GENERATED,
    PAID,
    CANCELLED
}
```

```java
class Bill {
    String billId;
    String orderId;
    double totalAmount;
    double finalAmount;
    LocalDateTime billDate;
    BillStatus status;
}
```

---

# 3️⃣ Services

---

## 👥 Staff Service

```java
interface StaffAllocationStrategy {
    Employee allocate(List<Employee> employees);
}
```

```java
class AvailabilityBasedStaffAllocation implements StaffAllocationStrategy {
    public Employee allocate(List<Employee> employees) {
        // return first available employee
        return null;
    }
}
```

```java
class StaffService {
    Map<StaffRole, List<Employee>> staffMap;
    StaffAllocationStrategy allocationStrategy;

    Employee allocateStaff(StaffRole role) {
        return allocationStrategy.allocate(staffMap.get(role));
    }
}
```

---

## 🪑 Table Service

```java
interface TableAllocationStrategy {
    Table allocate(List<Table> tables, int requiredCapacity);
}
```

```java
class AvailabilityTableAllocation implements TableAllocationStrategy {
    public Table allocate(List<Table> tables, int requiredCapacity) {
        return null;
    }
}
```

```java
class TableService {
    List<Table> tables;
    TableAllocationStrategy allocationStrategy;

    Table allocateTable(int capacity) {
        return allocationStrategy.allocate(tables, capacity);
    }

    void updateTableStatus(String tableId, TableStatus status) {}
}
```

---

## 🧾 Order Service

```java
class OrderService {

    Map<String, Order> orders;

    Order createOrder(String tableId, String waiterId) {
        return null;
    }

    void addItem(String orderId, MenuItem menuItem, int quantity) {}

    void updateOrderItemStatus(String orderId, String menuItemName, OrderItemStatus status) {}

    void completeOrder(String orderId) {}
}
```

---

## 👨‍🍳 Kitchen Service

```java
interface ChefAllocationStrategy {
    Chef allocate(List<Chef> chefs, OrderItem item);
}
```

```java
class AvailabilityChefAllocation implements ChefAllocationStrategy {
    public Chef allocate(List<Chef> chefs, OrderItem item) {
        return null;
    }
}
```

```java
class KitchenService {

    ChefAllocationStrategy allocationStrategy;
    NotificationService notificationService;

    void processOrderItem(Order order, OrderItem item) {}

    void markItemReady(Order order, OrderItem item) {}
}
```

---

## 🔔 Notification Service

```java
class NotificationService {

    void notifyWaiter(String waiterId, String message) {}
}
```

---

## 💵 Billing (Strategy Pattern)

```java
interface FeeCharge {
    boolean isApplicable(Order order);
    double applyCharge(double currentAmount);
}
```

### Example Charges

```java
class GSTCharge implements FeeCharge {
    public boolean isApplicable(Order order) { return true; }
    public double applyCharge(double amount) { return amount * 0.05; }
}
```

```java
class ServiceCharge implements FeeCharge {
    public boolean isApplicable(Order order) { return true; }
    public double applyCharge(double amount) { return amount * 0.10; }
}
```

---

## Bill Calculator

```java
class BillCalculatorService {

    List<FeeCharge> feeCharges;

    double calculate(Order order) {

        double total = 0;

        for (OrderItem item : order.getItems()) {
            total += item.getMenuItem().getPrice() * item.getQuantity();
        }

        double finalAmount = total;

        for (FeeCharge charge : feeCharges) {
            if (charge.isApplicable(order)) {
                finalAmount += charge.applyCharge(finalAmount);
            }
        }

        return finalAmount;
    }
}
```

---

## Bill Service

```java
class BillService {

    BillCalculatorService calculatorService;

    Bill generateBill(Order order) {
        return null;
    }
}
```

---

# 4️⃣ Relationships Summary

- Waiter → Creates Order
- Order → Belongs to Table
- Order → Contains List<OrderItem>
- OrderItem → Maps to MenuItem
- KitchenService → Updates OrderItem Status
- KitchenService → Notifies Waiter
- Bill → Generated from Order
- BillCalculatorService → Applies multiple FeeCharge strategies

---

# 5️⃣ Design Patterns Used

- ✅ Strategy Pattern (Allocation, FeeCharge)
- ✅ Open/Closed Principle (Billing extensibility)
- ✅ SRP (Separation of concerns)
- ✅ Facade (Optional RestaurantManager wrapper)

---

# 🎯 Design Goals Achieved

- Clean separation of concerns
- Extensible billing system
- Item-level order tracking
- Flexible allocation strategies
- Interview-ready LLD structure
