import java.util.*;
import java.util.concurrent.*;

enum AlertType {
    P0,
    P1,
    P2
}

enum ChannelType {
    EMAIL,
    SMS,
    PUSH
}

class Event {

    private String id;
    private String type;
    private Map<String, Object> payload;

    public Event(String id, String type, Map<String, Object> payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }
}

class Alert {

    String alertId;
    String message;
    AlertType alertType;
    Event event;
    List<ChannelType> channels;

    public Alert(String id, Event event, String message, List<ChannelType> channels) {
        this.alertId = id;
        this.event = event;
        this.message = message;
        this.channels = channels;
    }

    public String getMessage() {
        return message;
    }

    public List<ChannelType> getChannels() {
        return channels;
    }
}

interface NotificationChannel {
    public ChannelType getChannelType();
    public void send(Alert alert);
}

class EmailChannel implements NotificationChannel {

    public ChannelType getChannelType() {
        return ChannelType.EMAIL;
    }

    public void send(Alert alert) {
        System.out.println(Thread.currentThread().getName()
                + " -> Sending EMAIL: " + alert.getMessage());
    }
}

class SMSChannel implements NotificationChannel {

    public ChannelType getChannelType() {
        return ChannelType.SMS;
    }

    public void send(Alert alert) {
        System.out.println(Thread.currentThread().getName()
                + " -> Sending SMS: " + alert.getMessage());
    }
}


class PushChannel implements NotificationChannel {

    public ChannelType getChannelType() {
        return ChannelType.PUSH;
    }

    public void send(Alert alert) {
        System.out.println(Thread.currentThread().getName()
                + " -> Sending PUSH: " + alert.getMessage());
    }
}

class NotificationDispatcher {
    Map<ChannelType, NotificationChannel> channels = new HashMap<>();

    public NotificationDispatcher(List<NotificationChannel> channelList) {

        for (NotificationChannel ch : channelList) {
            channels.put(ch.getChannelType(), ch);
        }
    }

    public void dispatch(Alert alert) throws Exception {
        for(ChannelType channelType : alert.getChannels()) {
            try {
                if(channels.containsKey(channelType)) {
                    channels.get(channelType).send(alert);
                }
            }
            catch(Exception e) {
                throw new Exception("failed to send alert notification");
            }
        }
    }
}

class AlertQueue {

    private BlockingQueue<Alert> queue = new LinkedBlockingQueue<>();

    public void publish(Alert alert) throws InterruptedException {
        queue.put(alert);
    }

    public Alert consume() throws InterruptedException {
        return queue.take();
    }
}

class AlertWorker implements Runnable {

    private AlertQueue queue;
    private NotificationDispatcher dispatcher;

    public AlertWorker(AlertQueue queue, NotificationDispatcher dispatcher) {
        this.queue = queue;
        this.dispatcher = dispatcher;
    }

    public void run() {

        while (true) {

            try {

                Alert alert = queue.consume();

                dispatcher.dispatch(alert);

            } catch (Exception e) {
                System.out.println("Worker error: " + e.getMessage());
            }
        }
    }
}

class AlertProcessor {

    private AlertQueue queue;

    public AlertProcessor(AlertQueue queue) {
        this.queue = queue;
    }

    public void process(Event event) throws InterruptedException {
        Alert alert = buildAlert(event);
        queue.publish(alert);
    }

    private Alert buildAlert(Event event) {

        return new Alert(
                UUID.randomUUID().toString(),
                event,
                "Alert triggered for event: " + event.getType(),
                List.of(ChannelType.EMAIL, ChannelType.SMS, ChannelType.PUSH)
        );
    }
}

class AlertingSystem {
    public static void main(String[] args) {
        
        AlertQueue alertQueue = new AlertQueue();
        AlertProcessor alertProcessor = new AlertProcessor(alertQueue);
        NotificationChannel emailChannel = new EmailChannel();
        NotificationChannel SMSChannel = new SMSChannel();
        NotificationChannel pushChannel = new PushChannel();
        NotificationDispatcher notificationDispatcher = new NotificationDispatcher(List.of(emailChannel, SMSChannel, pushChannel));

        int threadCount = 3;
        for(int index = 1 ; index <= threadCount ; index++) {
            Thread worker = new Thread(new AlertWorker(alertQueue, notificationDispatcher), "Worker-" + index);
            worker.start();
        }

        for (int i = 1; i <= 5; i++) {

            Event event = new Event(
                    String.valueOf(i),
                    "SERVER_DOWN_" + i,
                    Map.of("server", "prod-" + i)
            );
            try {
                alertProcessor.process(event);
            }
            catch(Exception e) {
                System.err.println("alerting system failed " + e);
            }
        }

    }
}
