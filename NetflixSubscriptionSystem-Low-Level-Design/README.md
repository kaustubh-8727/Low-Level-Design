# Netflix Subscription System — Low-Level Design

## 1. Problem Statement

Design a subscription management system similar to Netflix that allows users to create accounts, explore subscription plans, view plan benefits, and purchase subscriptions.

The system should support dynamic plan and pricing updates, multiple payment methods, promotional offers, and notifications for successful payments and upcoming subscription expirations.

### Functional Requirements

1. Users should be able to create and manage their accounts.
2. Users should be able to view available subscription plans.
3. Users should be able to view the benefits associated with each plan.
4. Users should be able to purchase a subscription for a selected plan and tenure.
5. Users should receive notifications after successful payments and before their subscriptions expire.
6. Administrators should be able to dynamically add, update, and deactivate subscription plans and change their pricing.
7. The system should support dynamic pricing based on subscription tenure, discounts, and promotional offers.
8. The system should support multiple payment methods, such as UPI and credit/debit cards.

### Optional Requirements

- Support promotional offers such as festive discounts and coupon codes.
- Validate offer eligibility, validity periods, and discount rules.
- Support payment retries without creating duplicate charges.
- Support subscription expiration and renewal workflows.

## 2. Classes and Entities

### Core Entities

| Class / Entity | Description |
|---|---|
| `User` | Represents a registered user and stores details such as ID, name, and email. |
| `SubscriptionPlan` | Represents a subscription plan with attributes such as ID, name, base price, benefits, status, and version. |
| `Benefit` | Represents a feature included in a subscription plan, such as video quality, simultaneous screens, or downloads. |
| `Subscription` | Represents a user's purchased subscription, including plan ID, start date, end date, and subscription status. |
| `Invoice` | Stores billing details, including base amount, discounts, taxes, final amount, and currency. |
| `PaymentResult` | Represents a payment outcome, including transaction reference and payment status. |
| `Offer` | Stores promotional offer details, discount rules, validity periods, and eligibility conditions. |

### Plan Types

| Class | Description |
|---|---|
| `BasePlan` | Represents a basic subscription tier with limited benefits. |
| `PremiumPlan` | Represents a higher-tier subscription with additional benefits. |
| `GoldPlan` | Represents a premium subscription tier with its own configured benefits. |

**Note:** If plan types differ only in configuration, a single configurable `SubscriptionPlan` entity can replace these subclasses. This allows administrators to introduce new plans without changing the application code.

### Pricing Components

| Class / Interface | Description |
|---|---|
| `PricingStrategy` | Interface defining `calculatePrice()` for calculating subscription prices. |
| `ThreeMonthsPlan` | Implements pricing rules for a three-month subscription. |
| `SixMonthsPlan` | Implements pricing rules for a six-month subscription, potentially with a discount. |
| `TwelveMonthsPlan` | Implements pricing rules for a twelve-month subscription, potentially with a larger discount. |
| `PricingService` | Calculates the final subscription price by applying tenure pricing, eligible offers, and applicable taxes. |

### Services

| Class | Description |
|---|---|
| `UserManagementService` | Handles user creation, retrieval, updates, and deletion. |
| `SubscriptionService` | Orchestrates subscription purchases, activation, cancellation, and lifecycle management. |
| `AdminPlanService` | Allows administrators to create, update, deactivate, and reprice subscription plans. |
| `PaymentService` | Coordinates payment processing, payment status verification, and provider integration. |
| `NotificationService` | Publishes notification events for payment confirmations and subscription expirations. |
| `NotificationWorker` | Consumes queued notification events and delivers notifications asynchronously, with retries for failures. |
| `SubscriptionExpiryWorker` | Periodically identifies subscriptions approaching expiration and schedules reminder notifications. |

### Payment Components

| Class / Interface | Description |
|---|---|
| `Payment` | Interface defining `makePayment()` for processing a payment. |
| `UPIPayment` | Implements payment processing through a UPI payment provider. |
| `CardPayment` | Implements payment processing through a card payment provider. |

### Supporting Components

| Class / Entity | Description |
|---|---|
| `NotificationEvent` | Represents an event requesting a notification, such as payment success or subscription expiration. |
| `SubscriptionStatus` | Enum representing subscription states such as `PENDING_PAYMENT`, `ACTIVE`, `EXPIRED`, and `CANCELLED`. |
| `PaymentStatus` | Enum representing payment states such as `PENDING`, `SUCCESS`, and `FAILED`. |

## 3. Design Patterns

### 3.1 Strategy Pattern

**Used in:** `PricingStrategy`, `ThreeMonthsPlan`, `SixMonthsPlan`, `TwelveMonthsPlan`

Defines a common interface for pricing algorithms and allows different pricing strategies to be selected at runtime.

For example, a three-month subscription may have no discount, a six-month subscription may have a 10% discount, and a twelve-month subscription may have a 20% discount.

**Benefit:** Pricing algorithms can be changed or extended without modifying the subscription purchase workflow.

### 3.2 Strategy Pattern for Payments

**Used in:** `Payment`, `UPIPayment`, `CardPayment`

Defines a common payment interface with multiple implementations for different payment methods.

**Benefit:** New payment methods can be introduced without changing the business logic in `SubscriptionService`.

### 3.3 Factory Pattern

**Used in:** `PaymentFactory` or `PricingStrategyFactory`

Creates or selects the appropriate payment implementation or pricing strategy based on the user's selection.

For example, the factory can return `UPIPayment` when the user chooses UPI or `CardPayment` when the user chooses a card.

**Benefit:** Centralizes object creation and avoids scattering implementation-selection logic across services.

### 3.4 Observer Pattern / Event-Driven Design

**Used in:** `NotificationService`, `NotificationWorker`, `NotificationEvent`

Subscription and payment events trigger notification processing without requiring the core subscription workflow to send notifications directly.

For example, after a payment succeeds, a `PaymentSucceeded` event can be published and consumed by the notification worker.

**Benefit:** Decouples subscription processing from notification delivery and makes it easier to add new event consumers.

**Implementation note:** When using a message broker, this is typically implemented through event-driven messaging rather than only in-memory Observer objects.

### 3.5 Queue-Based Asynchronous Processing

**Used in:** `NotificationService`, `NotificationWorker`

Notification events are placed in a queue and processed asynchronously by background workers.

**Benefit:** Notifications do not block the subscription purchase response, and failed deliveries can be retried independently.

## 4. High-Level Purchase Workflow

1. The user selects a subscription plan and tenure.
2. `SubscriptionService` validates the user and the selected plan.
3. `PricingService` calculates the final price using the appropriate pricing strategy and any eligible promotional offers.
4. The system creates an invoice and a pending subscription.
5. `PaymentService` initiates payment using the selected payment method.
6. The payment provider confirms the payment outcome.
7. On successful payment, the subscription is activated and its start and end dates are recorded.
8. A payment-success notification event is published to the notification queue.
9. `NotificationWorker` processes the event and sends the notification to the user.
10. `SubscriptionExpiryWorker` periodically checks for upcoming expirations and schedules reminder notifications.

If payment fails, the subscription should not be activated. The system should record the failure and allow a safe retry.

## 5. Dynamic Pricing and Plan Updates

The system stores subscription plans, prices, benefits, and offers in persistent storage.

When an administrator updates a plan:

1. `AdminPlanService` validates and persists the changes.
2. The plan version is incremented when appropriate.
3. Caches are invalidated or updated so new purchases use the latest configuration.
4. Existing subscriptions retain their agreed billing details unless the applicable renewal policy explicitly permits a change.

Invoices should preserve the actual amounts charged, including discounts and taxes, so historical transactions remain auditable even when current prices change.
