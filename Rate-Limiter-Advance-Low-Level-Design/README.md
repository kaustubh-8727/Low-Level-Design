# Rate Limiter Low-Level Design

This project demonstrates a **rate limiter system** implemented in Java, supporting configurable algorithms and thread-safe request handling.

---

## Overview

The rate limiter controls the number of requests a user can make in a given time window.  
Currently, it supports the **Token Bucket algorithm**, but the design is extensible for other algorithms like Sliding Window.  

Key features:

- Configurable rate limiting strategies
- In-memory storage for state
- Thread-safe request handling
- Singleton global rate limiter instance

---

## Entities and Responsibilities

1. **AlgorithmType (Enum)**
   - Represents the type of rate-limiting strategy.
   - Examples: Token Bucket, Sliding Window.

2. **RateLimiterConfig**
   - Holds configuration parameters for the rate limiter.
   - Includes fields like bucket size, refill duration, max tokens, window duration.
   - Makes the system **configurable and flexible** for different algorithms.

3. **RateLimitKey**
   - Represents a unique key for each rate-limited entity (e.g., user ID, API key).
   - Ensures correct tracking of request limits per entity.

4. **RateLimitStore (Interface)**
   - Abstracts storage of rate-limiting state.
   - Allows different storage implementations (in-memory, Redis, database).

5. **InMemoryRateLimitStore**
   - Concrete implementation using a thread-safe map.
   - Maintains the state of each `RateLimitKey`.

6. **RateLimiterAlgorithm (Interface)**
   - Defines the contract for any rate-limiting algorithm.
   - Ensures that different strategies can be plugged in easily.

7. **TokenBucketAlgorithm**
   - Implements the Token Bucket rate-limiting strategy.
   - Tracks available tokens and refill time.
   - Handles request acceptance or rejection based on token availability.
   - Ensures **thread safety** using synchronization.

8. **UserRequestRateLimiter**
   - Singleton class providing a global rate limiter instance.
   - Initializes configuration, algorithm, and store.
   - Handles incoming requests from users.

9. **RateLimiterLowLevelDesign (Main Driver)**
   - Demonstrates concurrent requests from multiple users.
   - Shows the behavior of the rate limiter in real-time.

---

## Design Patterns Used

1. **Singleton**
   - `UserRequestRateLimiter` ensures only one instance of the rate limiter exists globally.
   - Useful for centralized request limiting in a system.

2. **Strategy**
   - `RateLimiterAlgorithm` interface allows switching between different rate-limiting algorithms.
   - Promotes **extensibility** without changing client code.

3. **Factory/Config-based Initialization**
   - Configuration object (`RateLimiterConfig`) abstracts algorithm parameters.
   - Makes system flexible and decoupled from hardcoded values.

4. **Thread Safety / Synchronization**
   - Ensures safe concurrent access to shared state (`TokenBucketState`).
   - Prevents race conditions in multi-threaded environments.

---

## Key Takeaways

- The system is **modular**, with each entity having a single responsibility.
- Adding a new algorithm requires implementing the `RateLimiterAlgorithm` interface.
- State storage can be swapped easily (e.g., from in-memory to Redis).
- Singleton pattern ensures one central limiter across the system.
- Strategy pattern allows multiple algorithms without modifying client code.
- Thread safety is critical for concurrent request handling.

---
