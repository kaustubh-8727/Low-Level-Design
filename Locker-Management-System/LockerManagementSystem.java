/*

locker management system

1. person
2. deliveryMan
3. deliveryMan manager
4. order
5. locker
6. locker manager
7. locker allocation service
8. authentication (locker authentication)
9. notification service
10. admin

*/

import java.util.*;

enum DeliveryManStatus {
    AVAILABLE,
    NOT_AVAILABLE
}

enum OrderStatus {
    INITIATED,
    IN_DELIVERY,
    DELIVERED,
    RECEIVED,
    COMPLETED,
    FAILED
}

enum LockerCategory {
    SMALL,
    LARGE,
    EXTRA_LARGE
}

enum LockerStatus {
    EMPTY,
    OCCUPIED
}

class User {
    int userId;
    String userName;
    Location address;
    Cart cart;
    int OTP;

    public User(String userName, Location address) {
        this.userId = (int)(Math.random() * 9000) + 1000;
        this.userName = userName;
        this.address = address;
        this.OTP = 0;
        this.cart = new Cart();
    }

    public void addProductToCart(Product product, int count) {
        cart.addProductToCart(product, count);
    }

    public void removeProductFromCart(Product product, int count) {
        cart.removeProductFromCart(product, count);
    }

    public Cart getCart() {
        return cart;
    }
    
    public void setOTP(int OTP) {
        this.OTP = OTP;
    }
    
    public int getOTP() {
        return OTP;
    }
    
    public int getUserId() {
        return userId;
    }
}

class DeliveryMan {
    int deliveryManId;
    String name;
    String contactNum;
    String emailId;
    Location address;
    DeliveryManStatus deliveryManStatus;
    Order order;
    
    public DeliveryMan(String name, String contactNum, String emailId, Location address) {
        this.deliveryManId = (int)(Math.random() * 9000) + 1000;
        this.name = name;
        this.contactNum = contactNum;
        this.emailId = emailId;
        this.address = address;
        this.deliveryManStatus = DeliveryManStatus.AVAILABLE;
    }
    
    public void setDeliveryManStatus(DeliveryManStatus deliveryManStatus) {
        this.deliveryManStatus = deliveryManStatus;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public DeliveryManStatus getStatus() {
        return deliveryManStatus;
    }
}

class DeliveryManService {
    List<DeliveryMan> deliveryManList = new ArrayList<>();
    
    public void addDeliveryMan(DeliveryMan deliveryMan) {
        deliveryManList.add(deliveryMan);
    }
    
    public void removeDeliveryMan(int deliveryManId) {
        // deliveryManList.removeIf(deliveryMan -> deliveryMan.deliveryManId.equals(deliveryManId));
    }
    
    public DeliveryMan getDeliveryMan() {
        for (DeliveryMan deliveryMan : deliveryManList) {
            if (deliveryMan.getStatus() == DeliveryManStatus.AVAILABLE) {
                return deliveryMan;
            }
        }
        return null;
    }
    
    public void assignDeliveryMan(DeliveryMan deliveryMan, Order order) {
        deliveryMan.setDeliveryManStatus(DeliveryManStatus.NOT_AVAILABLE);
        deliveryMan.setOrder(order);
    }
}

class Location {
    String country;
    String state;
    String city;
    String area;
    String pincode;
    
    public Location(String country, String state, String city, String area, String pincode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.area = area;
        this.pincode = pincode;
    }
}

class Locker {
    int lockerId;
    LockerCategory lockerCategory;
    LockerStatus lockerStatus;
    int OTP;

    public Locker(LockerCategory lockerCategory) {
        this.lockerId = (int)(Math.random() * 9000) + 1000;
        this.lockerCategory = lockerCategory;
        this.lockerStatus = LockerStatus.EMPTY;
    }
    
    public void setLockerStatus(LockerStatus lockerStatus) {
        this.lockerStatus = lockerStatus;
    }
    
    public LockerStatus getLockerStatus() {
        return lockerStatus;
    }
    
    public LockerCategory getLockerCategory() {
        return lockerCategory;
    }
    
    public void setOTP(int OTP) {
        this.OTP = OTP;
    }
    
    public int getOTP() {
        return OTP;
    }
    
    public int getLockerId() {
        return lockerId;
    }
}

class LockerService {
    LockerAllocationStrategy lockerAllocationStrategy;
    Locker[][] lockerGrid;
    int lockerGridSize;
    int OTP = 0;
    
    public LockerService(LockerAllocationStrategy lockerAllocationStrategy, int lockerGridSize) {
        this.lockerAllocationStrategy = lockerAllocationStrategy;
        this.lockerGridSize = lockerGridSize;
        this.lockerGrid = new Locker[lockerGridSize][lockerGridSize];
        initializeAllLockers();
    }
    
    private void initializeAllLockers() {
        for (int row = 0; row < lockerGridSize; row++) {
            for (int col = 0; col < lockerGridSize; col++) {
                if (row % 2 == 0) {
                    lockerGrid[row][col] = new Locker(LockerCategory.SMALL);
                } else {
                    lockerGrid[row][col] = new Locker(LockerCategory.LARGE);
                }
            }
        }
    }
    
    public void setOTP(int OTP) {
        this.OTP = OTP;
    }
    
    public int getOTP() {
        return OTP;
    }
    
    public Locker getLocker(LockerFilter lockerFilter) {
        return lockerAllocationStrategy.getLocker(lockerGrid, lockerFilter);
    }
}

class LockerFilter {
    private Location deliveryLocation;
    private LockerCategory lockerSize;

    public Location getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(Location deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public LockerCategory getLockerSize() {
        return lockerSize;
    }

    public void setLockerSize(LockerCategory lockerSize) {
        this.lockerSize = lockerSize;
    }
}

interface LockerAllocationStrategy {
    Locker getLocker(Locker[][] lockerGrid, LockerFilter lockerFilter);
}

class LockerAllocationStrategyByLocation implements LockerAllocationStrategy {
    @Override
    public Locker getLocker(Locker[][] lockerGrid, LockerFilter lockerFilter) {
        for (int row = 0; row < lockerGrid.length; row++) {
            for (int col = 0; col < lockerGrid[row].length; col++) {
                if (lockerGrid[row][col].getLockerStatus() == LockerStatus.EMPTY) {
                    return lockerGrid[row][col];
                }
            }
        }
        return null;
    }
}

class LockerAllocationStrategyBySize implements LockerAllocationStrategy {
    @Override
    public Locker getLocker(Locker[][] lockerGrid, LockerFilter lockerFilter) {
        for (int row = 0; row < lockerGrid.length; row++) {
            for (int col = 0; col < lockerGrid[row].length; col++) {
                if (lockerGrid[row][col].getLockerStatus() == LockerStatus.EMPTY && 
                    lockerGrid[row][col].getLockerCategory() == lockerFilter.getLockerSize()) {
                    return lockerGrid[row][col];
                }
            }
        }
        return null;
    }
}

class Product {
    String productName;
    int productId;
    double productPrice;
    
    public Product(String productName, double ProductPrice) {
        this.productId = (int)(Math.random() * 9000) + 1000;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}

class Cart {
    Map<Integer, Integer> productCartMap = new HashMap<>();

    public void addProductToCart(Product product, int count) {
        if(productCartMap.containsKey(product.productId)) {
            count += productCartMap.get(product.productId);
        }
        productCartMap.put(product.productId, count);
    }

    public void removeProductFromCart(Product product, int count) {
        if(productCartMap.containsKey(product.productId)) {
            int currentCount = productCartMap.get(product.productId);
            if(count < currentCount) {
                productCartMap.put(product.productId, currentCount - count);
                return;
            }
        }
        System.out.println("product cannot be removed");
    }
}

class Order {
    int orderId;
    OrderStatus status;
    User user;
    Map<Integer, Integer> productCategoryCountMap;
    Invoice invoice;
    Payment payment;
    DeliveryMan deliveryMan;
    boolean lockerNeeded = false;

    public Order(User user) {
        this.orderId = (int)(Math.random() * 9000) + 1000;
        this.user = user;
        this.productCategoryCountMap = user.getCart().productCartMap;
        invoice = new Invoice();
        invoice.generateInvoice(productCategoryCountMap);
        status = OrderStatus.INITIATED;
        this.deliveryMan = null;
    }

    public boolean checkout() {
        boolean paymentDone = makePayment(new UpiPayment());

        if(paymentDone) {
            status = OrderStatus.COMPLETED;
            return true;
        } else {
            status = OrderStatus.FAILED;
            System.out.println("payment failed");
            return false;
        }
    }

    public boolean makePayment(PaymentMode paymentMode) {
        payment = new Payment();
        return payment.makePayment(paymentMode, invoice.getTotalAmount());
    }

    public void getCompleteOrderDetails() {
        System.out.println("\nOrder Details:");
        System.out.println("Order ID: " + orderId);
        System.out.println("Status: " + status);
        System.out.println("User: " + user.userName);
        System.out.println("Delivery Address: " + user.address.city + ", " + user.address.area + ", " + user.address.pincode);
    
        System.out.println("\nProducts:");
        for (Map.Entry<Integer, Integer> entry : productCategoryCountMap.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("Product ID: " + productId + ", Quantity: " + quantity);
        }
    
        System.out.println("\nInvoice Summary:");
        System.out.println("Total Item Price: " + invoice.totalItemsPrice);
        System.out.println("Tax: " + invoice.taxOnOrder + "%");
        System.out.println("Total Amount Paid: " + invoice.getTotalAmount());
    
        if (deliveryMan != null) {
            System.out.println("\nDelivery Information:");
            System.out.println("Delivery Person: " + deliveryMan.name);
            System.out.println("Contact: " + deliveryMan.contactNum);
            System.out.println("Status: " + (deliveryMan.getStatus() == DeliveryManStatus.NOT_AVAILABLE ? "On Delivery" : "Available"));
        }
    }
    
    public void setLockerNeeded() {
        this.lockerNeeded = true;
    }
    
    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
    
    public int getOrderId() {
        return orderId;
    }
}

class OrderController {

    List<Order> orderList;
    Map<Integer, List<Order>> userIdOrdersMap = new HashMap<>();
    Map<Integer, Locker> orderIdLockerMap = new HashMap<>();
    LockerService lockerService;
    DeliveryManService deliveryManService;

    OrderController(LockerService lockerService, DeliveryManService deliveryManService){
        orderList = new ArrayList<>();
        userIdOrdersMap = new HashMap<>();
        this.lockerService = lockerService;
        this.deliveryManService = deliveryManService;
    }

    public Order createNewOrder(User user){
        Order order = new Order(user);
        orderList.add(order);

        if(userIdOrdersMap.containsKey(user.userId)){
            List<Order> userOrders = userIdOrdersMap.get(user.userId);
            userOrders.add(order);
            userIdOrdersMap.put(user.userId, userOrders);
        } else {
            List<Order> userOrders = new ArrayList<>();
            userOrders.add(order);
            userIdOrdersMap.put(user.userId, userOrders);
        }
        return order;
    }

    public void removeOrder(Order order){
        // logic to remove order
    }

    public List<Order> getOrderByCustomerId(int userId){
        // logic to get orders placed by user
        return null;
    }

    public Order getOrderByOrderId(int orderId){
        // logic to get the order
        return null;
    }

    public void checkoutOrder(User user, Order order, boolean lockerNeeded) {
        if(lockerNeeded == true) {
            int OTP = (int)(Math.random() * 9000) + 1000;
            user.setOTP(OTP);
            LockerFilter lockerFilter = new LockerFilter();
            lockerFilter.setLockerSize(LockerCategory.LARGE);
            Locker locker = lockerService.getLocker(lockerFilter);
            order.setLockerNeeded();
            locker.setOTP(OTP);

            if(!orderIdLockerMap.containsKey(order.getOrderId())){
                orderIdLockerMap.put(order.getOrderId(), locker);
            }
        }
        
        if(order.checkout()) {
            DeliveryMan deliveryMan = deliveryManService.getDeliveryMan();
            deliveryManService.assignDeliveryMan(deliveryMan, order);
            order.setDeliveryMan(deliveryMan);
        }
    }
    
    public Order getDeliveredOrder(User user, int orderId) {
        int userId = user.getUserId();
        List<Order> userOrders = userIdOrdersMap.get(userId);
        
        Order deliveredOrder = null;
        Locker orderLocker = null;
        
        if (userOrders != null) {
            for (Order order : userOrders) {
                if (order.getOrderId() == orderId) {
                    deliveredOrder = order;
                    Locker locker = orderIdLockerMap.get(orderId);
                    if (locker != null) {
                        orderLocker = locker;
                        System.out.println("Order ID: " + orderId + " is delivered and stored in Locker ID: " + locker.getLockerId());
                    }
                }
            }
        }
        
        if(deliveredOrder == null) {
            System.out.println("order not delivered to the correct location we are working on it");
            return null;
        }
        
        if(deliveredOrder.lockerNeeded == true) {
            if(authenticateLocker(user, deliveredOrder, orderLocker)) {
                return deliveredOrder;
            } else {
                System.out.println("incorrect OTP please enter correct OTP");
                return null;
            }
        }
        
        if(deliveredOrder != null) {
            return deliveredOrder;
        }
        
        return null;
    }
    
    public boolean authenticateLocker(User user, Order order, Locker locker) {
        if(user.getOTP() == locker.getOTP()) {
            return true;
        }
        return false;
    }
}

class Invoice {
    double totalPaymentAmount = 0.0;
    double taxOnOrder = 0.0;
    double totalItemsPrice = 0.0;

    public void setTaxOnOrder(double taxOnOrder) {
        this.taxOnOrder = taxOnOrder;
    }

    public void generateInvoice(Map<Integer, Integer> productCategoryCountMap) {
        for(Map.Entry<Integer, Integer> entry : productCategoryCountMap.entrySet()) {
            double itemPrice = 200;
            int count = entry.getValue();
            totalItemsPrice = totalItemsPrice + (itemPrice * count);
        }

        totalPaymentAmount = totalItemsPrice + (totalItemsPrice * taxOnOrder / 100);
    }

    public double getTotalAmount() {
        return totalPaymentAmount;
    }
}

class Payment {
    public boolean makePayment(PaymentMode paymentMode, double amount) {
        return paymentMode.makePayment(amount);
    }
}

interface PaymentMode {
    public boolean makePayment(double amount);
}

class UpiPayment implements PaymentMode {
    public boolean makePayment(double amount) {
        return true;
    }
}

class CardPayment implements PaymentMode {
    public boolean makePayment(double amount) {
        return true;
    }
}

public class LockerManagementSystem {
    public static void main(String[] args) {
        // Initialize locker allocation strategy and services
        LockerAllocationStrategy lockerStrategy = new LockerAllocationStrategyBySize();
        LockerService lockerService = new LockerService(lockerStrategy, 5);
        DeliveryManService deliveryManService = new DeliveryManService();
        
        // Adding delivery personnel to the delivery service
        deliveryManService.addDeliveryMan(new DeliveryMan("John Doe", "1234567890", "john@example.com", new Location("Country", "State", "City", "Area", "123456")));
        deliveryManService.addDeliveryMan(new DeliveryMan("Jane Smith", "0987654321", "jane@example.com", new Location("Country", "State", "City", "Area", "654321")));
        
        // Initialize order controller
        OrderController orderController = new OrderController(lockerService, deliveryManService);
        
        // Create user and add products to cart
        User user1 = new User("Alice", new Location("Country", "State", "City", "Area", "100001"));
        Product product1 = new Product("Book", 10.0);
        Product product2 = new Product("Laptop", 1000.0);
        
        user1.addProductToCart(product1, 1);
        user1.addProductToCart(product2, 1);
        
        // Create a new order for the user
        Order order1 = orderController.createNewOrder(user1);
        
        // Checkout order with locker needed
        boolean lockerNeeded = true;
        orderController.checkoutOrder(user1, order1, lockerNeeded);
        
        // Simulate order delivery
        System.out.println("\nOrder Checkout and Delivery:");
        order1.getCompleteOrderDetails();
        
        // Retrieve delivered order from locker
        System.out.println("\nRetrieving Order from Locker:");
        Order deliveredOrder = orderController.getDeliveredOrder(user1, order1.getOrderId());
        
        if (deliveredOrder != null) {
            System.out.println("Order retrieved successfully from locker.");
        } else {
            System.out.println("Failed to retrieve order from locker.");
        }
    }
}
