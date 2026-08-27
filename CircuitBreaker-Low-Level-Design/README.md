# Circuit Breaker - Low Level Design

## Problem Statement

Design and implement a **Circuit Breaker** that protects an application from continuously calling an unhealthy downstream service.

The Circuit Breaker monitors failures from downstream service calls and changes its state based on the failure threshold and recovery timeout.

It supports three states:

- **CLOSED** - Requests are allowed and failures are tracked.
- **OPEN** - Requests are rejected immediately without calling the downstream service.
- **HALF_OPEN** - After a configured timeout, a limited number of requests are allowed to check whether the downstream service has recovered.

### State Transition

```text
                    Failure Threshold Reached
               ┌───────────────────────────────┐
               │                               │
               ▼                               │
          ┌─────────┐                     ┌─────────┐
          │ CLOSED  │                     │  OPEN   │
          └────┬────┘                     └────┬────┘
               │                               │
               │ Success                       │ Timeout Expired
               │                               │
               │                               ▼
               │                         ┌───────────┐
               │                         │ HALF_OPEN │
               │                         └─────┬─────┘
               │                               │
               │                 ┌─────────────┴─────────────┐
               │                 │                           │
               │              Success                      Failure
               │                 │                           │
               │                 ▼                           ▼
               └────────────── CLOSED                       OPEN
```

## Functional Requirements

1. Execute requests through the Circuit Breaker.
2. Maintain `CLOSED`, `OPEN`, and `HALF_OPEN` states.
3. Track consecutive failures from downstream requests.
4. Open the circuit when the configured failure threshold is reached.
5. Reject requests immediately when the circuit is `OPEN`.
6. Automatically move from `OPEN` to `HALF_OPEN` after the configured timeout.
7. Allow a limited number of requests while in `HALF_OPEN`.
8. Move back to `CLOSED` when the recovery request succeeds.
9. Move back to `OPEN` when the recovery request fails.
10. Allow configuration of failure threshold, open-state timeout, and `HALF_OPEN` request limit.

---

# Classes and Interfaces

## 1. `CircuitState`

Represents the three possible states of the Circuit Breaker: `CLOSED`, `OPEN`, and `HALF_OPEN`.

---

## 2. `CircuitBreakerConfig`

Stores the configurable parameters of the Circuit Breaker, such as failure threshold, open-state timeout, and maximum `HALF_OPEN` requests.

---

## 3. `CircuitBreakerConfig.Builder`

Builds `CircuitBreakerConfig` objects using a fluent API while validating configuration values.

---

## 4. `FailureTracker`

Defines the interface for tracking failures and determining when the Circuit Breaker should open.

---

## 5. `ConsecutiveFailureTracker`

Implements `FailureTracker` by tracking consecutive failures and opening the circuit once the configured threshold is reached.

---

## 6. `CircuitOpenException`

Represents an exception thrown when a request is rejected because the Circuit Breaker is `OPEN` or the `HALF_OPEN` request limit has been reached.

---

## 7. `CircuitBreakerContext`

Maintains the Circuit Breaker's shared state, configuration, failure tracker, timestamps, and `HALF_OPEN` request permits.

---

## 8. `CircuitStateHandler`

Defines the common interface for handling request execution for each Circuit Breaker state.

---

## 9. `ClosedStateHandler`

Handles requests when the Circuit Breaker is `CLOSED`, records failures, and transitions the Circuit Breaker to `OPEN` when the failure threshold is reached.

---

## 10. `OpenStateHandler`

Handles requests when the Circuit Breaker is `OPEN`, rejects requests until the timeout expires, and then transitions the Circuit Breaker to `HALF_OPEN`.

---

## 11. `HalfOpenStateHandler`

Handles recovery requests in the `HALF_OPEN` state and transitions the Circuit Breaker to either `CLOSED` on success or `OPEN` on failure.

---

## 12. `CircuitBreaker`

Defines the public interface for executing requests through the Circuit Breaker.

---

## 13. `DefaultCircuitBreaker`

Provides the main Circuit Breaker implementation and delegates request handling to the appropriate state handler.

---

## 14. `CircuitBreakerSystem`

Contains the `main()` method and demonstrates the Circuit Breaker's state transitions and request execution.

# Design Patterns Used

## 1. State Pattern

The **State Pattern** is used to encapsulate the behavior associated with each Circuit Breaker state.

```text
CircuitStateHandler
       |
       ├── ClosedStateHandler
       ├── OpenStateHandler
       └── HalfOpenStateHandler
```

## 2. Strategy Pattern

The **Strategy Pattern** is used for failure tracking.

```text
FailureTracker
      |
      └── ConsecutiveFailureTracker
```

## 3. Builder Pattern

The **Builder Pattern** is used to construct `CircuitBreakerConfig`.

```java
CircuitBreakerConfig config =
    new CircuitBreakerConfig.Builder()
        .failureThreshold(3)
        .openStateTimeout(Duration.ofSeconds(5))
        .halfOpenMaxRequests(1)
        .build();
```

```text
Request 1 → Failure
Request 2 → Failure
Request 3 → Failure
                |
                ▼
          CLOSED → OPEN
                |
                ▼
       Requests rejected
                |
          Wait 5 seconds
                |
                ▼
          OPEN → HALF_OPEN
                |
                ▼
        Recovery request
           /          \
       Success       Failure
          |             |
          ▼             ▼
       CLOSED          OPEN
```
