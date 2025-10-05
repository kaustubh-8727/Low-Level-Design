import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

class Message {
    long messageId;
    String message;
    Date createdAt;

    public Message(long messageId, String message, Date createdAt) {
        this.messageId = messageId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getMessageId() { return message; }
    public String getmessage() { return message; }
    public Date getTimestamp() { return createdAt; }

    @Override
    public String toString() {
        return "[Message ID=" + messageId + ", message='" + message + "', createdAt=" + createdAt + "]";
    }
}

class Producer {
    String producerId;
    String producerName;
    Topic assignedTopic;

    public Producer(String producerName, Topic assignedTopic) {
        this.producerName = producerName;
        this.assignedTopic = assignedTopic;
        this.producerId = UUID.randomUUID().toString();
    }
}

class Consumer {
    String consumerId;
    String consumerName;

    public Consumer(String consumerName) {
        this.consumerName = consumerName;
        this.consumerId = UUID.randomUUID().toString();
    }

    public void onMessage(Message message) {
        System.out.println("[Consumer: " + consumerName + "] received => " + message);
    }
}

class Partition {
    private final String partitionId;
    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    private final AtomicLong offset = new AtomicLong(0);

    public Partition() {
        this.partitionId = "partition_" + UUID.randomUUID().toString();
    }

    public void put(Message m) throws InterruptedException {
        System.out.println("[Partition " + partitionId + "] Enqueuing message: " + m);
        queue.put(m);
        offset.incrementAndGet();
    }

    public Message take() throws InterruptedException {
        Message msg = queue.take();
        System.out.println("[Partition " + partitionId + "] Dequeued message: " + msg);
        return msg;
    }

    public String getId() { return partitionId; }
    public long offset() { return offset.get(); }
}

class Topic {
    String topicId;
    String topicName;
    List<Partition> partitions;

    public Topic(String topicName) {
        this.topicName = topicName;
        this.partitions = new ArrayList<>();
        this.topicId = UUID.randomUUID().toString();
    }

    public void addpartition() {
        Partition p = new Partition();
        partitions.add(p);
        System.out.println("Added new partition: " + p.getId());
    }

    public List<Partition> getPartitions() {
        return this.partitions;
    }

    public Partition getPartition(int id) { return partitions.get(id); }
    public int partitionCount() { return partitions.size(); }
}

class TopicService {
    ConcurrentHashMap<String, Topic> topics;

    public TopicService() {
        this.topics = new ConcurrentHashMap<>();
    }

    public void createTopic(String topicName, int partitionCount) throws Exception {
        if (topics.containsKey(topicName)) {
            throw new Exception("Topic name already exists");
        }

        Topic topic = new Topic(topicName);
        for (int i = 0; i < partitionCount; i++) {
            topic.addpartition();
        }
        this.topics.put(topicName, topic);
        System.out.println("Topic created: " + topicName + " with " + partitionCount + " partitions");
    }

    public Partition getPartition(String topicName, int index) {
        return topics.get(topicName).getPartition(index);
    }

    public Optional<Topic> getTopic(String name) { return Optional.ofNullable(topics.get(name)); }
}

class ProducerService {
    ConcurrentHashMap<String, Producer> producers;
    private final AtomicLong messageIdGenerator = new AtomicLong(0);
    private final TopicService topicService;
    private final PartitionChooser partitionChooser;

    public ProducerService(TopicService topicService) {
        this.producers = new ConcurrentHashMap<>();
        this.topicService = topicService;
        this.partitionChooser = new PartitionChooser();
    }

    public void registerProducer(String producerName, String topicName) throws Exception {
        if (producers.containsKey(producerName)) {
            throw new Exception("Producer name already exists");
        }

        Topic topic = topicService.getTopic(topicName)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topicName));
        Producer producer = new Producer(producerName, topic);
        this.producers.put(producerName, producer);
        System.out.println("Producer registered: " + producerName + " for topic: " + topicName);
    }

    public void sendMessage(String newMessage, String producerName, String topicName) throws InterruptedException, Exception {
        if (!producers.containsKey(producerName)) {
            throw new Exception("Producer does not exist");
        }

        Producer producer = producers.get(producerName);
        Topic topic = topicService.topics.get(topicName);
        Message message = new Message(messageIdGenerator.incrementAndGet(), newMessage, new Date());

        int partitionIndex = this.partitionChooser.choosePartition(topic);
        Partition partition = this.topicService.getPartition(topicName, partitionIndex);

        System.out.println("[Producer " + producerName + "] sending message to Partition " + partition.getId());
        partition.put(message);

        // simulate notifying consumers (if notification system hooked up)
        System.out.println("[Producer " + producerName + "] Message successfully sent to queue");
    }
}

class PartitionChooser {
    public int choosePartition(Topic topic) {
        int pcount = topic.partitionCount();
        int chosen = new Random().nextInt(pcount);
        System.out.println("PartitionChooser picked partition index: " + chosen);
        return chosen;
    }
}

class ConsumerService {
    ConcurrentHashMap<String, Consumer> consumers = new ConcurrentHashMap<>();

    public void registerConsumer(String consumerName) throws Exception {
        if (consumers.containsKey(consumerName)) {
            throw new Exception("Consumer name already exists");
        }
        Consumer consumer = new Consumer(consumerName);
        this.consumers.put(consumerName, consumer);
        System.out.println("Consumer registered: " + consumerName);
    }

    public void notifyConsumer(Consumer consumer, Message message) throws Exception {
        System.out.println("Notifying consumer: " + consumer.consumerName);
        consumer.onMessage(message);
    }

    public Optional<Consumer> get(String id) { return Optional.ofNullable(consumers.get(id)); }
}

class NotificationService {
    private final TopicService topicService;
    private final ConsumerService consumerService;
    private final Map<String, List<Consumer>> partitionConsumerMap = new ConcurrentHashMap<>();

    public NotificationService(TopicService topicService, ConsumerService consumerService) {
        this.topicService = topicService;
        this.consumerService = consumerService;
    }

    public void assignConsumersToTopic(String topicName, List<String> consumerIds) {
        Topic topic = topicService.getTopic(topicName)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topicName));

        if (consumerIds.isEmpty()) 
            throw new IllegalArgumentException("Need at least one consumer to start.");

        List<Partition> partitions = topic.getPartitions();

        for (int i = 0; i < partitions.size(); i++) {
            Partition partition = partitions.get(i);
            String consumerId = consumerIds.get(i % consumerIds.size());
            Consumer consumer = consumerService.get(consumerId)
                    .orElseThrow(() -> new IllegalArgumentException("Consumer not registered: " + consumerId));

            partitionConsumerMap
                .computeIfAbsent(partition.getId(), k -> new CopyOnWriteArrayList<>())
                .add(consumer);

            System.out.println("Assigned Consumer " + consumer.consumerName + " to Partition " + partition.getId());
        }
    }

    public void notifyConsumers(Message message, String partitionId) throws Exception {
        List<Consumer> consumers = partitionConsumerMap.get(partitionId);
        if (consumers == null || consumers.isEmpty()) {
            throw new IllegalArgumentException("Invalid or unassigned partition: " + partitionId);
        }

        for (Consumer consumer : consumers) {
            consumerService.notifyConsumer(consumer, message);
        }
    }
}

class KafkaLowLevelDesign {
    public static void main(String[] args) throws InterruptedException {
        try {

            // initialize services
            TopicService topicService = new TopicService();
            ProducerService producerService = new ProducerService(topicService);
            ConsumerService consumerService = new ConsumerService();
            NotificationService notificationService = new NotificationService(topicService, consumerService);

            // create topic
            String topicName = "topic_" + UUID.randomUUID();
            int partitionCount = 4;
            topicService.createTopic(topicName, partitionCount);

            // create producer
            String producerName = "producer_" + UUID.randomUUID();
            producerService.registerProducer(producerName, topicName);

            // create consumers
            String consumerName1 = "consumer1_" + UUID.randomUUID();
            consumerService.registerConsumer(consumerName1);

            String consumerName2 = "consumer2_" + UUID.randomUUID();
            consumerService.registerConsumer(consumerName2);

            String consumerName3 = "consumer3_" + UUID.randomUUID();
            consumerService.registerConsumer(consumerName3);

            // assign consumers to topic
            notificationService.assignConsumersToTopic(topicName,
                    Arrays.asList(consumerName1, consumerName2, consumerName3));

            // producer sends a message
            String message = "Hello, this is a message from producer " + producerName;
            producerService.sendMessage(message, producerName, topicName);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
