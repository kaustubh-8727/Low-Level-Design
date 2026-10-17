
# Package Delivery System — Low-Level Design (LLD)

## 1. Problem Statement

Design a package delivery system for an e-commerce company like Amazon that manages orders, shipment creation, delivery agent assignment, delivery status tracking, and unsuccessful delivery attempts.

### Requirements

- **Order Management:** Receive customer orders containing multiple product items.
- **Shipment Creation:** Create shipments from orders and maintain shipment status.
- **Delivery Agent Assignment:** Assign available delivery agents based on rating, active workload, or proximity.
- **Delivery Tracking:** Track delivery progress through status updates.
- **Asynchronous Processing:** Process delivery status events asynchronously using a blocking queue.
- **Failed Delivery Attempts:** Record unsuccessful delivery attempts and retry delivery by reassigning an eligible agent.
- **Prime Order Priority:** Prioritize Prime shipments over standard shipments during assignment.
- **Prime Agent Selection:** Assign the highest-rated available delivery agent to Prime shipments.

## 2. Classes and Responsibilities

| Class / Interface | Responsibility |
|---|---|
| `Order` | Represents a customer order containing multiple product items and delivery details. |
| `ProductItem` | Represents an individual product and its quantity in an order. |
| `Address` | Stores the delivery address and geographic coordinates. |
| `Shipment` | Represents a shipment created from an order and maintains its priority and status. |
| `Delivery` | Tracks the assigned agent, delivery status, and delivery attempts. |
| `DeliveryAgent` | Stores agent details, rating, location, availability, and active delivery count. |
| `DeliveryAttempt` | Records each delivery attempt, its outcome, timestamp, and failure reason. |
| `DeliveryEvent` | Represents a delivery status update submitted by a delivery agent. |
| `PackagingService` | Creates shipments from orders and submits them for delivery assignment. |
| `DeliveryService` | Manages agent assignment, delivery status transitions, and unsuccessful delivery attempts. |
| `DeliveryAssignStrategy` | Defines the interface for delivery agent assignment strategies. |
| `AssignByHighestRating` | Selects the highest-rated available delivery agent. |
| `AssignByLessOrders` | Selects the available agent with the fewest active deliveries. |
| `AssignByNearestDistance` | Selects the nearest available delivery agent. |
| `DeliveryEventQueue` | Processes delivery status events asynchronously using a blocking queue. |
| `Main` | Initializes the system and demonstrates shipment creation, agent assignment, and delivery status updates. |

## 3. Class Relationships

- An `Order` contains multiple `ProductItem` objects and has a delivery `Address`.
- An `Order` can generate one or more `Shipment` objects.
- A `Shipment` contains product items and maintains its priority and shipment status.
- A `Delivery` is associated with a `Shipment` and an assigned `DeliveryAgent`.
- A `Delivery` maintains multiple `DeliveryAttempt` records.
- A `DeliveryEvent` references a delivery and contains a status update.
- `PackagingService` creates shipments and submits them to `DeliveryService`.
- `DeliveryService` uses `DeliveryAssignStrategy` to select a delivery agent.
- `AssignByHighestRating`, `AssignByLessOrders`, and `AssignByNearestDistance` implement `DeliveryAssignStrategy`.
- `DeliveryEventQueue` consumes delivery events asynchronously and delegates status updates to `DeliveryService`.

## 4. Design Patterns Used

### 4.1 Strategy Pattern

**Purpose:** Encapsulates different delivery agent assignment algorithms behind a common interface.

**Implementation:**
- `DeliveryAssignStrategy` defines the assignment contract.
- `AssignByHighestRating` selects the highest-rated available agent.
- `AssignByLessOrders` selects the agent with the lowest active workload.
- `AssignByNearestDistance` selects the nearest available agent.

### 4.2 Producer-Consumer Pattern

**Purpose:** Separates the production of delivery status events from their processing.

**Implementation:**
- The delivery agent produces status events.
- `DeliveryEventQueue` stores events in a `BlockingQueue`.
- A consumer thread processes events asynchronously through `DeliveryService`.


### 4.3 Priority-Based Scheduling

**Purpose:** Ensures Prime shipments receive assignment priority over standard shipments.

**Implementation:**
- A `PriorityBlockingQueue` stores pending shipments.
- Prime shipments receive higher priority than standard shipments.
- Shipment creation time breaks ties between shipments with equal priority.

## 5. System Workflow

1. The customer places an order containing one or more product items.
2. `PackagingService` creates a shipment and marks it as pending assignment.
3. The shipment is added to the priority queue.
4. `DeliveryService` retrieves the highest-priority shipment.
5. An assignment strategy selects an eligible delivery agent:
   - Prime shipment: highest-rated available agent.
   - Standard shipment: agent with the fewest active deliveries.
6. The selected agent is reserved, and a `Delivery` object is created.
7. The delivery agent submits status updates through `DeliveryEventQueue`.
8. The queue consumer processes events asynchronously and invokes `DeliveryService`.
9. `DeliveryService` validates status transitions and updates delivery and shipment statuses.
10. If delivery fails, a `DeliveryAttempt` is recorded and another eligible agent may be assigned.
11. Delivery ends successfully when the shipment reaches `DELIVERED`, or unsuccessfully when the retry limit is reached.

## 6. Output
```
Shipment created: Shipment{id='S1', order='O101', priority=STANDARD, status=PENDING_ASSIGNMENT}
Shipment created: Shipment{id='S2', order='O102', priority=PRIME, status=PENDING_ASSIGNMENT}
Assigned S2 to Priya (A2, rating=4.9) using AssignByHighestRating
Assigned S1 to Rahul (A1, rating=4.5) using AssignByLessOrders

Prime agent assigned: Priya (A2, rating=4.9)

Standard agent assigned: Rahul (A1, rating=4.5)
Processed event E1, delivery status = OUT_FOR_DELIVERY, shipment status = OUT_FOR_DELIVERY
Delivery attempt failed: Customer unavailable
Reassigned delivery D1 to Priya (A2, rating=4.9)
Processed event E2, delivery status = ASSIGNED, shipment status = ASSIGNED

Final shipment statuses:
Shipment{id='S1', order='O101', priority=STANDARD, status=ASSIGNED}
Shipment{id='S2', order='O102', priority=PRIME, status=ASSIGNED}

```
