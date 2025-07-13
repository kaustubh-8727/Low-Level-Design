# 🚦 Sliding Window Rate Limiter in Java

This is a low-level implementation of a **Rate Limiter** using the **Sliding Window algorithm** in Java. The purpose of this design is to control the rate of incoming user requests in a multi-threaded environment.

## Features

- Thread-safe rate limiting using synchronized blocks  
- Sliding Window algorithm for precise time-based control  
- Configurable request limit and window size  
- Simulated with a fixed thread pool to mimic concurrent requests  

## How It Works

The **Sliding Window algorithm** keeps track of request timestamps in a queue. On each request:

1. The queue is cleaned of all timestamps older than the window size.
2. If the current queue size is less than the maximum allowed requests, the request is accepted.
3. Otherwise, the request is **rate-limited**.

This ensures a real-time, rolling window of time-based request control.

## Design Breakdown

### `RateLimitSlidingWindow`

- Holds the `windowSizeTime` (in seconds) and `maxRequest`.
- Uses a `Queue<Long>` to store timestamps of accepted requests.
- `handleRequest()` checks and maintains this queue.

### `UserRequestRateLimit`

- Encapsulates `RateLimitSlidingWindow`.
- Accepts configuration via `rateLimitConfigSetup(windowSize, maxRequest)`.
- Exposes `makeRequest()` to simulate incoming requests.

### `RateLimiterDesign` (Main Class)

- Configures the limiter to accept max **5 requests per 1 second**.
- Uses an `ExecutorService` with 10 threads to simulate concurrent requests.


3. Sample Output:

```
user request: 2 is accepted
user request: 5 is accepted
user request: 3 is accepted
user request: 1 is accepted
user request: 4 is accepted
user request: 7 is not accepted (rate limited)
user request: 6 is not accepted (rate limited)
...
```
