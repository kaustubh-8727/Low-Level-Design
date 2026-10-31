
# LLD: WhatsApp / Chat Application

## 1. Problem Statement

Design a simplified WhatsApp-like messaging application that allows users to communicate through one-to-one conversations and group chats.

The system should support:

- User registration and management.
- One-to-one conversations.
- Group creation and management.
- Sending and receiving messages.
- Tracking message delivery and read status.
- Viewing conversation history.
- Managing group members and administrators.
- Editing and deleting messages.

### Message Status

- `SENT`: Message successfully accepted by the messaging system.
- `DELIVERED`: Message delivered to the recipient's device.
- `READ`: Recipient has read the message.

For group conversations, delivery and read status should be tracked independently for each recipient.

---

## 2. Classes and Entities

### Core Entities

| Class | Responsibility |
|---|---|
| `User` | Maintains user details such as ID, name, phone number and profile picture. |
| `Chat` | Represents a conversation and maintains its participants and messages. |
| `ChatParticipant` | Maintains participant-specific information such as joined timestamp and last read message ID. |
| `Message` | Maintains message details such as ID, sender, content, timestamp and type. |
| `Group` | Maintains group details such as name, members and administrators. |
| `MessageReceipt` | Tracks individual message delivery and read status for each recipient. |
| `MessageMetadata` | Maintains additional message-related information. |

### Enums

- `MessageType`: TEXT, IMAGE, VIDEO, DOCUMENT.
- `MessageStatus`: SENT, DELIVERED, READ.
- `ChatType`: ONE_TO_ONE, GROUP.

---

## 3. Storage / Repository Layer

Use the Repository Pattern to abstract data persistence and allow different storage implementations.

| Interface | Implementation |
|---|---|
| `UserRepository` | `UserRepositoryInMemory` |
| `ChatRepository` | `ChatRepositoryInMemory` |
| `MessageRepository` | `MessageRepositoryInMemory` |
| `GroupRepository` | `GroupRepositoryInMemory` |
| `MessageReceiptRepository` | `MessageReceiptRepositoryInMemory` |

---

## 4. Services

| Service / Class | Responsibility |
|---|---|
| `UserManagementService` | Handles user registration, profile management and blocking/unblocking users. |
| `ChatService` | Handles one-to-one chat creation and conversation retrieval. |
| `GroupService` | Handles group creation, member management and admin permissions. |
| `MessageService` | Validates, creates and persists messages, then publishes them to the message queue. |
| `MessageWorker` | Consumes messages from the queue and delegates them to the appropriate message sender. |
| `MessageSender` | Interface for message delivery. |
| `MessageSenderOneToOne` | Handles one-to-one message delivery. |
| `MessageSenderGroupChat` | Handles group message delivery and fan-out to group members. |
| `MessageQueue` | Abstracts asynchronous message processing using a blocking queue. |

---

## 5. Design Patterns Used

### 1. Strategy Pattern

Used for message delivery.

- `MessageSender` acts as the strategy interface.
- `MessageSenderOneToOne` implements one-to-one delivery.
- `MessageSenderGroupChat` implements group message delivery.

The appropriate strategy is selected based on the chat type.

### 2. Repository Pattern

Used to abstract data persistence.

- Repository interfaces define storage operations.
- In-memory and database implementations can be provided independently.
- Services depend on repository interfaces rather than concrete storage implementations.

### 3. Producer-Consumer Pattern

Used for asynchronous message processing.

- `MessageService` publishes messages to `MessageQueue`.
- `MessageWorker` consumes messages from the queue.
- The worker delegates message delivery to the appropriate sender.

---

## 6. Message Flow

### One-to-One Messaging

1. User A sends a message to User B.
2. `MessageService` validates the sender and recipient.
3. A unique message ID is generated.
4. The message is persisted using `MessageRepository`.
5. The message is published to `MessageQueue`.
6. `MessageWorker` consumes the message.
7. Worker selects `MessageSenderOneToOne`.
8. The message is delivered to User B.
9. Message receipt status is updated.

### Group Messaging

1. A group member sends a message.
2. `MessageService` validates the sender's group membership.
3. A unique message ID is generated.
4. The message is persisted.
5. The message is published to `MessageQueue`.
6. `MessageWorker` consumes the message.
7. Worker selects `MessageSenderGroupChat`.
8. The message is delivered to all eligible group members, excluding the sender.
9. Individual message receipts are maintained for each recipient.

### Message Status Flow

```text
SENT
  |
  v
DELIVERED
  |
  v
READ
```

For group messages, each recipient has an independent delivery and read status.

---

## 7. Class Structure

```text
Entities
 |
 +-- User
 |
 +-- Chat
 |    |
 |    +-- ChatParticipant
 |
 +-- Group
 |
 +-- Message
 |    |
 |    +-- MessageMetadata
 |
 +-- MessageReceipt
 |
 +-- Enums
      |
      +-- MessageType
      +-- MessageStatus
      +-- ChatType

Repositories
 |
 +-- UserRepository
 +-- ChatRepository
 +-- MessageRepository
 +-- GroupRepository
 +-- MessageReceiptRepository

Services
 |
 +-- UserManagementService
 +-- ChatService
 +-- GroupService
 +-- MessageService

Messaging
 |
 +-- MessageQueue
 +-- MessageWorker
 +-- MessageSender
      |
      +-- MessageSenderOneToOne
      +-- MessageSenderGroupChat
```
