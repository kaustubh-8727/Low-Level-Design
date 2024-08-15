import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

enum OrderStatus {
    INTIATED,
    COMPLETED,
    CANCELLED,
    FAILED
}

class Product {
    int productId;
    String productName;

    public Product(String productName) {
        this.productId = (int)(Math.random() * 9000) + 1000;
        this.productName = productName;
    }
}

class ProductCategory {
    int productCategoryId;
    String productCategoryName;
    double price;
    int stock;
    List<Product> productList = new ArrayList<>();

    public ProductCategory(int productCategoryId, String productCategoryName, double price) {
        this.productCategoryId = productCategoryId;
        this.productCategoryName = productCategoryName;
        this.price = price;
    }

    public void addProduct(Product product) {
        productList.add(product);
        stock++;
    }

    public void removeProduct(int productId) {
        // logic to remove the product
    }

    public void removeProducts(int count) {
        stock = stock - count;
        while(count != 0) {
            productList.remove(0);
            count--;
        }
    }

    public double getPrice() {
        return price;
    }
}

class ProductInventory {
    int productInventoryId;
    List<ProductCategory> productCategoryList = new ArrayList<>();

    public ProductInventory() {
        this.productInventoryId = (int)(Math.random() * 9000) + 1000;
    }

    public void addProductCategory(ProductCategory productCategory) {
        productCategoryList.add(productCategory);
    }

    public void removeProductCategory(int productCategoryId) {
        // logic to remove the product category
    }

    public void addProducts(Map<Integer, Integer> productCategoryCountMap) {
        // logic to add products in inventory
    }

    public void removeProducts(Map<Integer, Integer> productCategoryCountMap) {
        for(Map.Entry<Integer, Integer> entry : productCategoryCountMap.entrySet()) {
            int productCategoryId = entry.getKey();
            int count = entry.getValue();
            ProductCategory productCategory = getProductCategoryFromID(productCategoryId);
            if(productCategory != null) {
                productCategory.removeProducts(count);
            }
        }
    }

    private ProductCategory getProductCategoryFromID(int productCategoryId){
        for(ProductCategory productCategory : productCategoryList){
            if(productCategory.productCategoryId == productCategoryId){
                return productCategory;
            }
        }
        return null;
    }

    public ProductCategory getProductCategoryFromName(String categoryName){
        for(ProductCategory productCategory : productCategoryList){
            if(productCategory.productCategoryName == categoryName){
                return productCategory;
            }
        }
        return null;
    }
}

class Warehouse {
    int warehouseId;
    ProductInventory productInventory;
    Address address;

    public Warehouse(Address address) {
        this.warehouseId = (int)(Math.random() * 9000) + 1000;
        this.address = address;
    }

    public void addInventory(ProductInventory inventory) {
        productInventory = inventory;
    }

    public ProductInventory getInventory() {
        return productInventory;
    }

    public void removeInventory() {
        // logic to remove inventory
    }

    public void removeProducts(Map<Integer, Integer> productCategoryCountMap) {
        productInventory.removeProducts(productCategoryCountMap);
    }

    public void addProducts(Map<Integer, Integer> productCategoryCountMap) {
        productInventory.addProducts(productCategoryCountMap);
    }
}

class WarehouseManagement {
    List<Warehouse> warehouseList = new ArrayList<>();

    public void addWarehouse(Warehouse warehouse) {
        warehouseList.add(warehouse);
    }

    public void removeWarehouse(int warehouseId) {
        // logic to remove warehouse
    }

    public Warehouse getWarehouse(WarehouseSelectionStrategy selectionStrategy) {
        return selectionStrategy.getWarehouse(warehouseList);
    }
}

interface WarehouseSelectionStrategy {
    public Warehouse getWarehouse(List<Warehouse> warehouseList);
}

class CheapestWarehouseSelectionStrategy implements WarehouseSelectionStrategy {
    public Warehouse getWarehouse(List<Warehouse> warehouseList) {
        return warehouseList.get(0);
    }
}

class ClosestWarehouseSelectionStrategy implements WarehouseSelectionStrategy {

    Address address;

    public ClosestWarehouseSelectionStrategy(Address address) {
        this.address = address;
    }

    public Warehouse getWarehouse(List<Warehouse> warehouseList) {
        return warehouseList.get(0);
    }
}

class User {
    int userId;
    String userName;
    Address address;
    Cart cart;

    public User(String userName, Address address) {
        this.userId = (int)(Math.random() * 9000) + 1000;
        this.userName = userName;
        this.address = address;
        cart = new Cart();
    }

    public void addProductToCart(ProductCategory product, int count) {
        cart.addProductToCart(product, count);
    }

    public void removeProductFromCart(ProductCategory product, int count) {
        cart.removeProductFromCart(product, count);
    }

    public Cart getCart() {
        return cart;
    }
}

class Cart {
    Map<Integer, Integer> productCartMap = new HashMap<>();

    public void addProductToCart(ProductCategory product, int count) {
        if(productCartMap.containsKey(product.productCategoryId)) {
            count += productCartMap.get(product.productCategoryId);
        }
        productCartMap.put(product.productCategoryId, count);
    }

    public void removeProductFromCart(ProductCategory product, int count) {
        if(productCartMap.containsKey(product.productCategoryId)) {
            int currentCount = productCartMap.get(product.productCategoryId);
            if(count < currentCount) {
                productCartMap.put(product.productCategoryId, currentCount - count);
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
    Warehouse warehouse;
    Invoice invoice;
    Payment payment;

    public Order(User user, Warehouse warehouse) {
        this.user = user;
        this.productCategoryCountMap = user.getCart().productCartMap;
        this.warehouse = warehouse;
        invoice = new Invoice();
        invoice.generateInvoice(productCategoryCountMap);
        status = OrderStatus.INTIATED;
    }

    public void checkout() {
        boolean paymentDone = makePayment(new UpiPayment());

        if(paymentDone) {
            cleanUp();
            status = OrderStatus.COMPLETED;
            getCompleteOrderDetails();
        } else {
            status = OrderStatus.FAILED;
            System.out.println("payment failed");
        }
    }

    public boolean makePayment(PaymentMode paymentMode) {
        payment = new Payment();
        return payment.makePayment(paymentMode, invoice.getTotalAmount());
    }

    public void cleanUp() {
        warehouse.removeProducts(productCategoryCountMap);
    }

    public void getCompleteOrderDetails() {
        System.out.println(
            "your order had been placed\n" +
            "total amount payed : " + invoice.getTotalAmount()
        );
    }
}

class OrderController {

    List<Order> orderList;
    Map<Integer, List<Order>> userIdOrdersMap;

    OrderController(){
        orderList = new ArrayList<>();
        userIdOrdersMap = new HashMap<>();
    }

    public Order createNewOrder(User user, Warehouse warehouse){
        Order order = new Order(user, warehouse);
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

    public void checkoutOrder(Order order) {
        order.checkout();
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

class Address {
    String country;
    String state;
    String city;
    String pincode;
    
    public Address(String country, String state, String city, String pincode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
    }
}

public class OnlineOrderManagementSystem {
	public static void main(String[] args) {

	        // create user address
	        Address userAddress = new Address("USA", "Arizona", "Phoenix", "85001");
	
	        //create warehouse address
	        Address warehouseAddress = new Address("USA", "New York", "Albany", "12207");

		// create products
		Product product1 = new Product("IPhone1");
		Product product2 = new Product("IPhone2");
		Product product3 = new Product("pepsi1");
		Product product4 = new Product("pepsi2");

	        // create product categories
	        ProductCategory productCategory1 = new ProductCategory(1001, "IPhone category", 80000.0);
	        productCategory1.addProduct(product1);
	        productCategory1.addProduct(product2);
	
	        ProductCategory productCategory2 = new ProductCategory(1002, "pepsi category", 20.00);
	        productCategory1.addProduct(product3);
	        productCategory1.addProduct(product4);
	
	        // create inventory
	        ProductInventory productInventory = new ProductInventory();
	        productInventory.addProductCategory(productCategory1);
	        productInventory.addProductCategory(productCategory2);
	
	        // create warehouse
	        Warehouse warehouse = new Warehouse(warehouseAddress);
	        warehouse.addInventory(productInventory);
	
	        // intialize warehouse controller
	        WarehouseManagement warehouseManagement = new WarehouseManagement();
	        warehouseManagement.addWarehouse(warehouse);
	
	        // intialize order controller
	        OrderController orderManagement = new OrderController();
	
	        // create user
	        User user1 = new User("alley", userAddress);
	        User user2 = new User("mike", userAddress);
	
	        // start the order management system
	
	        // 1. user comes
	        User user = user1;
	
	        // 2. get nearest warehouse
	        Warehouse nearestWarehouse = warehouseManagement.getWarehouse(new ClosestWarehouseSelectionStrategy(userAddress));
	
	        // 3. get the inventory to select the products
	        productInventory = nearestWarehouse.getInventory();
	
	        // 4. select the product that user wants
	        String userPreference = "IPhone category";
	        ProductCategory productCategory = productInventory.getProductCategoryFromName(userPreference);
	
	        // 5. user add product to the cart
	        user.addProductToCart(productCategory, 2);
	
	        // 6. user intiates the order
	        Order order = orderManagement.createNewOrder(user, nearestWarehouse);
	
	        // 7. user confirms order and checkout
	        orderManagement.checkoutOrder(order);
	}
}
