# Task Scheduler – Low Level Design

## Overview

This project implements a **multi-threaded task scheduler** that supports:

- One-time task execution at a specific time  
- Recurring tasks with fixed delay  
- Recurring tasks with fixed rate  
- Concurrent execution using a configurable worker thread pool  
- Safe scheduling, execution, rescheduling, and cancellation of jobs  

The design emphasizes **correct scheduling semantics**, **thread safety**, and **clean separation of responsibilities**, making it well-suited for **LLD / backend interviews**.

---

## High-Level Flow (Script Style)

1. Client creates a `JobMetadata` object describing **what to run** and **when**  
2. Client calls `SchedulerManager.createJob(...)`

3. **SchedulerManager**
   - Validates input  
   - Creates a `Job`  
   - Stores it in `TaskStore`  
   - Submits it to `SchedulerEngine`

4. **SchedulerEngine**
   - Converts `Job` into a `ScheduledTask`  
   - Places it into a `DelayQueue`

5. **Scheduler Thread**
   - Waits until the task becomes eligible  

6. **When execution time arrives**
   - Task is dispatched to `SchedulerExecutor`  

7. **SchedulerExecutor**
   - Executes the task using a worker thread  

8. **On completion**
   - Next execution time is calculated  
   - Task is rescheduled if required  

---

## Core Components & Responsibilities

### 1. Task

**Responsibility**
- Represents the actual unit of work  
- Encapsulates business logic  

**Key Notes**
- Executed by worker threads  
- Scheduler does not care about task internals  
- Keeps execution logic decoupled from scheduling logic  

---

### 2. Job

**Responsibility**
- Runtime representation of a scheduled job  
- Binds metadata and identity  

**Why it exists**
- Separates user intent (`JobMetadata`) from runtime entity  
- Provides a stable identifier (`jobId`) for cancellation and updates  

---

### 3. JobMetadata

**Responsibility**
- Immutable description of a job  

**Contains**
- Job name  
- Description  
- Task  
- Job type  
- Trigger  

**Design Intent**
- Represents **what the user wants**  
- Does not contain runtime state  
- Safe to validate and reuse  

---

### 4. Trigger (Interface)

**Responsibility**
- Decides **when the job should run next**

**Key Method**
- Computes next execution time based on execution context  

**Why it exists**
- Decouples scheduling logic from execution  
- Enables multiple scheduling strategies without modifying the core engine  

---

### 5. Trigger Implementations

#### OneTimeTrigger
- Executes once at a fixed timestamp  
- Returns `-1` after execution to signal termination  

#### FixedDelayTrigger
- Next execution calculated based on **last completion time**  
- Prevents overlapping executions  

#### FixedRateTrigger
- Next execution calculated based on **last scheduled time**  
- Maintains consistent rate even if execution is slow  

---

### 6. ExecutionContext

**Responsibility**
- Provides scheduling inputs to triggers:
  - Current time  
  - Last scheduled time  
  - Last completion time  

**Why it exists**
- Avoids tight coupling between scheduler internals and triggers  
- Makes triggers stateless and testable  

---

### 7. ScheduledTask

**Responsibility**
- Internal wrapper used by the scheduler  

**Maintains runtime scheduling state**
- Next execution time  
- Last scheduled time  
- Last completion time  

**Why it exists**
- Separates scheduling state from job definition  
- Implements `Delayed` to integrate with `DelayQueue`  

---

### 8. SchedulerEngine

**Responsibility**
- Core scheduling brain of the system  

**Owns**
- Scheduler thread  
- Delay queue  
- Rescheduling logic  

**What it does**
- Waits for tasks to become eligible  
- Dispatches tasks for execution  
- Reschedules recurring jobs  
- Handles cancellation and shutdown  

**Important Design Choice**
- Scheduler thread **never executes tasks**  
- It only coordinates timing  

---

### 9. SchedulerExecutor

**Responsibility**
- Executes tasks concurrently  

**Implementation**
- Fixed-size thread pool  

**Why separate from SchedulerEngine**
- Prevents long-running tasks from blocking scheduling  
- Allows independent scaling of execution capacity  

---

### 10. TaskStore

**Responsibility**
- Stores active jobs  
- Acts as an in-memory registry  

**Why it exists**
- Enables lookup for update / delete operations  
- Decouples persistence concerns from scheduling logic  

---

### 11. SchedulerManager

**Responsibility**
- Public API / Facade of the system  

**Entry point for**
- Creating jobs  
- Deleting jobs  
- Updating jobs  

**Why it exists**
- Shields clients from internal complexity  
- Centralizes validation and lifecycle management  

---

## Design Patterns Used

### 1. Singleton Pattern
**Used in**
- `SchedulerManager`  
- `TaskStore`  

**Why**
- Scheduler is a shared infrastructure component  
- Ensures a single coordination point  

---

### 2. Strategy Pattern
**Used in**
- `Trigger` interface and its implementations  

**Why**
- Allows different scheduling behaviors  
- Open for extension, closed for modification  

---

### 3. Producer–Consumer Pattern
**Used in**
- `SchedulerEngine` (producer)  
- `SchedulerExecutor` (consumer)  

**Why**
- Decouples scheduling from execution  
- Improves throughput and scalability  

---

### 4. Facade Pattern
**Used in**
- `SchedulerManager`  

**Why**
- Simplifies client interaction  
- Hides internal subsystem complexity  

---

### 5. Template Method Pattern (Implicit)

**Used in**
- Execution lifecycle:
- schedule → execute → reschedule

**Why**
- Enforces a consistent execution flow  
- Allows variations via triggers  

---

## Concurrency Model

### Scheduler Thread
- Single thread  
- Handles timing and ordering  

### Worker Threads
- Configurable thread pool  
- Execute tasks concurrently  

### Thread Safety
- Volatile fields for scheduling state  
- Concurrent collections  
- No shared mutable state between tasks  

---

## Summary

This task scheduler cleanly separates **what to run**, **when to run**, and **how to run**, while ensuring correctness, scalability, and extensibility.  
It demonstrates strong LLD principles, proper concurrency handling, and effective use of design patterns—making it an ideal interview-ready design.

## Sample Output

```
Job scheduled with ID: f8c4cf8b-4583-44c5-8c7e-7f02a6c510a1
Task executed at 1767899572420
Task executed at 1767899574412
Task executed at 1767899576416
Task executed at 1767899578416
Task executed at 1767899580416
Job cancelled
```
