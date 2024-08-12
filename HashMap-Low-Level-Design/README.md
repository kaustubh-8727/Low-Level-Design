# Custom HashMap Implementation

This project demonstrates a basic implementation of a HashMap in Java. The implementation includes methods for adding key-value pairs (`put`) and retrieving values by key (`get`). The HashMap handles collisions using a simple linked list (separate chaining).

## Introduction

The project implements a basic `HashMap` class called `HashMaps` that stores key-value pairs. It includes methods to insert and retrieve data efficiently using a hashing mechanism. This implementation is designed for educational purposes and demonstrates the underlying principles of a hash table.

## Classes and Design

### `Node<K, V>`
- Represents a node in the hash table's linked list.
- Stores a key-value pair and a reference to the next node in the chain.
- **Key Methods**:
  - `getKey()`: Returns the key stored in the node.
  - `getValue()`: Returns the value stored in the node.
  - `setValue(V value)`: Updates the value of the node.

### `HashMaps<K, V>`
- Implements a simple hash map using an array of `Node<K, V>` objects.
- Handles collisions using separate chaining with linked lists.
- **Key Methods**:
  - `put(K key, V value)`: Inserts or updates a key-value pair in the hash map.
  - `get(K key)`: Retrieves the value associated with the given key.

### Constants:
- `INITIAL_SIZE`: Initial size of the hash table (16).
- `MAXIMUM_CAPACITY`: Maximum allowed size of the hash table (2^30).

## How to Run

1. Ensure you have a Java development environment set up.
2. Copy the provided code into a Java file (e.g., `HashMapDesign.java`).
3. Compile the Java file:
    ```bash
    javac HashMapDesign.java
    ```
4. Run the compiled Java program:
    ```bash
    java HashMapDesign
    ```

## Example Usage

```java
public class HashMapDesign {
    public static void main(String[] args) {
        HashMaps<Integer, String> hashMap = new HashMaps<>();
        hashMap.put(1, "value 1");
        hashMap.put(2, "value 2");
        hashMap.put(4, "value 4");
        hashMap.put(5, "value 5");

        // Retrieve a value by key
        String value1 = hashMap.get(2);
        System.out.println(value1); // Output: value 2

        // Update the value associated with a key
        hashMap.put(2, "value 2 updated");

        // Retrieve the updated value
        String value2 = hashMap.get(2);
        System.out.println(value2); // Output: value 2 updated

        // Try to retrieve a value for a non-existent key
        String value3 = hashMap.get(6);
        System.out.println(value3); // Output: null
    }
}
