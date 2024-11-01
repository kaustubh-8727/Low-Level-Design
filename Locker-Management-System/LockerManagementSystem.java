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

enum DeliveryManStatus {
    AVAILABLE,
    NOT_AVAILABLE
}

enum OrderStatus {
    IN_PROGRESS,
    IN_DELIVERY,
    DELIVERED,
    RECEIVED
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
    string userId;
    String name;
    String contactNum;
    String emailId;
    Location address;
    
    public User(String name, String contactNum, String emailId, Location address) {
        this.userId = "1234";
        this.name = name;
        this.contactNum = contactNum;
        this.emailId = emailId;
        this.address = address;
    }
}

class DeliveryMan {
    String deliveryManId;
    String name;
    String contactNum;
    String emailId;
    Location address;
    DeliveryManStatus deliveryManStatus;
    Order order;
    
    public User(String name, String contactNum, String emailId, Location address) {
        this.deliveryManId = "1234";
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
}

class DeliveryManService {
    List<DeliveryMan> deliveryManList = new ArrayList<>();
    
    public void addDeliveryMan(DeliveryMan deliveryMan) {
        deliveryManList.add(deliveryMan);
    }
    
    public void removeDeliveryMan(String deliveryManId) {
        for(DeliveryMan deliveryMan : deliveryManList) {
            if(deliveryMan.getId() == deliveryManId) {
                return deliveryManList.remove(deliveryMan);
            }
        }
    }
    
    public DeliveryMan getDeliveryMan() {
        for(DeliveryMan deliveryMan : deliveryManList) {
            if(deliveryMan.getStatus() == DeliveryManStatus.AVAILABLE) {
                return deliveryMan;
            }
        }
        return null;
    }
    
    public void assignDeliveryMan(DeliveryMan deliveryMan, Order order) {
        deliveryMan.setDeliveryManStatus(DeliveryManStatus.NOT_AVAILABLE);
        deliveryMan.setOrder(order).
    }
}

class Locker {
    String lockerId;
    LockerCategory lockerCategory;
    LockerStatus lockerStatus
    
    public Locker(LockerCategory lockerCategory) {
        this.lockerId = "1234";
        this.lockerCategory = lockerCategory;
        this.lockerStatus = LockerStatus.EMPTY;
    }
    
    public void setLockerStatus(LockerStatus lockerStatus) {
        this.lockerStatus = lockerStatus;
    }
}

class LockerService {
    LockerAllocationStrategy lockerAllocationStrategy;
    Locker[][] lockerGrid;
    int lockerGridSize = 0;
    
    public LockerService(LockerAllocationStrategy lockerAllocationStrategy, int lockerGridSize) {
        this.lockerAllocationStrategy = lockerAllocationStrategy;
        this.lockerGridSize = lockerGridSize;
        lockerGrid = new [lockerGridSize][lockerGridSize];
        intializeAllLockers();
    }
    
    public void intializeAllLockers() {
        for(int row = 0 ; row < lockerGridSize ; row++) {
            for(int col = 0 col < lockerGridSize ; col++) {
                if(row % 2 == 0) {
                    lockerGrid[row][col] = new Locker(lockerCategory.SMALL);
                } else {
                    lockerGrid[row][col] = new Locker(lockerCategory.LARGE);
                }
            }
        }
    }
}

class Order {
    String orderId;
    String productName;
    double price;
    OrderStatus orderStatus;
    boolean lockerNeeded;
    
    public Order(String productName, double price, boolean lockerNeeded) {
        this.productName = productName;
        this.price = price;
        this.lockerNeeded = lockerNeeded;
        this.orderStatus = OrderStatus.IN_PROGRESS;
    }
    
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}


public class LockerManagementSystem {
	public static void main(String[] args) {
		System.out.println("Hello World");
	}
}
