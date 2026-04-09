# Publisher-Subscriber System (Low Level Design)

## Problem Statement

Design and implement a **Publisher-Subscriber (Pub-Sub) Messaging System** where:

- Producers can publish messages to a topic.
- Subscribers can subscribe to one or more topics.
- Messages are delivered asynchronously to all subscribers of a topic.
- The system should be **thread-safe** and support **multiple producers and consumers**.
- Use a **queue-based approach** to decouple message production and consumption.

---

## System Design Overview

The system follows an **event-driven architecture**:
Producer → MessageQueue → Worker Threads → SubscriberService → Subscribers


- Producers publish messages to a queue.
- Worker threads consume messages asynchronously.
- Subscribers receive notifications based on topic subscriptions.

---

## Classes & Responsibilities

| Class | Description |
|------|------------|
| `Message` | Represents the message payload with metadata like id, status, and timestamp. |
| `Producer` | Represents a message producer with unique identity. |
| `Topic` | Maintains topic details and a thread-safe list of subscribers. |
| `Subscriber` | Represents a consumer that receives messages via `onMessage`. |
| `TopicService` | Manages topics (create, delete, fetch, subscribe). |
| `ProducerService` | Handles publishing messages to the queue. |
| `SubscriberService` | Notifies all subscribers of a topic. |
| `Event` | Wrapper object containing message, producerId, and topicId. |
| `MessageQueue` | Thread-safe blocking queue for message storage. |
| `MessageWorkerQueue` | Worker thread that consumes messages and dispatches them. |
| `PublisherSubscriberSystem` | Entry point to simulate the system. |

---

## Concurrency & Multithreading

- Uses `BlockingQueue` (`LinkedBlockingQueue`) for safe message passing.
- Multiple worker threads (`ExecutorService`) process messages concurrently.
- Thread-safe collections:
  - `ConcurrentHashMap` → topic storage
  - `CopyOnWriteArrayList` → subscriber list

---

## Design Patterns Used

### 1. Observer Pattern
- Subscribers listen to topics and get notified when new messages arrive.

### 2. Producer-Consumer Pattern
- Producers add messages to the queue.
- Workers consume and process them asynchronously.

### 3. Service Layer Pattern
- `TopicService`, `ProducerService`, `SubscriberService` separate business logic from core entities.

### 4. Event-Driven Architecture
- Messages are wrapped as events and processed asynchronously.

---

## Key Features

- Asynchronous message processing  
- Thread-safe design  
- Decoupled architecture  
- Scalable with multiple workers  
- Supports multiple topics and subscribers  

---

## Assumptions

- Each message belongs to a single topic.
- Subscribers receive all messages of subscribed topics.
- No persistence (in-memory system).
- No retry or failure recovery mechanism implemented.

---

## Possible Enhancements

- Add retry mechanism & dead-letter queue (DLQ)  
- Message filtering per subscriber  
- Kafka-like partitioning & offsets  
- Persistent storage (DB / distributed log)  
- Subscriber acknowledgment system  

---
