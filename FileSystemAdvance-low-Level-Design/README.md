# In-Memory File System (LLD)

## Overview
This project implements an **in-memory file system** that mimics basic Linux directory operations such as `mkdir`, `cd`, and `pwd`.

It is designed with a focus on:
- Clean object-oriented design
- Extensibility
- Realistic filesystem behavior (absolute/relative paths, wildcards)

---

## Supported APIs

### `mkdir(dirname: String)`
Creates a new directory inside the current working directory.

### `createFile(name: String, content: String)`
Creates a file with content in the current directory.

### `cd(path: String)`
Changes the current working directory.

Supports:
- Absolute paths (`/home/user`)
- Relative paths (`../docs`)
- Special symbols:
  - `.` → current directory
  - `..` → parent directory
  - `*` → wildcard (matches any child directory)

---

### `pwd()`
Returns the absolute path of the current working directory.

---

## Class Design

### `Node` (Abstract Class)
Base abstraction for both files and directories, containing common attributes like name and parent.

### `File`
Represents a file with content, metadata (timestamps), and size.

### `Directory`
Represents a directory that stores child nodes (both files and subdirectories).

### `FileSystem`
Core controller class that manages filesystem operations and maintains root and current directory state.

### `IFileSystem` (Interface)
Defines the contract for filesystem operations like `mkdir`, `cd`, `pwd`, and file creation.

---

## Design Patterns Used

### 1. Composite Pattern (Core Pattern)
Treats `File` and `Directory` uniformly using a common `Node` abstraction, enabling a tree-like structure.

### 2. Singleton Pattern
Ensures only one instance of `FileSystem` exists and provides a global access point.

### 3. Interpreter Pattern (Conceptual)
The `cd(path)` logic interprets path expressions (`.`, `..`, `*`) step-by-step, treating the path as a mini language.

### 4. Command Pattern (Optional)
Can be introduced to encapsulate operations like `mkdir`, `cd`, etc., useful for undo/redo or logging.

---

## Key Design Decisions

- **Parent Pointer over Stack**
  - Simplifies navigation (`cd ..`) without extra data structures.

- **Single Map (`Map<String, Node>`)**
  - Allows directories to hold both files and subdirectories.

- **Deterministic Wildcard Handling**
  - `*` selects the first available directory for simplicity.

---

## Example Usage

```java
FileSystem fs = FileSystem.getInstance();

fs.mkdir("home");
fs.cd("home");

fs.mkdir("user");
fs.cd("user");

fs.createFile("file.txt", "hello");

System.out.println(fs.pwd()); // /home/user

fs.cd("..");
System.out.println(fs.pwd()); // /home

fs.cd("/home/user");
System.out.println(fs.pwd()); // /home/user
```
