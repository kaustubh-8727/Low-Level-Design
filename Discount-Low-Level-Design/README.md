# Discount Management System – Low Level Design (LLD)

This project demonstrates a **scalable and extensible Discount Management System** designed using **Low Level Design (LLD) principles**.  
It models real-world e-commerce discount scenarios such as **cart-based discounts, coupon discounts, and percentage/flat offers**.

The design focuses on:
- Separation of concerns
- Open–Closed Principle
- Easy extensibility
- Interview-ready clarity

---

## 📌 Problem Statement

Design a discount system for an e-commerce cart where:
- Multiple discounts can exist
- Each discount has eligibility rules
- Discounts can be applied in a controlled order
- New discounts can be added without modifying existing code

---

## 🧠 Design Overview

The discount system is divided into **five core components**:

1. DiscountContext
2. DiscountRule
3. DiscountAction
4. Discount
5. DiscountEngine


Each component has a **single responsibility**.

---

## 🧠 Design Overview

The discount system is divided into **five core components**, each following the **Single Responsibility Principle**:

- **DiscountContext**
- **DiscountRule**
- **DiscountAction**
- **Discount**
- **DiscountEngine**

Each component is independent, modular, and easily extensible.

---

## 🧩 Core Components Explanation

### 1️⃣ DiscountContext
Acts as a **data carrier** during discount evaluation.

It contains:
- User
- Cart
- Cart total
- Coupon code
- Order time

This avoids tight coupling between discount logic and business entities and enables future extensions such as:
- New user discounts
- Time-based discounts
- Loyalty or membership-based discounts

---

### 2️⃣ DiscountRule (Eligibility Logic)
Determines **whether a discount is applicable**.

```java
interface DiscountRule {
    boolean isApplicable(DiscountContext context);
}
```

### 2️⃣ DiscountRule (Eligibility Logic)

Determines **whether a discount is applicable**.

```java
interface DiscountRule {
    boolean isApplicable(DiscountContext context);
}
```

### 3️⃣ DiscountAction (Discount Calculation)

Defines **how the discount affects the payable amount**.

```java
interface DiscountAction {
    double apply(double amount, DiscountContext context);
}
```

### 4️⃣ Discount

Represents a complete discount configuration by combining:

- A rule (when to apply)
- An action (how to apply)
- Priority (order of application)
- Discount type (`ITEM`, `CART`, `ORDER`)

This separation allows complex discount behavior to be built **without code duplication or tight coupling**.

---

### 5️⃣ DiscountEngine

The **orchestrator** of the discount system.

**Responsibilities:**
- Filters applicable discounts using rules
- Sorts discounts based on priority
- Applies discounts sequentially

This component acts as the **execution engine** for all discount logic.

---

## 🔁 Discount Flow (Execution Script)

1. User adds products to the cart  
2. Cart total is calculated  
3. `DiscountContext` is created  
4. `DiscountEngine` evaluates all discounts  
5. Eligible discounts are applied in priority order  
6. Final payable amount is returned  

---

## 🧪 Sample Scenario Demonstrated

**Cart Total:** ₹64,000  

**Discounts Applied:**
- 10% festival discount (minimum cart value rule)
- ₹500 coupon discount  

**Final Payable Amount:**
```₹57,100```


---

## 🏗️ Design Patterns Used

| Area | Pattern |
|------|--------|
| Discount calculation | Strategy |
| Eligibility rules | Specification |
| Discount flow | Chain of Responsibility |
| Overall orchestration | Engine / Policy Pattern |
