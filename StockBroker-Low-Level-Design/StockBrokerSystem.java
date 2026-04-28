import java.util.*;

enum OrderType { BUY, SELL }
enum OrderStatus { PENDING, PARTIAL, FILLED }

class User {
    String userId;
    String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}

class Wallet {
    double available;
    double reserved;

    public Wallet(double balance) {
        this.available = balance;
        this.reserved = 0;
    }

    public boolean reserve(double amount) {
        if (available >= amount) {
            available -= amount;
            reserved += amount;
            return true;
        }
        return false;
    }

    public void settle(double usedAmount) {
        reserved -= usedAmount;
    }

    public void refund(double amount) {
        reserved -= amount;
        available += amount;
    }
}

class Holding {
    String stockId;
    double quantity;
    double avgPrice;

    public Holding(String stockId, double quantity, double avgPrice) {
        this.stockId = stockId;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
    }
}

class Portfolio {
    Map<String, Holding> holdings = new HashMap<>();

    public void buy(String stockId, double qty, double price) {
        Holding h = holdings.getOrDefault(stockId, new Holding(stockId, 0, 0));

        double totalCost = (h.avgPrice * h.quantity) + (price * qty);
        h.quantity += qty;
        h.avgPrice = totalCost / h.quantity;

        holdings.put(stockId, h);
    }

    public void sell(String stockId, double qty) {
        Holding h = holdings.get(stockId);
        if (h == null) return;

        h.quantity -= qty;
        if (h.quantity == 0) holdings.remove(stockId);
    }

    public double getQuantity(String stockId) {
        return holdings.getOrDefault(stockId, new Holding(stockId, 0, 0)).quantity;
    }
}

class Order {
    String orderId;
    String userId;
    String stockId;
    double price;
    double quantity;
    OrderType type;
    OrderStatus status;

    public Order(String orderId, String userId, String stockId, double price, double quantity, OrderType type) {
        this.orderId = orderId;
        this.userId = userId;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.status = OrderStatus.PENDING;
    }
}

class Trade {
    String tradeId;
    String buyOrderId;
    String sellOrderId;
    double price;
    double quantity;

    public Trade(String id, String b, String s, double p, double q) {
        tradeId = id;
        buyOrderId = b;
        sellOrderId = s;
        price = p;
        quantity = q;
    }
}

class OrderBook {
    PriorityQueue<Order> buyHeap = new PriorityQueue<>(
            (a, b) -> Double.compare(b.price, a.price));

    PriorityQueue<Order> sellHeap = new PriorityQueue<>(
            Comparator.comparingDouble(a -> a.price));
}

class UserAccount {
    User user;
    Wallet wallet;
    Portfolio portfolio;

    public UserAccount(User user, double balance) {
        this.user = user;
        this.wallet = new Wallet(balance);
        this.portfolio = new Portfolio();
    }
}

class TradeService {
    Map<String, UserAccount> users;
    List<TradeListener> listeners = new ArrayList<>();

    public TradeService(Map<String, UserAccount> users) {
        this.users = users;
    }

    public void registerListener(TradeListener listener) {
        listeners.add(listener);
    }

    public void executeTrade(Trade trade, Order buy, Order sell) {
        UserAccount buyer = users.get(buy.userId);
        UserAccount seller = users.get(sell.userId);

        double amount = trade.price * trade.quantity;

        buyer.wallet.settle(amount);
        seller.wallet.available += amount;

        buyer.portfolio.buy(buy.stockId, trade.quantity, trade.price);
        seller.portfolio.sell(sell.stockId, trade.quantity);

        System.out.println("TRADE EXECUTED: " + trade.quantity + " @ " + trade.price);

        for (TradeListener listener : listeners) {
            listener.onTrade(trade, buy, sell);
        }
    }
}

class MatchingEngine {
    OrderBook book;
    TradeService tradeService;
    int tradeCounter = 1;

    public MatchingEngine(OrderBook book, TradeService tradeService) {
        this.book = book;
        this.tradeService = tradeService;
    }

    public void placeOrder(Order order, UserAccount user) {
        if (order.type == OrderType.BUY) {
            double total = order.price * order.quantity;
            if (!user.wallet.reserve(total)) {
                System.out.println("Insufficient balance");
                return;
            }
            book.buyHeap.add(order);
        } else {
            if (user.portfolio.getQuantity(order.stockId) < order.quantity) {
                System.out.println("Insufficient stock");
                return;
            }
            book.sellHeap.add(order);
        }

        match();
    }

    private void match() {
        while (!book.buyHeap.isEmpty() && !book.sellHeap.isEmpty()) {
            Order buy = book.buyHeap.peek();
            Order sell = book.sellHeap.peek();

            if (buy.price < sell.price) break;

            double qty = Math.min(buy.quantity, sell.quantity);
            double price = sell.price;

            Trade trade = new Trade(
                    "T" + tradeCounter++,
                    buy.orderId,
                    sell.orderId,
                    price,
                    qty
            );

            tradeService.executeTrade(trade, buy, sell);

            buy.quantity -= qty;
            sell.quantity -= qty;

            if (buy.quantity == 0) {
                buy.status = OrderStatus.FILLED;
                book.buyHeap.poll();
            } else {
                buy.status = OrderStatus.PARTIAL;
            }

            if (sell.quantity == 0) {
                sell.status = OrderStatus.FILLED;
                book.sellHeap.poll();
            } else {
                sell.status = OrderStatus.PARTIAL;
            }
        }
    }
}

interface TradeListener {
    void onTrade(Trade trade, Order buy, Order sell);
}

class NotificationService implements TradeListener {

    @Override
    public void onTrade(Trade trade, Order buy, Order sell) {

        System.out.println("Notification:");

        System.out.println("Buyer (" + buy.userId + ") bought " +
                trade.quantity + " of " + buy.stockId +
                " at price " + trade.price);

        System.out.println("Seller (" + sell.userId + ") sold " +
                trade.quantity + " of " + sell.stockId +
                " at price " + trade.price);
    }
}

public class StockBrokerSystem {
    public static void main(String[] args) {

        Map<String, UserAccount> users = new HashMap<>();

        UserAccount u1 = new UserAccount(new User("U1", "Alice"), 10000);
        UserAccount u2 = new UserAccount(new User("U2", "Bob"), 0);

        u2.portfolio.buy("TCS", 10, 100);

        users.put("U1", u1);
        users.put("U2", u2);

        OrderBook book = new OrderBook();
        TradeService tradeService = new TradeService(users);
        NotificationService notificationService = new NotificationService();
        tradeService.registerListener(notificationService);
        
        MatchingEngine engine = new MatchingEngine(book, tradeService);

        Order buy = new Order("O1", "U1", "TCS", 120, 5, OrderType.BUY);
        engine.placeOrder(buy, u1);

        Order sell = new Order("O2", "U2", "TCS", 110, 5, OrderType.SELL);
        engine.placeOrder(sell, u2);

        System.out.println("Alice Balance: " + u1.wallet.available);
        System.out.println("Alice TCS Qty: " + u1.portfolio.getQuantity("TCS"));

        System.out.println("Bob Balance: " + u2.wallet.available);
        System.out.println("Bob TCS Qty: " + u2.portfolio.getQuantity("TCS"));
    }
}
