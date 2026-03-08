# Alerting System (Low Level Design)

## Problem

Design a simple alerting / messaging system that processes system events and sends notifications through multiple channels such as Email, SMS, and Push notifications.

The system should support:
- Multiple notification channels
- Asynchronous processing
- Concurrent workers
- Easy extensibility for new channels

The system uses an in-memory queue with worker threads to simulate asynchronous alert processing.

---

## Classes Overview

**Event**  
Represents an incoming system event that may trigger an alert.

**Alert**  
Represents the notification generated from an event and contains the alert message and target channels.

**AlertType (Enum)**  
Defines alert severity levels such as P0, P1, and P2.

**ChannelType (Enum)**  
Defines supported notification channels like EMAIL, SMS, and PUSH.

**NotificationChannel (Interface)**  
Defines the common contract for all notification channels.

**EmailChannel**  
Concrete implementation that sends alert notifications via Email.

**SMSChannel**  
Concrete implementation that sends alert notifications via SMS.

**PushChannel**  
Concrete implementation that sends alert notifications via Push notifications.

**NotificationDispatcher**  
Routes alerts to the appropriate notification channels based on the channel type.

**AlertQueue**  
Thread-safe queue that buffers alerts between producers and worker threads.

**AlertWorker**  
Worker thread that continuously consumes alerts from the queue and dispatches them.

**AlertProcessor**  
Processes incoming events and converts them into alerts which are pushed to the queue.

**AlertingSystem (Main)**  
Entry point that initializes components, starts worker threads, and simulates events.

---

## Design Patterns Used

**Strategy Pattern**  
Used for notification channels so different implementations (Email, SMS, Push) can be used interchangeably.

**Producer-Consumer Pattern**  
Used with AlertProcessor, AlertQueue, and AlertWorker to enable asynchronous alert processing.

**Dependency Injection**  
Dependencies such as NotificationDispatcher and AlertQueue are injected via constructors to keep components loosely coupled.

**Output**
```
Worker-3 -> Sending EMAIL: Alert triggered for event: SERVER_DOWN_3
Worker-1 -> Sending EMAIL: Alert triggered for event: SERVER_DOWN_1
Worker-2 -> Sending EMAIL: Alert triggered for event: SERVER_DOWN_2
Worker-2 -> Sending SMS: Alert triggered for event: SERVER_DOWN_2
Worker-3 -> Sending SMS: Alert triggered for event: SERVER_DOWN_3
Worker-1 -> Sending SMS: Alert triggered for event: SERVER_DOWN_1
Worker-1 -> Sending PUSH: Alert triggered for event: SERVER_DOWN_1
Worker-1 -> Sending EMAIL: Alert triggered for event: SERVER_DOWN_4
Worker-3 -> Sending PUSH: Alert triggered for event: SERVER_DOWN_3
Worker-2 -> Sending PUSH: Alert triggered for event: SERVER_DOWN_2
Worker-1 -> Sending SMS: Alert triggered for event: SERVER_DOWN_4
Worker-1 -> Sending PUSH: Alert triggered for event: SERVER_DOWN_4
Worker-3 -> Sending EMAIL: Alert triggered for event: SERVER_DOWN_5
Worker-3 -> Sending SMS: Alert triggered for event: SERVER_DOWN_5
Worker-3 -> Sending PUSH: Alert triggered for event: SERVER_DOWN_5
```
