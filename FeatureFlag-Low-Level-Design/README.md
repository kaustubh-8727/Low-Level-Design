# Feature Flag System - Low Level Design

---

# Problem Statement

Design a Feature Flag (Feature Toggle) System that allows applications to dynamically enable or disable features without redeploying code.

The system should support:
- Global feature enable/disable
- User-specific feature rollout
- Percentage-based rollout
- Dynamic updates without restarting applications
- Multiple applications/services using the same feature flag system

# Functional Requirements

1. Create and manage feature flags.
2. Enable or disable a feature globally.
3. Enable features for:
   - Specific users
   - Percentage rollout
4. Evaluate whether a feature is enabled for a given user.
5. Support multiple applications/services.
6. Reflect updates dynamically without restarting applications.

---

# High Level Architecture

```text
                +----------------------+
                |  Client Application  |
                +----------+-----------+
                           |
                           v
                +----------------------+
                | FeatureFlagService   |
                +----------+-----------+
                           |
        -----------------------------------------
        |                                       |
        v                                       v
+-------------------+              +----------------------+
| InMemory Store    |              | Persistent DB Store  |
+-------------------+              +----------------------+
```

# Core Classes

## 1. UserContext

Represents the user information required for feature evaluation.

### Responsibilities
- Stores user-related information
- Used during feature evaluation
- Supports flexible user attributes

## 2. FeatureFlag

Represents a feature toggle configuration.

### Responsibilities
- Maintains feature configuration
- Stores rollout rules
- Stores targeting information

## 3. FeatureFlagStore (Interface)

Abstract storage layer for feature flags.

### Responsibilities
- Store and retrieve feature flags
- Decouple storage implementation from business logic

---

## 4. InMemoryFeatureFlagStore

In-memory implementation of FeatureFlagStore.

### Responsibilities
- Fast lookup
- Useful for testing/demo
- Uses ConcurrentHashMap internally

---

## 5. FeatureFlagService

Main business logic layer of the system.

### Responsibilities
- Evaluate feature access
- Manage rollout logic
- Apply targeting rules
- Enable/disable features

---

# Feature Evaluation Flow

```text
                isEnabled(flag, user)
                           |
                           v
             +------------------------+
             | Is feature globally ON?|
             +-----------+------------+
                         |
               YES ------+-------> TRUE
                         |
                        NO
                         |
                         v
          +-----------------------------+
          | Is user explicitly enabled? |
          +-------------+---------------+
                        |
              YES ------+-------> TRUE
                        |
                       NO
                        |
                        v
          +-----------------------------+
          | Do targeting rules match?   |
          +-------------+---------------+
                        |
              NO -------+-------> FALSE
                        |
                       YES
                        |
                        v
          +-----------------------------+
          | Percentage rollout check    |
          +-------------+---------------+
                        |
                        v
                    TRUE/FALSE
```

# Design Patterns Used

## 1. Strategy Pattern

Used in rollout/evaluation logic.

### Different rollout strategies can be added
- User-based rollout
- Country-based rollout
- Percentage rollout
- Time-based rollout

---

## 2. Repository Pattern

Used in FeatureFlagStore abstraction.

### Benefits
- Decouples business logic from storage
- Easy DB migration
- Easy testing/mocking

---

## 3. Dependency Injection

FeatureFlagService depends on FeatureFlagStore abstraction.

---

## 4. Factory Pattern (Possible Extension)

Can be used to create:
- Different store implementations
- Different rollout strategies

---

## 5. Thread Safety

InMemoryFeatureFlagStore uses:

