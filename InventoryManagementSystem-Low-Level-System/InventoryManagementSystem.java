
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


enum ReservationStatus {
    ACTIVE,
    CONFIRMED,
    EXPIRED,
    CANCELLED
}

class Product {
    String productId;
    String name;
    String description;
    String category;
}

class InventoryItem {
    String productId;
    int availableStock;
    int reservedStock;
    long version;
}

class InventoryReservation {
    String reservationId;
    String productId;
    int quantity;
    ReservationStatus status;
    long expiryTime;
}

interface InventoryStore {

    public InventoryItem getProductInventory(String productId) throws Exception;
    public void saveProductInventory(InventoryItem item) throws Exception;
    public boolean reserveStock(String productId, int stock) throws Exception;
    public boolean deductStock(String productId, int stock) throws Exception;
    public boolean releaseStock(String productId, int stock) throws Exception;
    public boolean updateStock(String productId, int stock) throws Exception;
}

class InMemoryInventoryStore implements InventoryStore {

    private final ConcurrentHashMap<String, InventoryItem> productStore =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ReentrantLock> productLocker =
            new ConcurrentHashMap<>();


    @Override
    public InventoryItem getProductInventory(String productId) throws Exception {

        InventoryItem item = productStore.get(productId);

        if(item == null) {
            throw new Exception("Product not found: " + productId);
        }

        return item;
    }

    @Override
    public void saveProductInventory(InventoryItem item) throws Exception {

        if(productStore.containsKey(item.productId)) {
            throw new Exception("Product already exists: " + item.productId);
        }

        productStore.put(item.productId, item);
        productLocker.put(item.productId, new ReentrantLock());
    }

    public boolean reserveStock(String productId, int quantity) throws Exception {

        ReentrantLock lock = getProductLock(productId);

        lock.lock();

        try {

            InventoryItem item = getProductInventory(productId);

            if(item.availableStock < quantity) {
                return false;
            }

            item.availableStock -= quantity;
            item.reservedStock += quantity;

            return true;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean deductStock(String productId, int quantity) throws Exception {

        ReentrantLock lock = getProductLock(productId);

        lock.lock();

        try {

            InventoryItem item = getProductInventory(productId);

            if(item.reservedStock < quantity) {
                return false;
            }

            item.reservedStock -= quantity;

            return true;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean releaseStock(String productId, int quantity) throws Exception {

        ReentrantLock lock =
                getProductLock(productId);

        lock.lock();

        try {

            InventoryItem item = getProductInventory(productId);

            if(item.reservedStock < quantity) {
                return false;
            }

            item.reservedStock -= quantity;
            item.availableStock += quantity;

            return true;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean updateStock(String productId, int quantity) throws Exception {

        ReentrantLock lock =
                getProductLock(productId);

        lock.lock();

        try {

            InventoryItem item =
                    getProductInventory(productId);

            item.availableStock += quantity;

            return true;

        } finally {
            lock.unlock();
        }
    }

    private ReentrantLock getProductLock(String productId) throws Exception {

        ReentrantLock lock = productLocker.get(productId);

        if(lock == null) {
            throw new Exception("Lock not found for product: " + productId);
        }

        return lock;
    }
}

interface ReplenishStrategy {

    void replenish(String productId);
}

class ThresholdReplenishStrategy implements ReplenishStrategy {

    private static final int THRESHOLD = 20;
    private static final int REPLENISH_QTY = 100;

    private InventoryStore inventoryStore;

    public ThresholdReplenishStrategy(
            InventoryStore inventoryStore) {
        this.inventoryStore = inventoryStore;
    }

    @Override
    public void replenish(String productId) {

        try {

            InventoryItem item = inventoryStore.getProductInventory(productId);

            if(item.availableStock < THRESHOLD) {

                inventoryStore.updateStock(productId, REPLENISH_QTY);
                System.out.println("Threshold replenishment triggered");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

class WeeklyReplenishStrategy implements ReplenishStrategy {

    private static final int WEEKLY_REPLENISH_QTY = 500;
    private InventoryStore inventoryStore;

    public WeeklyReplenishStrategy(
            InventoryStore inventoryStore) {

        this.inventoryStore = inventoryStore;
    }

    @Override
    public void replenish(String productId) {

        try {

            if(LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY) {

                inventoryStore.updateStock(productId, WEEKLY_REPLENISH_QTY);

                System.out.println("Weekly replenishment triggered");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

class InventoryManager {

    InventoryStore inventoryStore;
    ReplenishStrategy replenishStrategy;

    public InventoryManager(InventoryStore inventoryStore, ReplenishStrategy replenishStrategy) {
        this.inventoryStore = inventoryStore;
        this.replenishStrategy = replenishStrategy;
    }

    public void addProduct(String productId, int stock) {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.productId = productId;
        inventoryItem.availableStock = stock;
        inventoryItem.reservedStock = 0;
        inventoryItem.version = 1;
        
        try {
            inventoryStore.saveProductInventory(inventoryItem);
        }
        catch(Exception e) {
            System.err.println("add product failed " + e);
        }
    }

    public void reserveStock(String productId, int stock) {

        try {
            inventoryStore.reserveStock(productId, stock);
        } catch (Exception e) {
            System.err.println("reserve stock failed " + e);
        }
    }

    public void confirmOrder(String productId, int quantity) {

        try {
            inventoryStore.deductStock(productId, quantity);
        } catch (Exception e) {
            System.err.println("stock update failed " + e);
        }
    }

    public void releaseReservation(String productId, int quantity) {

        try {
            inventoryStore.releaseStock(productId, quantity);
        } catch (Exception e) {
            System.err.println("release update failed " + e);
        }
    }
}

class InventoryManagementSystem {
    public static void main(String[] args) {
        System.out.println("");
    }
}
