
/*

1. foodItem -> represents the details of food item in menu.
2. menu -> represents all the foodItems, manages all the crud operations on food item.
3. inventory -> represents all the products needed for cooking, manages all the crud operations on food products.
4. staffPerson -> interface that represents the person who works in restaurent.
5. staffService -> represents all the staff people, manages all crud operation on staff person.
6. staffRole -> enum for staff person role.
7. payment service -> manages the payment through multiple payment methods.
8. order -> represent the order details that customer wants to place.
9. orderService -> manages all the orders placed by the customers.
10. bill -> represents the bill class.
11. order status -> enum.
12. paymentStatus -> enum.

*/

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

enum FoodCategory {
    INDIAN,
    MAXICAN,
    CHINESE,
    THAI,
    ITALIAN
}

enum FoodStatus {
    AVAILABLE,
    NOT_AVAILABLE
}

enum StaffRole {
    CLEANING,
    CHEF,
    WAITER,
    ADMIN,
    BILLING
}

enum StaffStatus {
    AVAILABLE,
    ABSENT,
    ON_LEAVE
}

enum OrderStatus {
    IN_PROGRESS,
    COOKED,
    SERVED
}

enum PaymentStatus {
    PENDING,
    FAILED,
    SUCCESS
}

class FoodItem {
    private String foodId;
    private String name;
    private FoodCategory foodCategory;
    private double price;
    private String description;
    private FoodStatus foodStatus;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodStatus getFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(FoodStatus foodStatus) {
        this.foodStatus = foodStatus;
    }
}

class Menu {
    private List<FoodItem> menuList = new ArrayList<>();
    private static Menu instance;

    private Menu() {}

    public static Menu getInstance() {
        if (instance == null) {
            synchronized (Menu.class) {
                if (instance == null) {
                    instance = new Menu();
                }
            }
        }
        return instance;
    }

    public void addFoodItem(FoodItem foodItem) {
        menuList.add(foodItem);
    }

    public void updateFoodItem(String id, FoodItem updatedFoodItem) {
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getFoodId() == null ? id == null : menuList.get(i).getFoodId().equals(id)) {
                menuList.set(i, updatedFoodItem);
                return;
            }
        }
    }

    public void deleteFoodItem(String id) {
        menuList.removeIf(foodItem -> (foodItem.getFoodId() == null ? id == null : foodItem.getFoodId().equals(id)));
    }

    public FoodItem getFoodItem(String id) {
        for (FoodItem foodItem : menuList) {
            if (foodItem.getFoodId() == null ? id == null : foodItem.getFoodId().equals(id)) {
                return foodItem;
            }
        }
        return null;
    }

    public List<FoodItem> getAllFoodItems() {
        return menuList;
    }
}

class Ingredient {
    private String ingredientId;
    private String ingredientName;
    private int quantity;

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

class Inventory {
    private static Inventory instance;
    private List<Ingredient> ingredientList = new ArrayList<>();

    private Inventory() {} // Private constructor for singleton

    public static Inventory getInstance() {
        if (instance == null) {
            synchronized (Inventory.class) {
                if (instance == null) {
                    instance = new Inventory();
                }
            }
        }
        return instance;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredientList.add(ingredient);
    }

    public void updateIngredient(String ingredientId, Ingredient updatedIngredient) {
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getIngredientId().equals(ingredientId)) {
                ingredientList.set(i, updatedIngredient);
                return;
            }
        }
    }

    public void deleteIngredient(String ingredientId) {
        ingredientList.removeIf(ingredient -> ingredient.getIngredientId().equals(ingredientId));
    }

    public Ingredient getIngredient(String ingredientId) {
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getIngredientId().equals(ingredientId)) {
                return ingredient;
            }
        }
        return null;
    }
}

abstract class StaffPerson {
    private String personId;
    private String name;
    private StaffRole staffRole;
    private String contactNumber;
    private StaffStatus staffStatus;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public StaffStatus getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(StaffStatus staffStatus) {
        this.staffStatus = staffStatus;
    }
}

class StaffService {
    private List<StaffPerson> chefStaffList = new ArrayList<>();
    private List<StaffPerson> cleaningStaffList = new ArrayList<>();
    private List<StaffPerson> waiterStaffList = new ArrayList<>();
    private List<StaffPerson> billingStaffList = new ArrayList<>();

    private static StaffService instance;

    private StaffService() {};

    public static StaffService getInstance() {
        if (instance == null) {
            synchronized (StaffService.class) {
                if (instance == null) {
                    instance = new StaffService();
                }
            }
        }
        return instance;
    }

    private void addStaffMember(List<StaffPerson> staffList, StaffPerson staffPerson) {
        staffList.add(staffPerson);
    }

    private StaffPerson getStaffMember(List<StaffPerson> staffList, String personId) {
        for (StaffPerson staffPerson : staffList) {
            if (staffPerson.getPersonId().equals(personId)) {
                return staffPerson;
            }
        }
        return null;
    }

    private void updateStaffMember(List<StaffPerson> staffList, String personId, StaffPerson updatedStaffPerson) {
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getPersonId().equals(personId)) {
                staffList.set(i, updatedStaffPerson);
                return;
            }
        }
    }

    private void deleteStaffMember(List<StaffPerson> staffList, String personId) {
        Iterator<StaffPerson> iterator = staffList.iterator();
        while (iterator.hasNext()) {
            StaffPerson staffPerson = iterator.next();
            if (staffPerson.getPersonId().equals(personId)) {
                iterator.remove();
                return;
            }
        }
    }

    // Chef Staff
    public void addChef(StaffPerson staff) { addStaffMember(chefStaffList, staff); }
    public StaffPerson getChef(String id) { return getStaffMember(chefStaffList, id); }
    public void updateChef(String id, StaffPerson updatedStaff) { updateStaffMember(chefStaffList, id, updatedStaff); }
    public void deleteChef(String id) { deleteStaffMember(chefStaffList, id); }

    // Cleaning Staff
    public void addCleaner(StaffPerson staff) { addStaffMember(cleaningStaffList, staff); }
    public StaffPerson getCleaner(String id) { return getStaffMember(cleaningStaffList, id); }
    public void updateCleaner(String id, StaffPerson updatedStaff) { updateStaffMember(cleaningStaffList, id, updatedStaff); }
    public void deleteCleaner(String id) { deleteStaffMember(cleaningStaffList, id); }

    // Waiter Staff
    public void addWaiter(StaffPerson staff) { addStaffMember(waiterStaffList, staff); }
    public StaffPerson getWaiter(String id) { return getStaffMember(waiterStaffList, id); }
    public void updateWaiter(String id, StaffPerson updatedStaff) { updateStaffMember(waiterStaffList, id, updatedStaff); }
    public void deleteWaiter(String id) { deleteStaffMember(waiterStaffList, id); }

    // Billing Staff
    public void addBillingStaff(StaffPerson staff) { addStaffMember(billingStaffList, staff); }
    public StaffPerson getBillingStaff(String id) { return getStaffMember(billingStaffList, id); }
    public void updateBillingStaff(String id, StaffPerson updatedStaff) { updateStaffMember(billingStaffList, id, updatedStaff); }
    public void deleteBillingStaff(String id) { deleteStaffMember(billingStaffList, id); }
}

class Order {
    private String orderId;
    private FoodItem foodItem;
    private int quantity;
    private Date orderPlaceDate;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

    public Order(FoodItem foodItem, int quantity) {
        this.orderId = "1234";
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.orderPlaceDate = new Date();
        this.orderStatus = OrderStatus.IN_PROGRESS;
        this.paymentStatus = paymentStatus.PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getOrderPlaceDate() {
        return orderPlaceDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

class OrderService {

    private static OrderService instance;
    private List<Order> orderList = new ArrayList<>();

    private OrderService() {} // Private constructor for singleton

    public static OrderService getInstance() {
        if (instance == null) {
            synchronized (OrderService.class) {
                if (instance == null) {
                    instance = new OrderService();
                }
            }
        }
        return instance;
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public Order getOrder(String orderId) {
        for (Order order : orderList) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        for (Order order : orderList) {
            if (order.getOrderId().equals(orderId)) {
                order.setOrderStatus(newStatus);
                return;
            }
        }
    }

    public void deleteOrder(String orderId) {
        Iterator<Order> iterator = orderList.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) {
                iterator.remove();
                return;
            }
        }
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderList);
    }
}

class Bill {
    private String billNumber;
    private Order order;
    private double totalAmount;
    private Date billDate;

    public Bill(String billNumber, Order order) {
        this.billNumber = billNumber;
        this.order = order;
        this.totalAmount = calculateTotalAmount();
        this.billDate = new Date();
    }

    private double calculateTotalAmount() {
        return order.getQuantity() * order.getFoodItem().getPrice();
    }

    public String getBillNumber() {
        return billNumber;
    }

    public Order getOrder() {
        return order;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Date getBillDate() {
        return billDate;
    }

    @Override
    public String toString() {
        return "Bill Number: " + billNumber + 
               "\nOrder ID: " + order.getOrderId() +
               "\nTotal Amount: $" + totalAmount +
               "\nDate: " + billDate;
    }
}

class Payment {

    private static Payment instance;
    private PaymentMethod paymentMethod;

    private Payment() {} // Private constructor for singleton

    public static Payment getInstance() {
        if (instance == null) {
            synchronized (Payment.class) {
                if (instance == null) {
                    instance = new Payment();
                }
            }
        }
        return instance;
    }

    public Payment(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean makePayment(Bill bill) {
        return paymentMethod.makePayment(bill);
    }
}

interface PaymentMethod {
    public boolean makePayment(Bill bill);
}

class UPIPayment implements  PaymentMethod {
    public boolean makePayment(Bill bill) {
        return true;
    }
}

class CardPayment implements  PaymentMethod {
    public boolean makePayment(Bill bill) {
        return true;
    }
}

class CreditCardPayment implements  PaymentMethod {
    public boolean makePayment(Bill bill) {
        return true;
    }
}

class RestaurantFactory {

    private static Menu menuInstance;
    private static Inventory inventoryInstance;
    private static StaffService staffServiceInstance;
    private static OrderService orderServiceInstance;
    private static Payment paymentInstance;

    public Menu getMenu() {
        return menuInstance.getInstance();
    }

    public Inventory getInventory() {
        return inventoryInstance.getInstance();
    }

    public StaffService getStaffService() {
        return staffServiceInstance.getInstance();
    }

    public OrderService getOrderService() {
        return orderServiceInstance.getInstance();
    }

    public Payment getPaymentService(PaymentMethod paymentMethod) {
        return paymentInstance.getInstance();
    }
}

class RestaurantManagementSystem {
    private RestaurantFactory restaurantFactory = new RestaurantFactory();

    public void addFoodItem(FoodItem foodItem) {
        restaurantFactory.getMenu().addFoodItem(foodItem);
    }

    public List<FoodItem> getMenu() {
        return restaurantFactory.getMenu().getAllFoodItems();
    }

    public void hireStaff(StaffPerson staffPerson) {
        switch (staffPerson.getStaffRole()) {
            case CHEF:
                restaurantFactory.getStaffService().addChef(staffPerson);
                break;
            case WAITER:
                restaurantFactory.getStaffService().addWaiter(staffPerson);
                break;
            case CLEANING:
                restaurantFactory.getStaffService().addCleaner(staffPerson);
                break;
            case BILLING:
                restaurantFactory.getStaffService().addBillingStaff(staffPerson);
                break;
            default:
                throw new IllegalArgumentException("Invalid role!");
        }
    }

    public void placeOrder(FoodItem foodItem, int quantity) {
        Order order = new Order(foodItem, quantity);
        restaurantFactory.getOrderService().addOrder(order);
    }

    public Order getOrderDetail(String orderId) {
        return restaurantFactory.getOrderService().getOrder(orderId);
    }

    public Bill generateBill(Order order) {
        return new Bill("BILL" + order.getOrderId(), order);
    }

    public boolean makePayment(Bill bill, PaymentMethod paymentMethod) {
        Payment paymentService = restaurantFactory.getPaymentService(paymentMethod);
        paymentService.setPaymentMethod(paymentMethod);
        return paymentService.makePayment(bill);
    }

    public static void main(String[] args) {
        RestaurantManagementSystem rms = new RestaurantManagementSystem();

        // Adding food items
        FoodItem pizza = new FoodItem();
        pizza.setFoodId("1");
        pizza.setName("Pizza");
        pizza.setFoodCategory(FoodCategory.ITALIAN);
        pizza.setPrice(12.99);
        pizza.setDescription("Delicious cheesy pizza");
        pizza.setFoodStatus(FoodStatus.AVAILABLE);

        rms.addFoodItem(pizza);
        System.out.println("Menu: " + rms.getMenu());

        // Hiring staff
        StaffPerson chef = new StaffPerson() {
            { setPersonId("S1"); setName("John"); setStaffRole(StaffRole.CHEF); }
        };
        rms.hireStaff(chef);
        
        // Placing an order
        rms.placeOrder(pizza, 2);
        Order order = rms.getOrderDetail("1234");
        System.out.println("Order placed: " + order.getFoodItem().getName() + "\n");

        // Generating bill
        Bill bill = rms.generateBill(order);

        // Making payment
        boolean paymentStatus = rms.makePayment(bill, new CardPayment());

        if(paymentStatus == true) {
            System.out.println("Payment Successful: " + paymentStatus);
            rms.restaurantFactory.getOrderService().getOrder(order.getOrderId()).setOrderStatus(OrderStatus.SERVED);
            rms.restaurantFactory.getOrderService().getOrder(order.getOrderId()).setPaymentStatus(PaymentStatus.SUCCESS);

            // show order details
            System.out.println(bill.toString());

        } else {
            System.out.println("Payment Successful: " + paymentStatus);
            rms.restaurantFactory.getOrderService().getOrder(order.getOrderId()).setPaymentStatus(PaymentStatus.FAILED);
        }
    }
}
