# Java Logging Framework

A clean, extensible, and interview-ready **logging framework implemented in Java**, inspired by Log4j / Logback internals.  
Designed using **SOLID principles**, **classic design patterns**, and **thread-safe asynchronous logging**.

---

## Features

Supports standard log levels  
- `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`

Log level filtering  
- Configure minimum log level per logger

Multiple appenders  
- ConsoleAppender  
- FileAppender  

Multiple destinations per log  
- One log message can be written to **multiple appenders simultaneously**

Asynchronous logging  
- Non-blocking logging using **producer–consumer pattern**

Pluggable formatters  
- Simple text formatter  
- Easily extendable (JSON, pattern-based, etc.)

Clean public API  
```java
logger.info("Order created");
logger.error("Payment failed");

Client Code
   |
   v
 Logger  ──► LoggingStrategy (Sync / Async)
   |
   v
 Appenders (Console, File)
   |
   v
 Formatters
```

## Design Patterns Used

| Pattern | Usage |
|-------|------|
| **Singleton** | `LoggerFactory` |
| **Factory** | Logger creation |
| **Strategy** | Sync vs Async logging, Log formatters |
| **Producer–Consumer** | Async logging queue |
| **Composition** | `Logger → Appenders → Formatters` |

---

## Core Components

### LogLevel
Defines log severity and ordering:

DEBUG < INFO < WARN < ERROR < FATAL


Used for:
- Log filtering
- Comparing minimum configured log level

---

### LogMessage
Immutable value object representing a single log event.

**Fields include:**
- Timestamp  
- Log level  
- Logger name  
- Thread name  
- Message  
- Optional exception / stack trace  

✔ Thread-safe  
✔ Ideal for asynchronous logging  
✔ Passed through the system without mutation  

---

### Logger
Main API exposed to client applications.

Example:
```java
logger.info("Order created");
```

### Logger — Responsibilities

- Log level filtering  
- Creating `LogMessage` objects  
- Delegating logging to **sync or async strategy**  
- Dispatching logs to **multiple appenders**

---

### Appenders
Define **where logs go**.

**Supported appenders:**
- `ConsoleAppender`
- `FileAppender`

**Characteristics:**
- One log message can be sent to **multiple appenders**
- Each appender is **independent**
- Each appender has its **own formatter**

---

### Formatters
Define **how logs look**.

- Responsible only for formatting, **not I/O**
- Fully **pluggable and extensible**

**Example output:**
```text
2026-01-03T12:30:00Z [INFO] OrderService - Order created
```

**Easily extensible for:**
- JSON formatting
- Custom patterns
- Structured logging

---

### Async Logging
Designed to avoid blocking application threads.

**Implementation uses:**
- `BlockingQueue<LogMessage>`
- Background worker thread(s)
- Producer–consumer pattern

**Benefits:**
- Non-blocking log calls
- Better performance under high load
- Safe and scalable design

Application threads enqueue log messages, while background threads handle I/O operations asynchronously.
