
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


enum ShipmentStatus {
    CREATED,
    PENDING_ASSIGNMENT,
    ASSIGNED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELIVERY_FAILED,
    CANCELLED
}

enum DeliveryStatus {
    ASSIGNED,
    PICKED_UP,
    OUT_FOR_DELIVERY,
    DELIVERY_FAILED,
    DELIVERED,
    CANCELLED
}

enum ShipmentPriority {
    PRIME(0),
    STANDARD(1);

    private final int rank;

    ShipmentPriority(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}

class Address {
    private final String city;
    private final String street;
    private final double latitude;
    private final double longitude;

    public Address(String city, String street,
                   double latitude, double longitude) {
        this.city = city;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return street + ", " + city;
    }
}

class ProductItem {
    private final String productId;
    private final String name;
    private final int quantity;

    public ProductItem(String productId, String name, int quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name + " x " + quantity;
    }
}

class Order {
    private final String orderId;
    private final String customerId;
    private final List<ProductItem> items;
    private final Address deliveryAddress;
    private final boolean primeOrder;
    private final LocalDateTime createdAt;


    public Order(String orderId, String customerId,
                 List<ProductItem> items, Address deliveryAddress,
                 boolean primeOrder) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.primeOrder = primeOrder;
        this.createdAt = LocalDateTime.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<ProductItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public boolean isPrimeOrder() {
        return primeOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

class Shipment {
    private final String shipmentId;
    private final String orderId;
    private final List<ProductItem> items;
    private final Address address;
    private final ShipmentPriority priority;
    private final LocalDateTime createdAt;

    private volatile ShipmentStatus status =
            ShipmentStatus.CREATED;

    public Shipment(String shipmentId, Order order) {
        this.shipmentId = shipmentId;
        this.orderId = order.getOrderId();
        this.items = new ArrayList<>(order.getItems());
        this.address = order.getDeliveryAddress();
        this.priority = order.isPrimeOrder()
                ? ShipmentPriority.PRIME
                : ShipmentPriority.STANDARD;
        this.createdAt = LocalDateTime.now();
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Address getAddress() {
        return address;
    }

    public ShipmentPriority getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Shipment{id='" + shipmentId
                + "', order='" + orderId
                + "', priority=" + priority
                + ", status=" + status + "}";
    }
}

class DeliveryAgent {
    private final String agentId;
    private final String name;
    private final double rating;
    private final double latitude;
    private final double longitude;

    private int activeDeliveryCount;
    private boolean available = true;

    public DeliveryAgent(String agentId, String name,
                         double rating, double latitude,
                         double longitude) {
        this.agentId = agentId;
        this.name = name;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public synchronized boolean isAvailable() {
        return available;
    }

    public synchronized int getActiveDeliveryCount() {
        return activeDeliveryCount;
    }

    public synchronized boolean reserve() {
        if (!available) {
            return false;
        }

        available = false;
        activeDeliveryCount++;
        return true;
    }

    public synchronized void release() {
        if (activeDeliveryCount > 0) {
            activeDeliveryCount--;
        }

        available = true;
    }

    @Override
    public String toString() {
        return name + " (" + agentId + ", rating=" + rating + ")";
    }
}

class DeliveryAttempt {
    private final int attemptNumber;
    private final LocalDateTime attemptedAt;
    private final boolean successful;
    private final String reason;

    public DeliveryAttempt(int attemptNumber, boolean successful,
                           String reason) {
        this.attemptNumber = attemptNumber;
        this.attemptedAt = LocalDateTime.now();
        this.successful = successful;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Attempt #" + attemptNumber
                + ", successful=" + successful
                + ", reason=" + reason
                + ", at=" + attemptedAt;
    }
}

class Delivery {
    private final String deliveryId;
    private final String shipmentId;
    private final List<DeliveryAttempt> attempts = new ArrayList<>();

    private DeliveryAgent agent;
    private DeliveryStatus status = DeliveryStatus.ASSIGNED;
    private int attemptCount;

    public Delivery(String deliveryId, String shipmentId,
                    DeliveryAgent agent) {
        this.deliveryId = deliveryId;
        this.shipmentId = shipmentId;
        this.agent = agent;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public synchronized DeliveryAgent getAgent() {
        return agent;
    }

    public synchronized DeliveryStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public synchronized int getAttemptCount() {
        return attemptCount;
    }

    public synchronized void addAttempt(boolean successful, String reason) {
        attemptCount++;
        attempts.add(new DeliveryAttempt(
                attemptCount, successful, reason));
    }

    public synchronized void reassignAgent(DeliveryAgent newAgent) {
        this.agent = newAgent;
        this.status = DeliveryStatus.ASSIGNED;
    }

    public synchronized List<DeliveryAttempt> getAttempts() {
        return new ArrayList<>(attempts);
    }
}

interface DeliveryAssignStrategy {
    DeliveryAgent assign(
            Shipment shipment,
            List<DeliveryAgent> candidates
    );
}

class AssignByHighestRating implements DeliveryAssignStrategy {
    @Override
    public DeliveryAgent assign(
            Shipment shipment,
            List<DeliveryAgent> candidates) {

        return candidates.stream()
                .filter(DeliveryAgent::isAvailable)
                .max(Comparator
                        .comparingDouble(DeliveryAgent::getRating)
                        .thenComparing(
                                DeliveryAgent::getAgentId,
                                Comparator.reverseOrder()))
                .orElse(null);
    }
}

class AssignByLessOrders implements DeliveryAssignStrategy {
    @Override
    public DeliveryAgent assign(
            Shipment shipment,
            List<DeliveryAgent> candidates) {

        return candidates.stream()
                .filter(DeliveryAgent::isAvailable)
                .min(Comparator
                        .comparingInt(
                                DeliveryAgent::getActiveDeliveryCount)
                        .thenComparing(
                                DeliveryAgent::getAgentId))
                .orElse(null);
    }
}

class AssignByNearestDistance implements DeliveryAssignStrategy {
    @Override
    public DeliveryAgent assign(
            Shipment shipment,
            List<DeliveryAgent> candidates) {

        return candidates.stream()
                .filter(DeliveryAgent::isAvailable)
                .min(Comparator.comparingDouble(
                        agent -> distance(
                                shipment.getAddress().getLatitude(),
                                shipment.getAddress().getLongitude(),
                                agent.getLatitude(),
                                agent.getLongitude())))
                .orElse(null);
    }

    private double distance(double lat1, double lon1,
                            double lat2, double lon2) {
        double latDiff = lat1 - lat2;
        double lonDiff = lon1 - lon2;

        return Math.sqrt(
                latDiff * latDiff + lonDiff * lonDiff);
    }
}

class DeliveryEvent {
    private final String eventId;
    private final String deliveryId;
    private final String agentId;
    private final DeliveryStatus newStatus;
    private final String failureReason;
    private final LocalDateTime occurredAt;

    public DeliveryEvent(String eventId, String deliveryId,
                         String agentId, DeliveryStatus newStatus,
                         String failureReason) {
        this.eventId = eventId;
        this.deliveryId = deliveryId;
        this.agentId = agentId;
        this.newStatus = newStatus;
        this.failureReason = failureReason;
        this.occurredAt = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getAgentId() {
        return agentId;
    }

    public DeliveryStatus getNewStatus() {
        return newStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}

// ---------- Packaging Service ----------

class PackagingService {
    private final DeliveryService deliveryService;
    private final Map<String, Shipment> shipments =
            new ConcurrentHashMap<>();
    private final AtomicInteger shipmentSequence =
            new AtomicInteger(1);

    public PackagingService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public Shipment createShipment(Order order) {
        String shipmentId = "S" + shipmentSequence.getAndIncrement();

        Shipment shipment = new Shipment(shipmentId, order);
        shipment.setStatus(ShipmentStatus.PENDING_ASSIGNMENT);
        shipments.put(shipmentId, shipment);

        System.out.println("Shipment created: " + shipment);

        deliveryService.submitShipment(shipment);
        return shipment;
    }

    public Shipment getShipment(String shipmentId) {
        return shipments.get(shipmentId);
    }
}

// ---------- Delivery Service ----------

class DeliveryService {
    private static final int MAX_ATTEMPTS = 3;

    private final PriorityBlockingQueue<Shipment> shipmentQueue =
            new PriorityBlockingQueue<>(
                    11,
                    Comparator
                            .comparingInt(
                                    (Shipment s) ->
                                            s.getPriority().getRank())
                            .thenComparing(Shipment::getCreatedAt)
                            .thenComparing(Shipment::getShipmentId)
            );

    private final Map<String, Delivery> deliveries =
            new ConcurrentHashMap<>();

    private final Map<String, Shipment> shipments =
            new ConcurrentHashMap<>();

    private final Map<String, DeliveryAgent> agents =
            new ConcurrentHashMap<>();

    private final DeliveryEventQueue eventQueue;
    private final AtomicInteger deliverySequence =
            new AtomicInteger(1);

    private volatile boolean running = true;
    private Thread assignmentWorker;

    public DeliveryService() {
        this.eventQueue = new DeliveryEventQueue(this);
    }

    public void registerAgent(DeliveryAgent agent) {
        agents.put(agent.getAgentId(), agent);
    }

    public void submitShipment(Shipment shipment) {
        shipments.put(shipment.getShipmentId(), shipment);
        shipmentQueue.offer(shipment);
    }

    public Delivery getDelivery(String deliveryId) {
        return deliveries.get(deliveryId);
    }

    public DeliveryEventQueue getEventQueue() {
        return eventQueue;
    }

    public void start() {
        eventQueue.start();

        assignmentWorker = new Thread(
                this::processShipments,
                "shipment-assignment-worker"
        );

        assignmentWorker.start();
    }

    private void processShipments() {
        while (running) {
            try {
                Shipment shipment = shipmentQueue.take();

                DeliveryAssignStrategy strategy =
                        shipment.getPriority() == ShipmentPriority.PRIME
                                ? new AssignByHighestRating()
                                : new AssignByLessOrders();

                DeliveryAgent agent = reserveAgent(shipment, strategy);

                if (agent == null) {
                    shipment.setStatus(
                            ShipmentStatus.PENDING_ASSIGNMENT);

                    // No agent is currently available.
                    // Put the shipment back and retry later.
                    shipmentQueue.offer(shipment);
                    Thread.sleep(500);
                    continue;
                }

                String deliveryId =
                        "D" + deliverySequence.getAndIncrement();

                Delivery delivery = new Delivery(
                        deliveryId,
                        shipment.getShipmentId(),
                        agent
                );

                deliveries.put(deliveryId, delivery);
                shipment.setStatus(ShipmentStatus.ASSIGNED);

                System.out.println(
                        "Assigned " + shipment.getShipmentId()
                                + " to " + agent
                                + " using " + strategy.getClass()
                                .getSimpleName()
                );

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Reserve only if the agent is still available.
    // This avoids assigning the same agent concurrently.
    private DeliveryAgent reserveAgent(
            Shipment shipment,
            DeliveryAssignStrategy strategy) {

        List<DeliveryAgent> candidates =
                new ArrayList<>(agents.values());

        // A strategy proposes a candidate; reserve() confirms it.
        for (int i = 0; i < candidates.size(); i++) {
            DeliveryAgent selected =
                    strategy.assign(shipment, candidates);

            if (selected == null) {
                return null;
            }

            if (selected.reserve()) {
                return selected;
            }

            // The selected agent became unavailable.
            // Remove it from this local candidate list and retry.
            candidates.remove(selected);
        }

        return null;
    }

    // Called only by the event consumer.
    public synchronized void updateDeliveryStatus(
            DeliveryEvent event) {

        Delivery delivery = deliveries.get(event.getDeliveryId());

        if (delivery == null) {
            System.out.println("Unknown delivery: "
                    + event.getDeliveryId());
            return;
        }

        // Reject events submitted by an agent not assigned
        // to this delivery.
        if (!delivery.getAgent().getAgentId()
                .equals(event.getAgentId())) {
            System.out.println("Rejected event from wrong agent: "
                    + event.getEventId());
            return;
        }

        DeliveryStatus oldStatus = delivery.getStatus();
        DeliveryStatus newStatus = event.getNewStatus();

        if (!isValidTransition(oldStatus, newStatus)) {
            System.out.println("Invalid transition: "
                    + oldStatus + " -> " + newStatus);
            return;
        }

        Shipment shipment =
                shipments.get(delivery.getShipmentId());

        switch (newStatus) {
            case PICKED_UP:
                delivery.setStatus(DeliveryStatus.PICKED_UP);
                break;

            case OUT_FOR_DELIVERY:
                delivery.setStatus(
                        DeliveryStatus.OUT_FOR_DELIVERY);
                shipment.setStatus(
                        ShipmentStatus.OUT_FOR_DELIVERY);
                break;

            case DELIVERED:
                delivery.setStatus(DeliveryStatus.DELIVERED);
                delivery.addAttempt(true, "Delivered successfully");
                shipment.setStatus(ShipmentStatus.DELIVERED);
                delivery.getAgent().release();
                break;

            case DELIVERY_FAILED:
                handleFailedAttempt(delivery, shipment, event);
                break;

            case CANCELLED:
                delivery.setStatus(DeliveryStatus.CANCELLED);
                shipment.setStatus(ShipmentStatus.CANCELLED);
                delivery.getAgent().release();
                break;

            default:
                return;
        }

        System.out.println("Processed event "
                + event.getEventId()
                + ", delivery status = "
                + delivery.getStatus()
                + ", shipment status = "
                + shipment.getStatus());
    }

    private void handleFailedAttempt(
            Delivery delivery,
            Shipment shipment,
            DeliveryEvent event) {

        delivery.addAttempt(false, event.getFailureReason());
        delivery.getAgent().release();

        System.out.println("Delivery attempt failed: "
                + event.getFailureReason());

        if (delivery.getAttemptCount() >= MAX_ATTEMPTS) {
            delivery.setStatus(DeliveryStatus.DELIVERY_FAILED);
            shipment.setStatus(ShipmentStatus.DELIVERY_FAILED);
            System.out.println("Maximum attempts reached.");
            return;
        }

        // Retry by assigning another eligible agent.
        DeliveryAssignStrategy strategy =
                shipment.getPriority() == ShipmentPriority.PRIME
                        ? new AssignByHighestRating()
                        : new AssignByLessOrders();

        DeliveryAgent newAgent = reserveAgent(shipment, strategy);

        if (newAgent == null) {
            delivery.setStatus(DeliveryStatus.DELIVERY_FAILED);
            shipment.setStatus(ShipmentStatus.PENDING_ASSIGNMENT);
            System.out.println(
                    "No agent available for immediate reassignment.");
            return;
        }

        delivery.reassignAgent(newAgent);
        shipment.setStatus(ShipmentStatus.ASSIGNED);

        System.out.println("Reassigned delivery "
                + delivery.getDeliveryId()
                + " to " + newAgent);
    }

    private boolean isValidTransition(
            DeliveryStatus oldStatus,
            DeliveryStatus newStatus) {

        switch (oldStatus) {
            case ASSIGNED:
                return newStatus == DeliveryStatus.PICKED_UP
                        || newStatus == DeliveryStatus.OUT_FOR_DELIVERY
                        || newStatus == DeliveryStatus.DELIVERY_FAILED
                        || newStatus == DeliveryStatus.CANCELLED;

            case PICKED_UP:
                return newStatus == DeliveryStatus.OUT_FOR_DELIVERY
                        || newStatus == DeliveryStatus.DELIVERY_FAILED
                        || newStatus == DeliveryStatus.CANCELLED;

            case OUT_FOR_DELIVERY:
                return newStatus == DeliveryStatus.DELIVERED
                        || newStatus == DeliveryStatus.DELIVERY_FAILED
                        || newStatus == DeliveryStatus.CANCELLED;

            // A reassigned delivery returns to ASSIGNED.
            // Terminal states do not accept further transitions.
            case DELIVERY_FAILED:
            case DELIVERED:
            case CANCELLED:
            default:
                return false;
        }
    }

    public void shutdown() {
        running = false;

        if (assignmentWorker != null) {
            assignmentWorker.interrupt();
        }

        eventQueue.shutdown();
    }
}

// ---------- Delivery Event Queue ----------

class DeliveryEventQueue {
    private final BlockingQueue<DeliveryEvent> queue =
            new LinkedBlockingQueue<>();

    private final DeliveryService deliveryService;
    private final Set<String> processedEventIds =
            ConcurrentHashMap.newKeySet();

    private volatile boolean running = true;
    private Thread consumer;

    public DeliveryEventQueue(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public void publish(DeliveryEvent event) {
        if (!running) {
            throw new IllegalStateException("Event queue is stopped");
        }

        queue.offer(event);
    }

    public void start() {
        consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running || !queue.isEmpty()) {
                    try {
                        DeliveryEvent event = queue.poll(
                                200, TimeUnit.MILLISECONDS);
                        
                        if (event == null) {
                            continue;
                        }
                        
                        // Basic in-memory idempotency check.
                        if (!processedEventIds.add(event.getEventId())) {
                            System.out.println("Duplicate event ignored: "
                                    + event.getEventId());
                            continue;
                        }
                        
                        deliveryService.updateDeliveryStatus(event);
                        
                    } catch (InterruptedException e) {
                        if (!running) {
                            break;
                        }
                    } catch (Exception e) {
                        System.err.println(
                                "Failed to process delivery event: "
                                        + e.getMessage());
                    }
                }
            }
        }, "delivery-event-consumer");

        consumer.start();
    }

    public void shutdown() {
        running = false;

        if (consumer != null) {
            consumer.interrupt();
        }
    }
}

// ---------- Main: Runnable Demonstration ----------

public class ShipmentDeliverySystem {
    public static void main(String[] args)
            throws InterruptedException {

        DeliveryService deliveryService = new DeliveryService();
        PackagingService packagingService =
                new PackagingService(deliveryService);

        // Register agents.
        DeliveryAgent agent1 = new DeliveryAgent(
                "A1", "Rahul", 4.5, 12.9716, 77.5946);

        DeliveryAgent agent2 = new DeliveryAgent(
                "A2", "Priya", 4.9, 12.9750, 77.6000);

        DeliveryAgent agent3 = new DeliveryAgent(
                "A3", "Amit", 4.7, 12.9680, 77.5900);

        deliveryService.registerAgent(agent1);
        deliveryService.registerAgent(agent2);
        deliveryService.registerAgent(agent3);

        deliveryService.start();

        Address address = new Address(
                "Bengaluru",
                "MG Road",
                12.9716,
                77.5946
        );

        // Create a standard order.
        Order standardOrder = new Order(
                "O101",
                "C101",
                Arrays.asList(
                        new ProductItem("P1", "Laptop", 1),
                        new ProductItem("P2", "Mouse", 2)
                ),
                address,
                false
        );

        // Create a Prime order.
        Order primeOrder = new Order(
                "O102",
                "C102",
                Arrays.asList(
                        new ProductItem("P3", "Phone", 1)
                ),
                address,
                true
        );

        // Submit both shipments.
        Shipment standardShipment =
                packagingService.createShipment(standardOrder);

        Shipment primeShipment =
                packagingService.createShipment(primeOrder);

        // Allow the assignment worker to process both shipments.
        Thread.sleep(1000);

        // Find the delivery associated with each shipment.
        Delivery standardDelivery = findDelivery(
                deliveryService, standardShipment.getShipmentId());

        Delivery primeDelivery = findDelivery(
                deliveryService, primeShipment.getShipmentId());

        if (primeDelivery != null) {
            System.out.println("\nPrime agent assigned: "
                    + primeDelivery.getAgent());

            // Simulate delivery lifecycle.
            DeliveryEventQueue events =
                    deliveryService.getEventQueue();

            events.publish(new DeliveryEvent(
                    "E1",
                    primeDelivery.getDeliveryId(),
                    primeDelivery.getAgent().getAgentId(),
                    DeliveryStatus.OUT_FOR_DELIVERY,
                    null
            ));

            events.publish(new DeliveryEvent(
                    "E2",
                    primeDelivery.getDeliveryId(),
                    primeDelivery.getAgent().getAgentId(),
                    DeliveryStatus.DELIVERY_FAILED,
                    "Customer unavailable"
            ));
        }

        if (standardDelivery != null) {
            System.out.println("\nStandard agent assigned: "
                    + standardDelivery.getAgent());
        }

        // Allow asynchronous events to be consumed.
        Thread.sleep(1000);

        System.out.println("\nFinal shipment statuses:");
        System.out.println(standardShipment);
        System.out.println(primeShipment);

        deliveryService.shutdown();
    }

    private static Delivery findDelivery(
            DeliveryService service,
            String shipmentId) {

        // In a larger implementation, add a direct
        // shipmentId -> deliveryId repository lookup.
        for (String id : Arrays.asList("D1", "D2", "D3", "D4")) {
            Delivery delivery = service.getDelivery(id);

            if (delivery != null
                    && delivery.getShipmentId().equals(shipmentId)) {
                return delivery;
            }
        }

        return null;
    }
}
