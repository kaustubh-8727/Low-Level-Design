import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

enum MessageStatus {
    INITIALIZED,
    SEND,
    FAILED,
    PENDING
}

class Message {
    String messageId;
    String message;
    MessageStatus status;
    Date createdAt;

    public Message(String messageId, String message) {
        this.messageId = messageId;
        this.message = message;
        this.status = MessageStatus.INITIALIZED;
        this.createdAt = new Date();
    }
}

class Producer {
    String producerId;
    String name;
    Date createdAt;

    public Producer(String producerId, String name) {
        this.producerId = producerId;
        this.name = name;
        this.createdAt = new Date();
    }
}

class Topic {
    private final String topicId;
    private final String name;
    private final CopyOnWriteArrayList<Subscriber> subscribers;

    public Topic(String topicId, String name) {
        this.topicId = topicId;
        this.name = name;
        this.subscribers = new CopyOnWriteArrayList<>();
    }

    public void subscribe(Subscriber subscriber) {
        if (subscriber != null && !subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getName() {
        return name;
    }
}

class Subscriber {
    String subscriberId;
    String name;

    public Subscriber(String subscriberId, String name) {
        this.subscriberId = subscriberId;
        this.name = name;
    }

    public void onMessage(Message message) {
        System.out.println("Subscriber " + subscriberId + " received message: " + message.message);
    }
}

class TopicService {

    ConcurrentHashMap<String, Topic> topicMap = new ConcurrentHashMap<>();

    public Topic createTopic(String name) {
        String id = UUID.randomUUID().toString();
        Topic topic = new Topic(id, name);
        topicMap.put(id, topic);
        return topic;
    }

    public void deleteTopic(String topicId) {
        topicMap.remove(topicId);
    }

    public Topic getTopic(String topicId) {
        return topicMap.get(topicId);
    }

    public void subscribe(String topicId, Subscriber subscriber) {
        Topic topic = topicMap.get(topicId);
        if (topic != null) {
            topic.subscribe(subscriber);
        }
    }
}

interface IProducerService {
    void sendMessage(Message message, String producerId, String topicId);
}

class ProducerService implements IProducerService {

    MessageQueue messageQueue;

    public ProducerService(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void sendMessage(Message message, String producerId, String topicId) {
        Event event = new Event(producerId, topicId, message);
        try {
            messageQueue.publish(event);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.err.println("Failed to publish message");
        }
    }
}

interface ISubscriberservice {
    void notifySubscribers(Message message, String topicId);
}

class SubscriberService implements ISubscriberservice {

    TopicService topicService;

    public SubscriberService(TopicService topicService) {
        this.topicService = topicService;
    }

    @Override
    public void notifySubscribers(Message message, String topicId) {
        Topic topic = topicService.getTopic(topicId);
        if (topic == null) {
            return;
        }

        List<Subscriber> subscribers = topic.getSubscribers();
        for (Subscriber subscriber : subscribers) {
            try {
                subscriber.onMessage(message);
            } catch (Exception e) {
                System.err.println("Failed to notify subscriber: " + subscriber.subscriberId);
            }
        }
    }
}

class Event {
    Message message;
    String producerId;
    String topicId;

    public Event(String producerId, String topicId, Message message) {
        this.producerId = producerId;
        this.topicId = topicId;
        this.message = message;
    }
}

class MessageQueue {
    BlockingQueue<Event> messageQueue = new LinkedBlockingQueue<>();

    public void publish(Event event) throws InterruptedException {
        messageQueue.put(event);
    }

    public Event consume() throws InterruptedException {
        return messageQueue.take();
    }
}

class MessageWorkerQueue implements Runnable {

    MessageQueue messageQueue;
    SubscriberService subscriberService;

    public MessageWorkerQueue(MessageQueue messageQueue, SubscriberService subscriberService) {
        this.messageQueue = messageQueue;
        this.subscriberService = subscriberService;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Event event = messageQueue.consume();
                event.message.status = MessageStatus.SEND;
                subscriberService.notifySubscribers(event.message, event.topicId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Failed to process message");
            }
        }
    }
}

public class PublisherSubscriberSystem {
    public static void main(String[] args) {

        Producer producer = new Producer("producer-1", "producer-name-1");

        TopicService topicService = new TopicService();
        Topic topic_1 = topicService.createTopic("topic-1");
        Topic topic_2 = topicService.createTopic("topic-2");

        MessageQueue messageQueue = new MessageQueue();
        ProducerService producerService = new ProducerService(messageQueue);

        SubscriberService subscriberService = new SubscriberService(topicService);

        Subscriber subscriber_1 = new Subscriber("subscriber-1", "subscriber-1");
        Subscriber subscriber_2 = new Subscriber("subscriber-1", "subscriber-2");

        topicService.subscribe(topic_1.getTopicId(), subscriber_1);
        topicService.subscribe(topic_2.getTopicId(), subscriber_2);

        ExecutorService worker = Executors.newFixedThreadPool(3);
        worker.submit(new MessageWorkerQueue(messageQueue, subscriberService));
        worker.submit(new MessageWorkerQueue(messageQueue, subscriberService));
        worker.submit(new MessageWorkerQueue(messageQueue, subscriberService));

        Message message_1 = new Message("message-1", "this is message-1");
        producerService.sendMessage(message_1, producer.producerId, topic_1.getTopicId());

        Message message_2 = new Message("message-2", "this is message-2");
        producerService.sendMessage(message_2, producer.producerId, topic_1.getTopicId());

    }
}
