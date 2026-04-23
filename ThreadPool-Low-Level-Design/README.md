# Custom Thread Pool (Java)

## Problem Statement
Design and implement a custom thread pool in Java that accepts tasks (Runnable), executes them using a fixed number of worker threads, uses a bounded queue, and supports graceful and immediate shutdown.

---

## Classes Overview

- `CustomThreadPool` → Manages worker threads, task queue, and task submission  
- `WorkerThread` → Continuously polls tasks from the queue and executes them  
- `State (enum)` → Represents worker lifecycle states (AVAILABLE, STOPPED, FAILED)  
- `ThreadPool` → Demo class to test the thread pool  

---

## Approach

Uses a fixed-size thread pool with a blocking queue where worker threads continuously poll tasks and execute them, ensuring controlled concurrency and efficient task processing.

---

## Features

- Fixed number of worker threads  
- Bounded task queue (`ArrayBlockingQueue`)  
- Thread-safe worker management (`CopyOnWriteArrayList`)  
- Task rejection when queue is full  
- Graceful shutdown (`shutdown`)  
- Immediate shutdown (`shutdownNow`)  

---

## Example Usage

```java
CustomThreadPool pool = new CustomThreadPool(5);

for (int i = 1; i <= 20; i++) {
    int taskId = i;
    pool.submit(() -> {
        System.out.println("Executing task: " + taskId);
    });
}
```

## Notes

- Submitting tasks after shutdown throws `RejectedExecutionException`  
- Queue capacity currently also controls number of worker threads  
- `shutdown()` completes existing tasks, while `shutdownNow()` interrupts execution  

---

## Complexity

- Task submission: O(1)  
- Task execution: O(1) per task (excluding task runtime)
