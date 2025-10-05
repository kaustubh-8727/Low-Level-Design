# 🧵 In-Memory Message Streaming System (Java)

This project is a **Low-Level Design (LLD)** implementation of a simplified **in-memory message streaming system**, inspired by Kafka’s core architecture — including **topics, partitions, producers, consumers, and message notifications**.

The system supports multiple producers and consumers, maintains message order within partitions, and demonstrates thread-safe message queues using **`LinkedBlockingQueue`**.

---

## 🚀 Features

- 🧩 **Topics and Partitions**
  - Each topic can have multiple partitions.
  - Messages within a partition are strictly ordered.

- 👨‍💻 **Producers**
  - Producers can publish messages to topics.
  - Partition selection is random (can be customized later).

- 👥 **Consumers**
  - Consumers are registered and assigned to partitions.
  - Notification service delivers messages to assigned consumers.

- ⚙️ **Thread-Safe Queues**
  - Uses `LinkedBlockingQueue` to ensure safe concurrent message passing.

- 🔔 **Notification Service**
  - Handles partition-to-consumer assignment.
  - Simulates delivery of messages from partitions to consumers.

---

## 🧠 Architecture Overview

+---------------------------+
| TopicService |
| └── manages Topics |
| └── each has Partitions (Queue<Message>) |
+---------------------------+
▲
│
+---------------------------+
| ProducerService |
| └── sends Messages to |
| Topic partitions |
+---------------------------+
│
▼
+---------------------------+
| NotificationService |
| └── assigns Consumers |
| to Partitions |
| └── notifies Consumers |
+---------------------------+
│
▼
+---------------------------+
| ConsumerService |
| └── receives messages |
+---------------------------+


## 🧩 Class Responsibilities

| Class | Responsibility |
|--------|----------------|
| **Message** | Represents a message with ID, content, and timestamp. |
| **Partition** | Thread-safe message queue maintaining order. |
| **Topic** | Contains multiple partitions. |
| **TopicService** | Creates and manages topics. |
| **Producer** | Represents a message producer assigned to a topic. |
| **ProducerService** | Handles message publishing and partition selection. |
| **Consumer** | Processes incoming messages. |
| **ConsumerService** | Registers consumers and manages delivery. |
| **NotificationService** | Assigns consumers to partitions and triggers notifications. |
| **PartitionChooser** | Chooses which partition to publish messages to. |
| **KafkaLowLevelDesign (Main)** | Demonstrates the end-to-end workflow. |


## Sample Output

```

Added new partition: partition_a1719b3f-6ee7-46b7-895c-11f266d3e2b6
Added new partition: partition_f929aa15-e510-4379-b599-a89179c8c5bd
Added new partition: partition_1400419c-808a-4e56-8972-b1154e82ef98
Added new partition: partition_ed69bf91-b59b-404b-98aa-2ea4ac0cedd9

Topic created: topic_c799eedb-f671-4f8a-b9de-2ccd22b2abba with 4 partitions

Producer registered: producer_2e5fe22e-8b66-4a97-920b-81726bb0ee7c for topic: topic_c799eedb-f671-4f8a-b9de-2ccd22b2abba

Consumer registered: consumer1_012cdfbd-8efe-4ef7-a35c-587fa1272ce7
Consumer registered: consumer2_b668bc4a-e66f-403d-8732-b03bc6aecd4a
Consumer registered: consumer3_80309e9f-b067-4535-9f5a-9a9820a2c0f2

Assigned Consumer consumer1_012cdfbd-8efe-4ef7-a35c-587fa1272ce7 to Partition partition_a1719b3f-6ee7-46b7-895c-11f266d3e2b6
Assigned Consumer consumer2_b668bc4a-e66f-403d-8732-b03bc6aecd4a to Partition partition_f929aa15-e510-4379-b599-a89179c8c5bd
Assigned Consumer consumer3_80309e9f-b067-4535-9f5a-9a9820a2c0f2 to Partition partition_1400419c-808a-4e56-8972-b1154e82ef98
Assigned Consumer consumer1_012cdfbd-8efe-4ef7-a35c-587fa1272ce7 to Partition partition_ed69bf91-b59b-404b-98aa-2ea4ac0cedd9

PartitionChooser picked partition index: 0

[Producer producer_2e5fe22e-8b66-4a97-920b-81726bb0ee7c] sending message to Partition partition_a1719b3f-6ee7-46b7-895c-11f266d3e2b6

[Partition partition_a1719b3f-6ee7-46b7-895c-11f266d3e2b6] Enqueuing message: [Message ID=1, message='Hello, this is a message from producer producer_2e5fe22e-8b66-4a97-920b-81726bb0ee7c', createdAt=Sun Oct 05 14:45:07 IST 2025]

[Producer producer_2e5fe22e-8b66-4a97-920b-81726bb0ee7c] Message successfully sent to queue
