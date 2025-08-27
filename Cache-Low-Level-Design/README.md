# Cache System - Low Level Design

This project demonstrates the **Low Level Design (LLD)** of a **Cache System** in Java with support for pluggable eviction strategies such as **LRU (Least Recently Used)** and placeholders for **LFU (Least Frequently Used)**.

---

## Features
- **Cache Storage**
  - In-memory storage with configurable capacity.
  - Thread-safe using `ConcurrentHashMap`.
- **Database Storage**
  - Abstracted storage layer (can be integrated with any DB).
- **Eviction Algorithms**
  - `LRUCacheEvictionAlgorithm` → Removes least recently used items.
  - `LFUCacheEvictionAlgorithm` → Placeholder for least frequently used items.
- **Cache Operations**
  - `updateData(K key, V value)` → Adds/updates value in cache and DB.
  - `getData(K key)` → Reads from cache, falls back to DB if not present.
- **Synchronization**
  - Thread-safe read/write operations (`synchronized`).

---

## Class Design

### 1. **Storage**
- `CacheStorage<K,V>` → Defines in-memory cache contract.
- `CacheStorageImpl<K,V>` → Implements cache storage using `ConcurrentHashMap`.
- `DatabaseStorage<K,V>` → Defines persistent storage contract.
- `DatabaseStorageImpl<K,V>` → Placeholder for DB integration.

### 2. **Eviction**
- `CacheEvictionAlgorithm<K,V>` → Strategy interface for eviction.
- `LRUCacheEvictionAlgorithm<K,V>` → Implements **Least Recently Used**.
- `LFUCacheEvictionAlgorithm<K,V>` → Placeholder for **Least Frequently Used**.
- `DoublyLinkedListNode<K>` → Used by LRU for O(1) updates and eviction.
- `CacheEvictionStrategy<K,V>` → Wrapper to choose eviction algorithm.

### 3. **Cache**
- `Cache<K,V>` → Manages:
  - Cache storage
  - Database storage
  - Eviction strategy
  - Read/Write synchronization

### 4. **Main**
- `CacheLowLevelDesign` → Demonstrates cache usage with **LRU eviction**.

---

## Example Run

```java

value fetched for key - 2 from cache is blue
value fetched for key - 4 from cache is green
value fetched for key - 5 from cache is orange
eviction process started
value added to database
value fetched for key - 6 from cache is purple

