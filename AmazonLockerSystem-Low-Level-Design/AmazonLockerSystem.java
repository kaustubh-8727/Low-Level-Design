
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


enum LockerStatus {
    AVAILABLE,
    RESERVED,
    BOOKED,
    MAINTAINANCE,
    OCCUPIED
}

enum LockerSize {
    SMALL,
    MEDIUM,
    LARGE,
    XLARGE
}

enum orderStatus {
    CREATED,
    RESERVED,
    CONFIRMED,
    RECEIVED,
}

enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAID,
    LOCKER_ASSIGNED,
    IN_TRANSIT,
    DELIVERED_TO_LOCKER,
    PICKED_UP,
    CANCELLED,
    EXPIRED
}

class User {

    private String userId;
    private String name;
    private String email;
    private String contactNum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }
}

class PackageItem {
    String itemId;
    double weight;
    Dimension dimension;
    LockerSize lockerSize;
}

class Dimension {
    double length;
    double width;
    double height;
}

class Location {
    double id;
    String city;
    String state;
    String country;
    String pinCode;
}

class PickupCode {
    String otp;
    LocalDateTime expiryTime;

    public PickupCode(String otp) {
        this.otp = otp;
        this.expiryTime = LocalDateTime.now().plusMinutes(20);
    }
}

class Locker {
    String id;
    LockerStatus lockerStatus;
    LockerSize lockerSize;
    PickupCode pickupCode;

    public Locker(LockerStatus lockerStatus, LockerSize lockerSize, PickupCode pickupCode) {
        this.id = UUID.randomUUID().toString();
        this.lockerSize = lockerSize;
        this.lockerStatus = lockerStatus;
        this.pickupCode = pickupCode;
    }
}

class LockerHub {
    Location location;
    Map<String, Locker> lockers = new HashMap<>();

    public LockerHub(Location location) {
        this.location = location;
    }

    public void addLocker(LockerSize lockerSize) {

        Locker locker = new Locker(LockerStatus.AVAILABLE, lockerSize, null);

        if(!lockers.containsKey(locker.id)) {
            lockers.put(locker.id, locker);
        }
    }

    public Locker getLocker(String id) {
        if(lockers.containsKey(id)) {
            lockers.get(id);
        }

        return null;
    }

    public void updateLocker(String id, Locker locker) {}

    public void removeLocker(String id) {}
}

interface LockerAllocationStrategy {
    public Locker fetchlocker(PackageItem packageItem, LockerHub lockerHubs);
}

class LockerAllocationBySize implements  LockerAllocationStrategy {
    
    public Locker fetchlocker(PackageItem packageItem, LockerHub lockerHubs) {
        return null;
    }
}

class LockerAllocationByLevel implements  LockerAllocationStrategy {
    
    public Locker fetchlocker(PackageItem packageItem, LockerHub lockerHubs) {
        return null;
    }
}

class LockerService {

    Map<Location, LockerHub> lockerHubs = new HashMap<>();
    LockerAllocationStrategy lockerAllocationStrategy;

    public LockerService(LockerAllocationStrategy lockerAllocationStrategy) {
        this.lockerAllocationStrategy = lockerAllocationStrategy;
    }

    public LockerHub getLockerHub(Location location) {
        if(lockerHubs.containsKey(location)) {
            return lockerHubs.get(location);
        }

        return null;
    }

    public Locker getLocker(PackageItem packageItem, Location location) {
        if(lockerHubs.containsKey(location)) {
            return lockerAllocationStrategy.fetchlocker(packageItem, lockerHubs.get(location));
        }
        return null;
    }

    public void reserveLocker(Locker locker) {
        locker.lockerStatus = LockerStatus.RESERVED;
    }

    public void bookLocker(Locker locker) {
        locker.lockerStatus = LockerStatus.BOOKED;
    }
}

interface PaymentService {
    public boolean makePayment(double amount);
}

class UPIPayment implements PaymentService {

    public boolean makePayment(double amount) {
        // perform payment processing
        return true;
    }
}

class CardPayment implements PaymentService {

    public boolean makePayment(double amount) {
        // perform payment processing
        return true;
    }
}

interface Notifier {
    public void notifyUser(User user, String message);
}

class EmailNotifier implements Notifier {

    @Override
    public void notifyUser(User user, String message) {
        System.out.println("Email sent to " + user.getEmail() + ": " + message);
    }
}

class SMSNotifier implements Notifier {

    @Override
    public void notifyUser(User user, String message) {
        System.out.println("SMS sent to " + user.getContactNum() + ": " + message);
    }
}

class PushNotifier implements Notifier {

    @Override
    public void notifyUser(User user, String message) {
        System.out.println("Push notification sent to user " + user.getUserId() + ": " + message);
    }
}

class NotificationService {

    private List<Notifier> notifiers;

    public NotificationService(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void notifyUser(User user, String message) {
        for (Notifier notifier : notifiers) {
            notifier.notifyUser(user, message);
        }
    }
}

class Item {

    private String itemId;
    private String itemName;
    private double price;
    private double weight;
    private LockerSize lockerSize;

    public Item() {
    }

    public Item(String itemId,
                String itemName,
                double price,
                double weight,
                LockerSize lockerSize) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.weight = weight;
        this.lockerSize = lockerSize;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LockerSize getLockerSize() {
        return lockerSize;
    }

    public void setLockerSize(LockerSize lockerSize) {
        this.lockerSize = lockerSize;
    }
}

class Bill {

    private String billId;

    private double itemTotal;
    private double taxAmount;
    private double deliveryCharge;
    private double finalAmount;

    public Bill() {
    }

    public Bill(String billId,
                double itemTotal,
                double taxAmount,
                double deliveryCharge) {
        this.billId = billId;
        this.itemTotal = itemTotal;
        this.taxAmount = taxAmount;
        this.deliveryCharge = deliveryCharge;
        this.finalAmount =
                itemTotal + taxAmount + deliveryCharge;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
        calculateFinalAmount();
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
        calculateFinalAmount();
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
        calculateFinalAmount();
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    private void calculateFinalAmount() {
        this.finalAmount =
                itemTotal + taxAmount + deliveryCharge;
    }
}

class Order {

    private String orderId;
    private User user;

    private List<Item> items;

    private Bill bill;

    private PaymentService payment;

    private Locker assignedLocker;

    private DeliveryPerson deliveryPerson;

    private PickupCode pickupCode;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.CREATED;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public PaymentService getPayment() {
        return payment;
    }

    public void setPayment(PaymentService payment) {
        this.payment = payment;
    }

    public Locker getAssignedLocker() {
        return assignedLocker;
    }

    public void setAssignedLocker(Locker assignedLocker) {
        this.assignedLocker = assignedLocker;
    }

    public DeliveryPerson getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public PickupCode getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(PickupCode pickupCode) {
        this.pickupCode = pickupCode;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

class CheckoutService {

    private LockerService lockerService;
    private NotificationService notificationService;

    public CheckoutService(LockerService lockerService,
                           NotificationService notificationService) {
        this.lockerService = lockerService;
        this.notificationService = notificationService;
    }

    public Order checkout(User user,
                          List<Item> items,
                          PaymentService paymentService,
                          Location lockerLocation) {

        // 1. Create Order
        Order order = createOrder(user, items, paymentService);

        // 2. Make Payment
        boolean paymentSuccess =
                paymentService.makePayment(order.getBill().getFinalAmount());

        if (!paymentSuccess) {
            throw new RuntimeException("Payment Failed");
        }

        order.setOrderStatus(OrderStatus.PAID);

        // 3. Find suitable locker
        PackageItem packageItem = buildPackageItem(items);

        Locker locker =
                lockerService.getLocker(packageItem, lockerLocation);

        if (locker == null) {
            throw new RuntimeException("No locker available");
        }

        // 4. Reserve locker
        lockerService.reserveLocker(locker);

        order.setAssignedLocker(locker);
        order.setOrderStatus(OrderStatus.LOCKER_ASSIGNED);

        // 5. Notify User
        notificationService.notifyUser(
                user,
                "Order Created Successfully. Locker Reserved : "
                        + locker.id);

        return order;
    }

    private Order createOrder(User user,
                              List<Item> items,
                              PaymentService paymentService) {

        Order order = new Order();

        order.setOrderId(UUID.randomUUID().toString());
        order.setUser(user);
        order.setItems(items);
        order.setPayment(paymentService);

        Bill bill = generateBill(items);
        order.setBill(bill);

        order.setOrderStatus(OrderStatus.PAYMENT_PENDING);

        return order;
    }

    private Bill generateBill(List<Item> items) {

        double itemTotal = 0;

        for (Item item : items) {
            itemTotal += item.getPrice();
        }

        double tax = itemTotal * 0.18;
        double deliveryCharge = 50;

        return new Bill(
                UUID.randomUUID().toString(),
                itemTotal,
                tax,
                deliveryCharge
        );
    }

    private PackageItem buildPackageItem(List<Item> items) {

        PackageItem packageItem = new PackageItem();

        LockerSize maxSize = LockerSize.SMALL;

        for (Item item : items) {

            if (item.getLockerSize().ordinal() >
                    maxSize.ordinal()) {
                maxSize = item.getLockerSize();
            }

            packageItem.weight += item.getWeight();
        }

        packageItem.lockerSize = maxSize;

        return packageItem;
    }
}

class DeliveryPerson extends User {

    private double rating;
    private Location address;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }
}

class DeliveryService {

    private List<DeliveryPerson> deliveryPersonList = new ArrayList<>();
    private LockerService lockerService;
    private NotificationService notificationService;

    public DeliveryService(LockerService lockerService, NotificationService notificationService) {
        this.lockerService = lockerService;
        this.notificationService = notificationService;
    }

    public DeliveryPerson assignDeliveryPerson(Order order) {

        if (deliveryPersonList.isEmpty()) {
            throw new RuntimeException("No delivery person available");
        }

        DeliveryPerson assignedPerson = deliveryPersonList.get(0);

        order.setDeliveryPerson(assignedPerson);
        order.setOrderStatus(OrderStatus.IN_TRANSIT);

        return assignedPerson;
    }

    public void deliverPackageToLocker(Order order) {

        Locker locker = order.getAssignedLocker();

        if (locker == null) {
            throw new RuntimeException("Locker not assigned");
        }

        locker.lockerStatus = LockerStatus.OCCUPIED;

        PickupCode pickupCode = generatePickupCode();

        order.setPickupCode(pickupCode);
        order.setOrderStatus(OrderStatus.DELIVERED_TO_LOCKER);

        notifyUser(order, pickupCode);
    }

    private PickupCode generatePickupCode() {

        PickupCode pickupCode = new PickupCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        return pickupCode;
    }

    private void notifyUser(Order order, PickupCode pickupCode) {

        String message = "OTP: " + pickupCode.otp + " sent to user " + order.getUser().getName();
        notificationService.notifyUser(order.getUser(), message);
    }

    public void addDeliveryPerson(DeliveryPerson deliveryPerson) {
        deliveryPersonList.add(deliveryPerson);
    }
}

class AmazonLockerSystem {
    public static void main(String[] args) {
        System.err.println("");
    }
}
