# Amazon Locker System - Low Level Design

## Problem Statement

Design an **Amazon Locker System** that allows customers to select a locker delivery option during checkout. The system orchestrates several core capabilities:

* **Allocation:** Assign an appropriate locker based on package dimensions and size constraints.
* **Payment:** Process transactions seamlessly during checkout.
* **Reservation:** Secure a locker slot temporarily or permanently for an order.
* **Logistics:** Assign delivery personnel to transport packages to designated locker hubs.
* **Security:** Generate secure, time-bound pickup OTPs.
* **Notifications:** Keep users informed at critical milestones (delivery confirmation, OTP generation).

---

## Design Patterns Used

### Strategy Pattern (Locker Allocation)
Used to decouple the locker allocation logic from the core service. This allows different allocation algorithms to be plugged in dynamically without altering `LockerService`.
* `LockerAllocationStrategy` (Interface)
* `LockerAllocationBySize`
* `LockerAllocationByLevel`

### Strategy Pattern (Payment Processing)
Used to support multiple payment channels uniformly. New payment methods can be added by implementing a common interface.
* `PaymentService` (Interface)
* `UPIPayment`
* `CardPayment`

### Observer Pattern
Used to dispatch alerts across multiple notification channels simultaneously or selectively.
* `Notifier` (Subject/Interface)
* `EmailNotifier`
* `SMSNotifier`
* `PushNotifier`

### Dependency Injection
Ensures that services receive their operational dependencies via constructors, promoting loose coupling, easier unit testing, and high extensibility.

---

## Class Responsibilities

### Core Actors & Items
* **User:** Represents the customer interacting with the Amazon Locker system.
* **PackageItem:** Holds metadata required for locker placement, mapping physical properties to space requirements.
* **Dimension:** Encapsulates physical measurements (Length, Width, Height).
* **Location:** Stores geographical coordinates and address details of a physical locker hub.
* **DeliveryPerson:** Represents the logistics partner responsible for dropping off packages.

### Locker Domain Models
* **Locker:** Represents an individual slot. Tracks its current state (Empty, Reserved, Occupied), physical size, and its active `PickupCode`.
* **LockerHub:** Represents the physical facility housing a collection of individual lockers; manages real-time slot inventory.
* **PickupCode:** Represents a secure, time-sensitive OTP required by the user to open a locker.

### Core Logistics & Business Logic
* **LockerService:** The central coordinator for managing locker hubs, calculating proximity, reserving slots, and finalizing bookings.
* **DeliveryService:** Handles driver assignments, status updates during transport, package drop-offs, and triggering OTP generation upon physical locker closure.

### Checkout & Billing
* **Item:** Represents a product in the catalog containing base pricing and default packaging constraints.
* **Bill:** Computes line items, taxes, delivery fees, and final totals.
* **Order:** Maintains the lifecycle of a purchase, linking the user, items, financial state, and assigned locker details.
* **CheckoutService:** The orchestrator that sequences order generation, payment authorization, locker blocking, and success notifications.

---

## High-Level Execution Flow

```text
[User Checkout] ──> [Create Order] ──> [Process Payment]
                                               │
[Notify User] <── [Reserve Locker] <───────────┘
      │
[Assign Delivery Driver] ──> [Package Deposited] ──> [OTP Generated & Sent]
                                                            │
[User Opens Locker] <── [Verify OTP at Hub] <───────────────┘
```

## Execution Steps Detailed

* **Checkout Initiation:** The user aggregates items and opts for locker delivery.
* **Order & Payment:** `CheckoutService` builds the order details and fires the chosen `PaymentService` strategy.
* **Locker Reservation:** Upon successful payment, `LockerService` queries its allocation strategies to lock down an optimal slot at the target `LockerHub`.
* **Logistics Handshake:** `DeliveryService` dispatches a `DeliveryPerson` with the physical package.
* **Drop-off & Security:** The delivery driver deposits the package into the assigned locker. The system updates the `Locker` status, generates a secure `PickupCode`, and broadcasts it via the `NotificationService`.
* **Fulfillment:** The user arrives at the hub, submits their OTP, and retrieves their package before the code expires.