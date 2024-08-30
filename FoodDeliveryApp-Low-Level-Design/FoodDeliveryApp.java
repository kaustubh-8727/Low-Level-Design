import java.util.*;

enum FoodItemStatus {
    AVAILABLE,
    NOT_AVAILABLE
}

enum OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}

enum FoodItemType {
    CHINESE,
    ITALIAN,
    INDIAN,
    MEXICAN;
}

enum DeliveryAgentStatus {
    AVAILABLE,
    NOT_AVAILABLE
}

class User {

    private String userId;
    private String userName;
    private String emailId;
    private String contactNumber;
    private Location location;
    private Cart cart;

    public User(String userId, String userName, String emailId, String contactNumber, Location location) {
        this.userId = userId;
        this.userName = userName;
        this.emailId = emailId;
        this.contactNumber = contactNumber;
        this.location = location;
        cart = new Cart();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Location getLocation() {
        return location;
    }

    public Cart getCart() {
        return cart;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addToCart(OrderItem item) {
        System.out.println("Adding item to cart...");
        cart.addToCart(item);
    }

    public void notifyUser(Order order) {
        System.out.println("Notifying user about order status...");
    }
}

class Location {
    String country;
    String state;
    String city;
    String pincode;

    public Location(String country, String state, String city, String pincode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
    }
}

class Restaurant {

    String restaurentId;
    String restaurantName;
    Location location;
    String emailId;
    String contactNumber;
    List<FoodItem> foodItems;
    List<Order> orders;
    Rating rating;

    public Restaurant(String restaurantName, String emailId, String contactNumber, Location location) {
        this.restaurentId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.restaurantName = restaurantName;
        this.emailId = emailId;
        this.contactNumber = contactNumber;
        this.location = location;
        foodItems = new ArrayList<>();
        orders = new ArrayList<>();
        this.rating = new Rating();
    }

    public void addFoodItem(FoodItem foodItem) {
        System.out.println("Adding food item to restaurant menu...");
        foodItems.add(foodItem);
    }

    public FoodItem getFoodItem(String foodItemId) {
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getId() == foodItemId && foodItem.getStatus() == FoodItemStatus.AVAILABLE) {
                return foodItem;
            }
        }
        return null;
    }

    public boolean checkFoodItem(String foodItemId) {
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getId() == foodItemId && foodItem.getStatus() == FoodItemStatus.AVAILABLE) {
                return true;
            }
        }
        return false;
    }

    public void removeFoodItem(String foodItemId) {
        // logic to remove food item
    }

    public double getRating() {
        return rating.getRating();
    }

    public void addRating(double ratingNum, String comment) {
        System.out.println("Adding rating to restaurant...");
        rating.addRating(ratingNum, comment);
    }

    public void notifyRestaurant(Order order) {
        System.out.println("Notifying restaurant about the new order...");
        orders.add(order);
    }
}

class RestaurantController {

    List<Restaurant> restaurants = new ArrayList<>();

    public void addRestaurant(Restaurant restaurant) {
        System.out.println("Adding restaurant to the system...");
        restaurants.add(restaurant);
    }

    public void removeRestaurant(String restaurantId) {
        // logic to remove the restaurant
    }

    public List<Restaurant> getRestaurantByFoodItem(String foodItemId) {
        System.out.println("Searching for restaurants with the specified food item...");
        List<Restaurant> restaurantList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.checkFoodItem(foodItemId) == true) {
                restaurantList.add(restaurant);
            }
        }
        return restaurantList;
    }

    public List<Restaurant> getTopRatedRestaurant() {
        // returns 10 top rated restaurant
        return null;
    }
}

class Rating {

    List<Double> ratingList = new ArrayList<>();
    List<String> ratingComments = new ArrayList<>();
    double totalRatingcount = 0;
    double totalRating = 0.0;

    public void addRating(double rating, String ratingComment) {
        ratingList.add(rating);
        ratingComments.add(ratingComment);

        totalRatingcount++;
        totalRating = (totalRating + rating) / totalRatingcount;
    }

    public double getRating() {
        return totalRating;
    }
}

class FoodItem {

    String foodItemId;
    String foodItemName;
    FoodItemType foodItemType;
    double price;
    FoodItemStatus foodItemStatus;

    public FoodItem(String foodItemName, FoodItemType foodItemType, double price, FoodItemStatus foodItemStatus) {
        this.foodItemId = "FI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.foodItemName = foodItemName;
        this.foodItemType = foodItemType;
        this.price = price;
        this.foodItemStatus = foodItemStatus;
    }

    public String getId() {
        return foodItemId;
    }

    public FoodItemStatus getStatus() {
        return foodItemStatus;
    }

    public double getPrice() {
        return price;
    }
}

class Cart {

    List<OrderItem> items = new ArrayList<>();

    public void addToCart(OrderItem item) {
        System.out.println("Adding item to cart...");
        items.add(item);
    }

    public void removeFromCard(OrderItem item) {
        items.remove(item);
    }

    public List<OrderItem> getOrderList() {
        return items;
    }
}

class DeliveryAgent {
    String agentId;
    String agentName;
    String contactNumber;
    DeliveryAgentStatus deliveryAgentStatus;
    Order currentOrder;

    public DeliveryAgent(String agentId, String agentName, String contactNumber) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.contactNumber = contactNumber;
        deliveryAgentStatus = DeliveryAgentStatus.AVAILABLE;
    }
    
    public DeliveryAgentStatus getStatus() {
        return deliveryAgentStatus;
    }
    
    public void updateStatus(DeliveryAgentStatus deliveryAgentStatus) {
        this.deliveryAgentStatus = deliveryAgentStatus;
    }
    
    public void notifyDeliveryAgent(Order order) {
        this.currentOrder = currentOrder;
        System.out.println("Notifying delivery agent to deliver the order...");
    }
}

class Order {

    String orderId;
    User user;
    OrderStatus orderStatus;
    Restaurant restaurant;
    Invoice invoice;
    DeliveryAgent deliveryAgent;
    Location deliveryAddress;

    public Order createOrder(User user, Restaurant restaurant, Location deliveryAddress) {
        System.out.println("Creating order...");
        this.orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.orderStatus = OrderStatus.PENDING;
        this.user = user;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.invoice = new Invoice();
        invoice.generateInvoice(user);

        cleanUpUserCart();

        return this;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return orderId;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public OrderStatus getStatus() {
        return orderStatus;
    }

    public void cleanUpUserCart() {
        System.out.println("Cleaning up user cart...");
    }
    
    public void assignDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }
}

class OrderController {

    List<Order> orderList;
    Map<String, List<Order>> userIdOrdersMap;
    List<DeliveryAgent> deliveryAgents = new ArrayList<>();

    OrderController() {
        orderList = new ArrayList<>();
        userIdOrdersMap = new HashMap<>();
    }

    public Order placeOrder(User user, Restaurant restaurant, Location deliveryAddress) {
        System.out.println("Placing order...");
        Order order = new Order();
        order.createOrder(user, restaurant, deliveryAddress);

        orderList.add(order);

        if (userIdOrdersMap.containsKey(user.getUserId())) {
            List<Order> userOrders = userIdOrdersMap.get(user.getUserId());
            userOrders.add(order);
            userIdOrdersMap.put(user.getUserId(), userOrders);
        } else {
            List<Order> userOrders = new ArrayList<>();
            userOrders.add(order);
            userIdOrdersMap.put(user.getUserId(), userOrders);
        }

        restaurant.notifyRestaurant(order);

        return order;
    }

    public void updateOrderStatus(String orderId, OrderStatus orderStatus) {
        System.out.println("Updating order status...");
        for (Order order : orderList) {
            if (order.getId() == orderId) {
                order.setOrderStatus(orderStatus);
                order.getUser().notifyUser(order);
                allocateDeliveryAgent(order);
            }
        }
    }
    
    public void addDeliveryAgent(DeliveryAgent deliveryAgent) {
        deliveryAgents.add(deliveryAgent);
    }
    
    public void allocateDeliveryAgent(Order order) {
        if(order.getStatus() != OrderStatus.CONFIRMED) {
            return;
        }
        for(DeliveryAgent deliveryAgent : deliveryAgents) {
            if(deliveryAgent.getStatus() == DeliveryAgentStatus.AVAILABLE) {
                order.assignDeliveryAgent(deliveryAgent);
                deliveryAgent.updateStatus(DeliveryAgentStatus.NOT_AVAILABLE);
                deliveryAgent.notifyDeliveryAgent(order);
            }
        }
    }

    public void removeOrder(Order order) {
        // logic to remove order
    }

    public List<Order> getOrderByCustomerId(int userId) {
        // logic to get orders placed by user
        return null;
    }

    public Order getOrderByOrderId(int orderId) {
        // logic to get the order
        return null;
    }
}

class OrderItem {

    FoodItem foodItem;
    int quantity;

    public OrderItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public int getQuantity() {
        return quantity;
    }
}

class Invoice {
    
    List<OrderItem> orderItems;
    double totalPrice;
    
    public void generateInvoice(User user) {
        orderItems = user.getCart().getOrderList();
        totalPrice = calculateTotalPrice();
    }
    
    public double calculateTotalPrice() {
        
        double totalAmount = 0.0;
        for(OrderItem orderItem : orderItems) {
            totalAmount = totalAmount + (orderItem.getFoodItem().getPrice() * orderItem.getQuantity());
        }
        
        return totalAmount;
    }
}

public class FoodDeliveryApp {
	public static void main(String[] args) {
	    
	    // create controllers
	    RestaurantController restaurantController = new RestaurantController();
	    OrderController orderController = new OrderController();
		
		// create user
		User user = new User("user-4545", "alex", "alex@gmail.com", "877867677", new Location("USA", "Arizona", "Phoenix", "zx-6756"));
		
		// create delivery Agent
		DeliveryAgent deliveryAgent = new DeliveryAgent("agent-7777", "max", "46464443345");
		
		// add delivery agent to order controller
		orderController.addDeliveryAgent(deliveryAgent);
		
		// create some food items
		FoodItem foodItem1 = new FoodItem("pizza", FoodItemType.ITALIAN, 250.0, FoodItemStatus.AVAILABLE);
		FoodItem foodItem2 = new FoodItem("noodles", FoodItemType.CHINESE, 200.0, FoodItemStatus.AVAILABLE);
		
		// create restaurant
		Restaurant restaurant = new Restaurant("taste world", "tw@gmail.com", "3453563634", new Location("USA", "Arizona", "Phoenix", "zx-6759"));
		restaurant.addFoodItem(foodItem1);
		restaurant.addFoodItem(foodItem2);

		// add restaurant to controller
		restaurantController.addRestaurant(restaurant);
		
		// 1. user search for food in app
		List<Restaurant> restaurantList = restaurantController.getRestaurantByFoodItem(foodItem1.getId());
		
		// 2. user selects first restaurant to order
		Restaurant favouriteRestaurant = restaurantList.get(0);
		OrderItem orderItem = new OrderItem(foodItem1, 4);
		
		// 3. user add order item to cart
		user.addToCart(orderItem);
		
		// 4. user place the order
		Order order = orderController.placeOrder(user, favouriteRestaurant, user.getLocation());
		
		// 5. order is confirmed by the restaurant
		orderController.updateOrderStatus(order.getId(), OrderStatus.CONFIRMED);
		
	}
}
