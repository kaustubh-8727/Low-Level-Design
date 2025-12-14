## Problem Description

You are required to design an **Employee Access Management System** that manages and controls access permissions for employees over different resources within an organization.

The system should allow **fine-grained access control**, enabling multiple types of permissions to be **assigned, queried, and revoked efficiently**.

The focus of this problem is on **designing the data structures, classes, and interactions** required to support the described functionality.  
You are **not required to write the implementation or solution**, but to clearly understand and define what the system is expected to do.

---

## Core Concepts

### Employees

- The system supports **multiple employees**, uniquely identified by an `employee_id`.
- Example: E1, E2, E3, ..., En


---

### Resources

- The system manages **multiple resources**, each uniquely identified by a `resource_id`.
- Example: R1, R2, R3, ..., Rn

- A resource can represent:
- A document
- A service
- A database
- An internal tool

---

### Access Types

Each employee can be granted one or more of the following access types on a resource:

- **READ** – Permission to view or read the resource
- **WRITE** – Permission to modify the resource
- **ADMIN** – Full control over the resource, including managing access

> An employee may have **multiple access types for the same resource simultaneously**.

---

## Functional Requirements

The system must support the following operations:

---

### Grant Access

**Method**
```text
grant_access(employee_id, resource_id, access_type)

**Description**
- Grants a specific type of access to an employee for a given resource.
- If the employee already has some access to the resource, the new access type is **added without removing existing ones**.
- The same employee can have **multiple access types** for the same resource.
```
---

### Revoke Access

**Method**
```text
revoke_access(employee_id, resource_id, access_type)

**Description**
- Revokes a specific access type from an employee for a given resource.
- If access_type is provided, only that permission is removed.
- If access_type is null or None, all access permissions for that resource are revoked.
- If the employee does not have the specified access, no change is required.
```
---

### Retrieve Access for a Resource

**Method**
```text
retrieve_access(employee_id, resource_id)

**Description**
- Returns all access types that the employee currently has for the specified resource.
- If the employee has no access to the resource, an empty list is returned.
```

---

### Retrieve Access for a Resource

**Method**
```text
retrieve_access(employee_id, resource_id)

**Description**
- Returns all access types that the employee currently has for the specified resource.
- If the employee has no access to the resource, an empty list is returned.
```

---

### Retrieve Accessible Resources for an Employee

**Method**
```text
retrieve_resources(employee_id)

**Description**
- Returns a list of all resources the employee has access to.
- Only resources with at least one access type should be included.
- If the employee has no access to any resource, an empty list is returned.
```

## Expectations

- The design should clearly define how:
  - Employees
  - Resources
  - Access permissions  
  are modeled and related.

- Data consistency and correctness of access information must be maintained.
- The system should support **efficient querying and updates**.
- No **user interface** or **persistence layer** is required unless explicitly stated.


## UML-Style Design: Employee Access Management System

---

### Class Diagram Overview

```text
+-------------+        +-------------------+
|  Employee   |        |     Resource      |<------------------+
+-------------+        +-------------------+                   |
| - id        |        | + getId()         |                   |
| - name      |        | + getType()       |                   |
+-------------+        +-------------------+                   |
                                                               |
                                                               |
                                   +---------------------------+
                                   |
                          +-------------------+
                          | ResourceImpl(s)   |
                          +-------------------+
                          | FileResource     |
                          | DbResource       |
                          | ApiResource      |
                          +-------------------+

+---------------------+
| AccessPermission    |
+---------------------+
| READ                |
| WRITE               |
| ADMIN               |
+---------------------+

+-----------------------------+
|     ResourceManager         |
+-----------------------------+
| - accessStore               |
+-----------------------------+
| + grantAccess(...)          |
| + revokeAccess(...)         |
| + retrieveAccess(...)       |
| + retrieveResources(...)    |
+-----------------------------+

+--------------------------------+
| EmployeeResourceProxy          |
+--------------------------------+
| - resourceManager              |
+--------------------------------+
| + getResource(...)             |
+--------------------------------+
```

## 2️⃣ Class Descriptions

---

### **Employee**

Represents an employee in the organization.

**Attributes**
- `employeeId`
- Optional metadata (name, department, etc.)

**Responsibilities**
- Acts as a unique identity
- Does not contain access logic

**Relationships**
- Has access to multiple resources via `ResourceManager`

---

### **Resource (Interface)**

Represents any protected resource.

**Methods**
- `getId()`
- `getType()`

**Purpose**
- Enables polymorphism
- Supports multiple resource types without modifying access logic

---

### **Resource Implementations**

Concrete implementations of `Resource`.

**Examples**
- `FileResource`
- `DatabaseResource`
- `ApiResource`

**Design Benefit**
- Follows the **Open–Closed Principle**
- New resource types can be added easily

---

### **AccessPermission (Enum)**

Defines allowed access levels.

**Values**
- `READ`
- `WRITE`
- `ADMIN`

**Purpose**
- Ensures type safety
- Prevents invalid access states

---

### **ResourceManager**

Central authority for managing employee access.

**Responsibilities**
- Maintains mapping between:
  ```text
  Employee → Resource → Set<AccessPermission>
  ```

**Implements all access control operations**

---

### Public Methods

- `grantAccess(employeeId, resourceId, permission)`
- `revokeAccess(employeeId, resourceId, permission)`
- `retrieveAccess(employeeId, resourceId)`
- `retrieveResources(employeeId)`

---

### Design Role

- Acts as the **single source of truth** for permissions
- Keeps access control logic **centralized**

---

### **EmployeeResourceProxy**

Acts as a controlled access layer between employees and resources.

---

### Responsibilities

- Intercepts resource access requests
- Queries `ResourceManager` for permission checks
- Grants or denies access based on authorization result

---

### Design Pattern

- **Proxy Pattern**

---

### Why It Exists

- Prevents direct access to resources
- Enforces authorization consistently
- Keeps resource classes clean

---

## 3️⃣ Interaction Flow (High-Level)

### Accessing a Resource

```text
Employee → EmployeeResourceProxy → ResourceManager → Resource
```

