import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/*
1. user
2. buyer
3. seller
4. product
5. auction
6. auction service
7. auction state store
8. bid
9. bid service
10. auction lock manager
11. auction status
12. notification service
13. email notification service
14. text notification service
*/

enum AuctionStatus {
    CREATED,
    RUNNING,
    CLOSED
}

class User {

    private final String userId;
    private String name;
    private String email;
    private String contactNum;
    private List<String> joinedAuctions;
    private List<String> ownedAuctions;

    public User(String name, String email, String contactNum) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.contactNum = contactNum;
        this.joinedAuctions = new ArrayList<>();
        this.ownedAuctions = new ArrayList<>();
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNum() {
        return contactNum;
    }

    public List<String> getJoinedAuctions() {
        return Collections.unmodifiableList(joinedAuctions);
    }

    // Business method
    public void joinAuction(String auctionId) {
        if (!joinedAuctions.contains(auctionId)) {
            joinedAuctions.add(auctionId);
        }
    }

    public void addOwnedAuction(String auctionId) {
        if (!ownedAuctions.contains(auctionId)) {
            ownedAuctions.add(auctionId);
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contactNum='" + contactNum + '\'' +
                '}';
    }
}

class Product {

    private final String productId;
    private String name;
    private String description;
    private Double basePrice;
    private final Date createdAt;

    public Product(String name, String description, Double basePrice) {
        this.productId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.createdAt = new Date();
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }
}

class Auction {

    private final String auctionId;
    private String name;
    private String description;
    private Product product;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final LocalDateTime createdAt;
    private String ownerId;
    private AuctionStatus status;

    public Auction(String name,
                   String description,
                   Product product,
                   LocalDateTime startTime,
                   LocalDateTime endTime,
                   String ownerId) {

        this.auctionId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.product = product;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = LocalDateTime.now();
        this.ownerId = ownerId;
        this.status = AuctionStatus.CREATED;
    }

    // Getters
    public String getAuctionId() {
        return auctionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    // Setters (allowed only before start)
    public void setName(String name) {
        validateUpdatable();
        this.name = name;
    }

    public void setDescription(String description) {
        validateUpdatable();
        this.description = description;
    }

    public void setProduct(Product product) {
        validateUpdatable();
        this.product = product;
    }

    public void setStartTime(LocalDateTime startTime) {
        validateUpdatable();
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        validateUpdatable();
        this.endTime = endTime;
    }

    // State change methods
    public void start() {
        this.status = AuctionStatus.RUNNING;
    }

    public void close() {
        this.status = AuctionStatus.CLOSED;
    }

    // Validation helpers
    private void validateUpdatable() {
        if (status != AuctionStatus.CREATED) {
            throw new IllegalStateException(
                    "Auction cannot be updated once started"
            );
        }
    }

    public boolean isRunning() {
        return status == AuctionStatus.RUNNING;
    }

    public boolean isOwner(String userId) {
        return ownerId.equals(userId);
    }
}


class Bid {

    private final String bidId;
    private final String userId;
    private final long amount;
    private final LocalDateTime timestamp;

    public Bid(String userId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bid amount must be positive");
        }
        this.bidId = UUID.randomUUID().toString();
        this.userId = userId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getBidId() {
        return bidId;
    }

    public String getUserId() {
        return userId;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "bidId='" + bidId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}

interface AuctionStore {
    void saveBid(String auctionId, Bid bid);

    Bid getHighestBid(String auctionId);

    List<Bid> getBidHistory(String auctionId);

    void clear(String auctionId);
}

class InMemoryAuctionStore implements AuctionStore {
    private final Map<String, List<Bid>> bidHistoryMap = new ConcurrentHashMap<>();
    private final Map<String, Bid> highestBidMap = new ConcurrentHashMap<>();

    public void saveBid(String auctionId, Bid bid) {
        bidHistoryMap.computeIfAbsent(auctionId, k -> new ArrayList<>()).add(bid);
        highestBidMap.put(auctionId, bid);
    }

    public Bid getHighestBid(String auctionId) {
        return highestBidMap.get(auctionId);
    }

    public List<Bid> getBidHistory(String auctionId) {
        return bidHistoryMap.getOrDefault(auctionId, new ArrayList<>());
    }

    public void clear(String auctionId) {
        bidHistoryMap.remove(auctionId);
        highestBidMap.remove(auctionId);
    }
}

class AuctionModel {

    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public AuctionModel() {}

    public AuctionModel(String name,
                        String description,
                        LocalDateTime startTime,
                        LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}

class AuctionLockManager {

    private static final AuctionLockManager INSTANCE = new AuctionLockManager();
    private final Map<String, ReentrantLock> auctionLocks = new ConcurrentHashMap<>();

    private AuctionLockManager() {}

    public static AuctionLockManager getInstance() {
        return INSTANCE;
    }

    public ReentrantLock getLock(String auctionId) {
        return auctionLocks.computeIfAbsent(auctionId, id -> new ReentrantLock());
    }

    public void removeLock(String auctionId) {
        auctionLocks.remove(auctionId);
    }
}

class AuctionService {
    private static final AuctionService INSTANCE = new AuctionService();
    Map<String, Auction> auctionMap = new ConcurrentHashMap<>();
    Map<String, List<String>> auctionUserMap = new ConcurrentHashMap<>();

    private AuctionService() { }

    public static AuctionService getInstance() {
        return INSTANCE;
    }

    public void createAuction(String name, String description, Product product, LocalDateTime startTime, LocalDateTime endTime, String ownerId) {
        Auction auction = new Auction(name, description, product, startTime, endTime, ownerId);
        auctionMap.put(auction.getAuctionId(), auction);
    }

    public Auction getAuctionById(String auctionId) {
        return auctionMap.get(auctionId);
    }

    public void updateAuction(String auctionId, AuctionModel model) {

        Auction auction = auctionMap.get(auctionId);

        if (auction == null) {
            throw new IllegalArgumentException("Auction not found");
        }

        if (auction.getStatus() != AuctionStatus.CREATED) {
            throw new IllegalStateException("Only CREATED auctions can be updated");
        }

        if (model.getName() != null) {
            auction.setName(model.getName());
        }

        if (model.getDescription() != null) {
            auction.setDescription(model.getDescription());
        }

        if (model.getStartTime() != null) {
            auction.setStartTime(model.getStartTime());
        }

        if (model.getEndTime() != null) {
            auction.setEndTime(model.getEndTime());
        }
    }

    public void deleteAuction(String auctionId) {
        auctionMap.remove(auctionId);
    }

    public void startAuction(String auctionId) {
        Auction auction = auctionMap.get(auctionId);
        if(auction != null && auction.getStatus() == AuctionStatus.CREATED) {
            auction.start();
        }
    }

    public void closeAuction(String auctionId) {
        Auction auction = auctionMap.get(auctionId);
        if(auction != null && auction.getStatus() == AuctionStatus.RUNNING) {
            auction.close();
        }
    }

    public void joinAuction(String auctionId, String userId) {
        if(auctionMap.containsKey(auctionId) && auctionMap.get(auctionId).getStatus() == AuctionStatus.CREATED) {
            auctionUserMap.computeIfAbsent(auctionId, k -> new ArrayList<>()).add(userId);
        } 
    }
}

class BidService {

    private static BidService INSTANCE;
    private AuctionService auctionService;
    private AuctionLockManager auctionLockManager;
    private AuctionStore auctionStateStore;
    private NotificationService notificationService;

    private BidService() {}

    public static synchronized BidService getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new BidService();
        }
        return INSTANCE;
    }

    // One-time initialization
    public void initialize(AuctionService auctionService, AuctionLockManager auctionLockManager, AuctionStore auctionStateStore, NotificationService notificationService) {

        if (this.auctionService != null) {
            throw new IllegalStateException("BidService already initialized");
        }

        this.auctionService = auctionService;
        this.auctionLockManager = auctionLockManager;
        this.auctionStateStore = auctionStateStore;
        this.notificationService = notificationService;
    }

    public void placeBid(String auctionId, Bid bid) {

        ReentrantLock lock = auctionLockManager.getLock(auctionId);
        lock.lock();

        try {
            Auction auction = auctionService.getAuctionById(auctionId);

            if (auction == null) {
                throw new IllegalArgumentException("Auction not found");
            }

            if (!auction.isRunning()) {
                throw new IllegalStateException("Auction is not running");
            }

            Bid highestBid = auctionStateStore.getHighestBid(auctionId);

            if (highestBid != null &&
                    bid.getAmount() <= highestBid.getAmount()) {
                throw new IllegalArgumentException(
                        "Bid must be higher than current highest bid"
                );
            }

            auctionStateStore.saveBid(auctionId, bid);

            notificationService.notifyUsers(this.auctionService.auctionUserMap.get(auctionId), bid);

        } 
        catch(IllegalArgumentException e) {
            System.err.println("wrong bid by user " + bid.getUserId() +" bid must be higher than current highest bid");
        }
        finally {
            lock.unlock();
        }
    }

    public List<Bid> getBidHistory(String auctionId) {
        return auctionStateStore.getBidHistory(auctionId);
    }

    public Bid getHighestBid(String auctionId) {
        return auctionStateStore.getHighestBid(auctionId);
    }
}

interface NotificationService {
    public void notifyUsers(List<String> user, Bid bid);
}

class TextNotificationService implements NotificationService {

    @Override
    public void notifyUsers(List<String> users, Bid bid) {

        if (users == null || users.isEmpty()) {
            return;
        }

        for (String userId : users) {

            System.out.println(
                "[TEXT] Notification sent to User: " + userId +
                " | New Bid Placed: ₹" + bid.getAmount() +
                " by User: " + bid.getUserId() +
                " at " + bid.getTimestamp()
            );

        }
    }
}

class EmailNotificationService implements  NotificationService {
    public void notifyUsers(List<String> users, Bid bid) {
        for(String userId : users) {
            // send email
        }
    }
}

public class AuctionManagementSystem {

    public static void main(String[] args) throws InterruptedException {

        // Initialize Services
        AuctionService auctionService = AuctionService.getInstance();
        NotificationService notificationService =
                new TextNotificationService();
        AuctionLockManager auctionLockManager =
                AuctionLockManager.getInstance();
        AuctionStore auctionStore =
                new InMemoryAuctionStore();

        BidService bidService = BidService.getInstance();
        bidService.initialize(
                auctionService,
                auctionLockManager,
                auctionStore,
                notificationService
        );

        // Create Users
        User owner = new User("Owner1", "owner@mail.com", "9999999999");

        User buyer1 = new User("Buyer1", "b1@mail.com", "1111");
        User buyer2 = new User("Buyer2", "b2@mail.com", "2222");
        User buyer3 = new User("Buyer3", "b3@mail.com", "3333");
        User buyer4 = new User("Buyer4", "b4@mail.com", "4444");

        // Owner creates product
        Product product = new Product(
                "iPhone 15",
                "Brand new sealed",
                50000.0
        );

        // Owner creates auction
        auctionService.createAuction(
                "iPhone Auction",
                "Bidding for iPhone 15",
                product,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                owner.getUserId()
        );

        // fetch created auctionId
        String auctionId =
                auctionService.auctionMap.keySet()
                        .iterator().next();

        System.out.println("Auction Created: " + auctionId);

        // Buyers join auction
        auctionService.joinAuction(auctionId, buyer1.getUserId());
        auctionService.joinAuction(auctionId, buyer2.getUserId());
        auctionService.joinAuction(auctionId, buyer3.getUserId());
        auctionService.joinAuction(auctionId, buyer4.getUserId());

        // Owner starts auction
        auctionService.startAuction(auctionId);
        System.out.println("Auction Started");

        // Buyers place bids concurrently
        Thread t1 = new Thread(() ->
                bidService.placeBid(
                        auctionId,
                        new Bid(buyer1.getUserId(), 52000)
                )
        );

        Thread t2 = new Thread(() ->
                bidService.placeBid(
                        auctionId,
                        new Bid(buyer2.getUserId(), 54000)
                )
        );

        Thread t3 = new Thread(() ->
                bidService.placeBid(
                        auctionId,
                        new Bid(buyer3.getUserId(), 56000)
                )
        );

        Thread t4 = new Thread(() ->
                bidService.placeBid(
                        auctionId,
                        new Bid(buyer4.getUserId(), 58000)
                )
        );

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        // Owner closes auction
        auctionService.closeAuction(auctionId);
        System.out.println("Auction Closed");

        // Show Winner
        Bid winningBid =
                bidService.getHighestBid(auctionId);

        System.out.println("\n Winner Details:");
        System.out.println("User: " + winningBid.getUserId() + " | Amount: rs" + winningBid.getAmount()
        );
    }
}
