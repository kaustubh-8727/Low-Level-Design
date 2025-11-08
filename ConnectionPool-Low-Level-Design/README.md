# Connection Pool - Low Level Design

## Overview
This project provides a **low-level design (LLD)** for a simple in-memory **Connection Pool** implemented in Java.  
It demonstrates how to manage database-like connections efficiently by reusing them instead of creating new ones for every request.

The design includes configuration management, connection lifecycle control, timeout handling, lazy initialization, and periodic cleanup of idle connections.

---

## Key Components

### 1. `Connection`
Represents a single logical database connection.

**Attributes:**
- `connectionID`: Unique identifier for the connection.
- `connectionState`: Enum representing the current state (`IDLE`, `ACTIVE`, `CLOSED`, `ERROR`).
- `createdTime`: Timestamp when the connection was created.
- `lastUsedTime`: Timestamp when it was last used.
- `connectionCredentials`: Holds credentials for the connection.

**Key Methods:**
- `close()`: Closes the connection.
- Standard getters and setters.

---

### 2. `ConnectionCredentials`
Stores the credentials and connection details required to establish a database connection.

**Attributes:**
- `userName`
- `password`
- `host`
- `port`
- `databaseName`

Used to initialize the connections in the pool.

---

### 3. `ConnectionPoolConfig`
Defines configuration parameters for the connection pool.

**Attributes:**
- `minimumConnections`: Minimum number of connections to maintain.
- `maximumConnections`: Maximum number of connections allowed.
- `idleTimeout`: Time after which idle connections are closed.
- `connectionRequestTimeout`: Time to wait for a free connection before throwing a timeout.
- `lazyInitialization`: Whether to initialize connections immediately or lazily.
- `testOnBorrow`: Whether to validate a connection before giving it to a user.
- `validationQuery`: Query used for connection validation.
- `connectionCredentials`: Database credentials.

Provides **default values** for each setting if not specified.

---

### 4. `ConnectionPoolConfigBuilder`
Implements the **Builder Pattern** for constructing `ConnectionPoolConfig` objects fluently.

### 5. ConnectionPoolService

The **ConnectionPoolService** class is the core component responsible for managing the lifecycle, state, and availability of all connections in the pool.  
It handles connection creation, allocation, validation, cleanup, and graceful shutdown.

#### Internal Data Structures

- **`BlockingQueue<Connection> idleConnections`**  
  Holds idle (available) connections that can be reused by clients.

- **`Set<Connection> activeConnections`**  
  Tracks all connections currently in use by clients.

- **`ScheduledExecutorService cleanupExecutor`**  
  A background thread that periodically checks and cleans up stale idle connections.

- **`AtomicLong connectionCount`**  
  Maintains the total number of active + idle connections currently managed by the pool.

#### Major Functions

- **`borrowConnection()`**  
  Retrieves a connection from the pool.  
  If no idle connection is available and the pool has not reached its maximum size, it creates a new connection.  
  If the pool is full, it waits up to the configured `connectionRequestTimeout`.

- **`returnConnection(Connection connection)`**  
  Returns a used connection back to the idle pool, marking it as `IDLE`.

- **`validateConnection(Connection connection)`**  
  Validates a connection before returning it to the caller, if `testOnBorrow` is enabled.  
  (Currently simulated through a dummy query check.)

- **`cleanupIdleConnections()`**  
  Periodically removes connections that have been idle for longer than the configured `idleTimeout`.  
  Executed by the scheduled cleanup thread.

- **`shutdown()`**  
  Gracefully shuts down the connection pool, closing all idle and active connections and stopping the cleanup executor.

#### Behavior

- Ensures that the total number of connections never exceeds the configured `maximumConnections`.
- Throws a timeout exception if no connection becomes available within `connectionRequestTimeout`.
- Uses a scheduled background task to automatically remove stale idle connections.
- Safely handles concurrent access to the pool through thread-safe collections and atomic operations.

---

### 6. ConnectionPool (Main Class)

The **ConnectionPool** class demonstrates how to initialize and interact with the `ConnectionPoolService`.

## Example Output

```
--- Borrowing connections sequentially ---
Borrowed connection: 0301e7b8-a93d-436a-a577-ffb17c502862 | State: ACTIVE
Borrowed connection: 94af5db2-3382-4017-b273-7a104e6380cd | State: ACTIVE
Returned connection: 0301e7b8-a93d-436a-a577-ffb17c502862

--- Waiting for idle connections to expire ---
Deleted idle connection: 144daea7-4cbe-4964-9cab-107f566cc1d2
Deleted idle connection: 0301e7b8-a93d-436a-a577-ffb17c502862

Connection Pool shutdown complete.

```
