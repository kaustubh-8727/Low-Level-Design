# 📁 File System Low-Level Design in Java

This project demonstrates a **low-level design (LLD)** of a simple in-memory file system using **Object-Oriented Programming (OOP)** principles in Java. It supports core functionalities similar to Unix/Linux systems:

- `ls`: List directory and file structures in a hierarchical format.
- `findFile`: Search for files with a specific extension recursively.

---

## Problem Statement

Design and implement a simplified file system with the ability to:

1. Represent files and directories.
2. List all files and directories (`ls`) in a tree-like structure.
3. Search and return all files with a given extension (`findFile`).

---

## Class Design

### `FileSystemNode` (Interface)
An abstraction for both `File` and `Directory` nodes.

- `void ls(String indent)` — lists structure with indentation.
- `List<String> findFile(String fileExtension)` — recursively finds files by extension.

### `File`
Represents a file and implements `FileSystemNode`.

- Stores the file name.
- Implements extension match logic.
- Prints formatted output using `ls`.

### `Directory`
Represents a directory and implements `FileSystemNode`.

- Stores a list of `FileSystemNode` children (files or subdirectories).
- Recursively traverses and prints the tree.
- Searches nested files by extension.

---

## Example Output
```
|-- [DIR] root
    |-- [DIR] company1
        |-- [FILE] facebook.txt
        |-- [FILE] google.js
        |-- [FILE] microsoft.txt
        |-- [FILE] netflix.xml
    |-- [DIR] company2
        |-- [FILE] amazon.xml
        |-- [FILE] apple.xml

search all files with extension with .XML in a directory - 
netflix.xml
amazon.xml
apple.xml

```

