# Membership System - Low Level Design

## Overview

This project implements a **generic and extensible Membership / Subscription System**.

The system supports:

- Multiple services (e.g., Streaming Platform, Gym, Music App)
- Membership tiers (Basic, Premium, Premium Plus)
- Subscription lifecycle management (Buy, Renew, Cancel, Expire)
- Multiple payment methods
- Flexible pricing strategies
- Notification mechanisms

The design follows SOLID principles and applies appropriate design patterns to ensure scalability and maintainability.

---

# System Architecture

The system is divided into the following logical layers:

1. Core Domain Layer
2. Payment Layer
3. Pricing Layer
4. Notification Layer
5. Service Orchestration Layer

---

# Core Domain Classes

## 1. User

**Description:**  
Represents a customer of the system.

**Responsibilities:**
- Stores user details (id, name, email, phone)
- Owns subscriptions

---

## 2. Service

**Description:**  
Represents a platform offering membership plans (e.g., Streaming App, Gym).

**Responsibilities:**
- Maintains list of available MembershipPlans

---

## 3. MembershipPlan

**Description:**  
Defines the blueprint of a subscription plan.

**Attributes:**
- Tier (BASIC / PREMIUM / PREMIUM_PLUS)
- Price
- Validity (`Period`)
- Benefits

---

## 4. Subscription

**Description:**  
Represents an instance of a user's subscription.

**Responsibilities:**
- Links User and MembershipPlan
- Maintains start date and expiry date
- Tracks subscription status
- Handles lifecycle operations (activate, cancel, expiry check)

---

# Payment Layer

## 5. PaymentMethod (Interface)

**Purpose:**  
Abstracts payment processing.

**Method:**
```
pay(double amount)
```


---

## 6. CardPayment / UpiPayment

**Description:**  
Concrete implementations of PaymentMethod.

**Responsibility:**
- Process payment
- Return PaymentResult

---

## 7. PaymentResult

**Description:**  
Encapsulates payment outcome (SUCCESS, FAILED, PENDING).

---

# Pricing Layer

## 8. PricingStrategy (Interface)

**Purpose:**  
Defines pricing logic.

**Method:**
```
calculatePrice(User user, MembershipPlan plan)
```


---

## 9. DefaultPricingStrategy

Returns base plan price.

---

## 10. FestivalDiscountStrategy

Applies discount logic (e.g., 10% discount).

---

# Notification Layer

## 11. NotificationService (Interface)

**Purpose:**  
Abstracts notification mechanism.

**Method:**
```
send(User user, String message)
```


---

## 12. EmailNotificationService

Sends notifications via email.

---

## 13. SmsNotificationService

Sends notifications via SMS.

---

# Business Orchestration Layer

## 14. SubscriptionService

**Description:**  
Core service that coordinates the entire subscription flow.

**Responsibilities:**
- buySubscription()
- renewSubscription()
- cancelSubscription()
- Coordinate pricing, payment, and notifications

---

# Subscription Lifecycle Flow

1. User selects a MembershipPlan.
2. PricingStrategy calculates final amount.
3. PaymentMethod processes payment.
4. If payment succeeds:
   - Subscription becomes ACTIVE.
   - Notification is sent.
5. If payment fails:
   - Subscription remains in PENDING_PAYMENT state.

---

# Design Patterns Used

## 1. Strategy Pattern

Used in:
- PricingStrategy
- PaymentMethod

**Benefit:**
- Enables dynamic behavior selection.
- Follows Open/Closed Principle.

---

## 2. Dependency Injection (Constructor-Based)

Used in:
- SubscriptionService

**Benefit:**
- Promotes loose coupling.
- Improves testability.

---

## 3. (Optional Extension) Factory Pattern

Can be introduced for:
- PaymentMethod creation
- NotificationService creation

---

# SOLID Principles Applied

- **Single Responsibility Principle**  
  Each class has a single, well-defined responsibility.

- **Open/Closed Principle**  
  New payment methods or pricing strategies can be added without modifying existing logic.

- **Dependency Inversion Principle**  
  High-level modules depend on abstractions (interfaces).

---

# Extensibility

The system can be extended to support:

- Auto-renewal via scheduler
- Proration logic during upgrades
- State pattern for subscription lifecycle
- Audit logging
- Database persistence layer
- Event-driven notifications

---

# Conclusion

This project demonstrates:

- Clean separation of concerns
- Proper abstraction
- Usage of behavioral design patterns
- Scalable and extensible architecture

It is suitable for Low-Level Design interviews and can be extended into a production-grade system.
