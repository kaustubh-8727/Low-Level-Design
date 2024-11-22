/*
Stock Brokerage System

1. user - details of user
2. user account - details of user account (like balance, add balance, deduct balance)
3. user management - manages all the users.
4. user portfolio - keeps history of all user stocks buy/sell, maintains the current profit, loss, etc.
5. stock - class that keeps a particular stock information.
6. stocks management - keeps all the stocks, update the stocks, remove the stocks, update prices of stocks, etc.
7. order - creates a buy or sell order.
8. buy order class
9. sell order class.
10. order management - manages all the orders (stores orders in queue to process), call specific order to execute.
11. stock market service - singleton class that performs the actual actions.
12. stock filter - a stock filter which will return the stocks based on filters applied.

*/

import java.util.*;

class User {
    String userId;
    String userName;
    String email;
    String contactNumber;
    
    public User(String userName, String email, String contactNumber) {
        this.userId = "1234";
        this.userName = userName;
        this.email = email;
        this.contactNumber = contactNumber;
    }

}

class UserAccount {
    String accountId;
    User user;
    Portfolio portfolio;
    double balance;
    
    public UserAccount(User user, Portfolio portfolio, double balance) {

        this.accountId = "1234";
        this.user = user;
        this.portfolio = portfolio;
        this.balance = balance;
    }
    
    public void creditBalance(double balance) {
        this.balance += balance;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void debitBalance(double balance) {
        this.balance -= balance;
    }
    
    public User getUser() {
        return user;
    }
    
    public Portfolio getPortfolio() {
        return portfolio;
    }
}

class UserStock {
    Stock stock;
    double quantity;
    double priceBought;
    
    public UserStock(Stock stock, double quantity, double priceBought) {
        this.stock = stock;
        this.quantity = quantity;
        this.priceBought = priceBought;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public Stock getStock() {
        return stock;
    }
}

class Portfolio {
    
    String portfolioId;
    Map<String, UserStock> stocksHolding = new HashMap<>();
    Map<String, Order> orderHistory = new HashMap<>();
    double currentProfit;
    double totalAmountInvested;
    double currentLoss;
    
    public Portfolio() {
        this.portfolioId = "1234";
    }
    
    public void buyStockUpdate(Order order) {
        UserStock userStock = createUserStock(order);
        stocksHolding.put(order.getStock().getStockId(), userStock);
        orderHistory.put(order.getOrderId(), order);
        
        this.updatePortfolioBalance();
    }
    
    public UserStock createUserStock(Order order) {
        Stock stock = order.getStock();
        double quantity = order.getQuantity();
        double price = stock.getStockValue();
        
        UserStock userStock = new UserStock(stock, quantity, price);
        
        return userStock;
    }
    
    public void sellStockUpdate(Order order) {
        String stockId = order.getStock().getStockId();
        
        if(stocksHolding.containsKey(stockId)) {
            stocksHolding.remove(stockId);
        }
        if(orderHistory.containsKey(order.getOrderId())) {
            orderHistory.remove(order.getOrderId());
        }
        
        this.updatePortfolioBalance();
    }
    
    public UserStock getStock(Stock stock) {
        String stockId = stock.getStockId();
        
        if(!stocksHolding.containsKey(stockId)) {
            return null;
        }
        
        return stocksHolding.get(stockId);
    } 
    
    public void updatePortfolioBalance() {
        currentProfit = 0.0;
        currentLoss = 0.0;
        totalAmountInvested = 0.0;
    
        for (UserStock userStock : stocksHolding.values()) {
            double investedAmount = userStock.getQuantity() * userStock.priceBought;
            double currentValue = userStock.getQuantity() * userStock.getStock().getStockValue();
            
            totalAmountInvested += investedAmount;
    
            if (currentValue > investedAmount) {
                currentProfit += currentValue - investedAmount;
            } else {
                currentLoss += investedAmount - currentValue;
            }
        }
    }
}

class UserManagement {
    
    private static UserManagement instance;
    
    List<UserAccount> allUsers = new ArrayList<>();
    
    public static synchronized UserManagement getInstance() {
        if (instance == null) {
            instance = new UserManagement();
        }
        return instance;
    }
    
    public void addUser(UserAccount userAccount) {
        allUsers.add(userAccount);
    }
    
    public void removeUser(UserAccount userAccount) {
        allUsers.remove(userAccount);
    }
    
    public UserAccount getUserAccount(String userId) {
        for(UserAccount userAccount : allUsers) {
            if(userAccount.accountId == userId) {
                return userAccount;
            }
        }
        return null;
    }
}

class Stock {
    private String stockId;
    private String stockName;
    private double stockValue;

    public Stock(String stockName, double stockValue) {
        this.stockId = "1234";
        this.stockName = stockName;
        this.stockValue = stockValue;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getStockValue() {
        return stockValue;
    }

    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }
}

class StockManagement {
    
    private static StockManagement instance;
    
    public static synchronized StockManagement getInstance() {
        if (instance == null) {
            instance = new StockManagement();
        }
        return instance;
    }

    List<Stock> stocksList = new ArrayList<>();
    
    public void addStock(Stock stock) {
        stocksList.add(stock);
    }
    
    public void removeStokc(Stock stock) {
        stocksList.remove(stock);
    }
    
    public List<Stock> getStocks(StockFilter stockFilter) {
        if(stockFilter == null) {
            return stocksList;
        }
        return stocksList;
    }
    
    public void updateStockValue(String stockId, double stockValue) {

        for(Stock stock : stocksList) {
            if(stock.getStockId() == stockId) {
                stock.setStockValue(stockValue);
            }
        }
    }
}

class StockFilter {
    
}

abstract class Order {
    String orderId;
    UserAccount userAccount;
    Stock stock;
    double quantity;
    
    public Order(UserAccount userAccount, Stock stock, double quantity) {
        this.orderId = "1234";
        this.userAccount = userAccount;
        this.stock = stock;
        this.quantity = quantity;
    }
    
    public Stock getStock() {
        return stock;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    abstract public void processOrder();
}

class BuyOrder extends Order {
    
    public BuyOrder(UserAccount userAccount, Stock stock, double quantity) {
        super(userAccount, stock, quantity);
    }
    
    double price = quantity * stock.getStockValue();
    
    public void processOrder() {
        
        if(this.validateOrder() == false) {
            // throw exception
        }
        
        userAccount.debitBalance(price);
        userAccount.getPortfolio().buyStockUpdate(this);
    }
    
    public boolean validateOrder() {
        boolean isValid = true;
        if(userAccount.getBalance() < price) {
            isValid = false;
        }
        return isValid;
    }
}

class SellOrder extends Order {
    
    public SellOrder(UserAccount userAccount, Stock stock, double quantity) {
        super(userAccount, stock, quantity);
    }
    
    double price = quantity * stock.getStockValue();
    
    public void processOrder() {
        
        if(this.validateOrder() == false) {
            // throw exception
        }
        userAccount.creditBalance(price);
        userAccount.getPortfolio().sellStockUpdate(this);
    }
    
    public boolean validateOrder() {
        boolean isValid = true;
        
        if(userAccount.getPortfolio().getStock(this.getStock()) == null) {
            isValid = false;
        }
        
        if(this.getQuantity() > userAccount.getPortfolio().getStock(this.getStock()).getQuantity()) {
            isValid = false;
        }
        return isValid;
    }
}

class OrderManagement {
    
    private static OrderManagement instance;
    
    public static synchronized OrderManagement getInstance() {
        if (instance == null) {
            instance = new OrderManagement();
        }
        return instance;
    }
    
    Queue<Order> orderQueue = new LinkedList<>();
    List<Order> allOrderHistory = new ArrayList<>();
    
    public void placeOrder(Order order) {
        orderQueue.add(order);
        allOrderHistory.add(order);
        this.processOrder();
    }
    
    public void processOrder() {
        while(!orderQueue.isEmpty()) {
            
            Order order = orderQueue.poll();
            order.processOrder();
        }
    }
}

class StockBrokerFactory {
    
    UserManagement userManagement;
    StockManagement stockManagement;
    OrderManagement orderManagement;
    
    public UserManagement getUserManagement() {
        return userManagement.getInstance();
    }
    
    public StockManagement getStockManagement() {
        return stockManagement.getInstance();
    }
    
    public OrderManagement getOrderManagement() {
        return orderManagement.getInstance();
    }
}

class StockBrokerService {
    
    StockBrokerFactory stockBrokerFactory = new StockBrokerFactory();
    
    public UserAccount createUserAccount(User user, Portfolio portfolio, double balance) {
        UserAccount userAccount = new UserAccount(user, portfolio, balance);
        stockBrokerFactory.getUserManagement().addUser(userAccount);
        
        return userAccount;
    }
    
    public void createOrder(Order order) {
        stockBrokerFactory.getOrderManagement().placeOrder(order);
    }
    
    public void buyStock(Stock stock, UserAccount userAccount, double quantity) {
        Order order = new BuyOrder(userAccount, stock, quantity);
        createOrder(order);
    }
    
    public void sellStock(Stock stock, UserAccount userAccount, double quantity) {
        Order order = new SellOrder(userAccount, stock, quantity);
        createOrder(order);
    }
    
    public void showPortfolio(UserAccount userAccount) {
        Portfolio portfolio = userAccount.getPortfolio();
        portfolio.updatePortfolioBalance();
    
        System.out.println("Portfolio for user: " + userAccount.getUser().userName);
        System.out.println("Balance: " + userAccount.getBalance());
        System.out.println("Total Amount Invested: " + portfolio.totalAmountInvested);
        System.out.println("Current Profit: " + portfolio.currentProfit);
        System.out.println("Current Loss: " + portfolio.currentLoss);
    
        System.out.println("\nStocks Holding:");
        for (UserStock userStock : portfolio.stocksHolding.values()) {
            System.out.println("Stock Name: " + userStock.getStock().getStockName());
            System.out.println("Quantity: " + userStock.getQuantity());
            System.out.println("Price Bought: " + userStock.priceBought);
            System.out.println("Current Price: " + userStock.getStock().getStockValue());
            System.out.println("-----------------------------\n");
        }
    }

    public void createStock(Stock stock) {
        stockBrokerFactory.getStockManagement().addStock(stock);
    }
    
    public List<Stock> getStocks(StockFilter stockFilter) {
        return stockBrokerFactory.getStockManagement().getStocks(stockFilter);
    }
    
    public void updateStockValue(String stockId, double stockValue) {
        stockBrokerFactory.getStockManagement().updateStockValue(stockId, stockValue);
    }
}

class StockBrokerageSystem {
	public static void main(String[] args) {
	    
	    StockBrokerService stockBrokerService = new StockBrokerService();
		
		User user1 = new User("jack", "jack@gmail.com", "675757878");
		Portfolio portfolio1 = new Portfolio();
		
		User user2 = new User("jimmy", "jimmy@gmail.com", "675757878");
		Portfolio portfolio2 = new Portfolio();
		
		
		UserAccount userAccount1 = stockBrokerService.createUserAccount(user1, portfolio1, 20000);
		UserAccount userAccount2 = stockBrokerService.createUserAccount(user2, portfolio2, 50000);
		
		
		Stock stock1 = new Stock("TATA", 120);
		Stock stock2 = new Stock("JIO", 90);
		
		stockBrokerService.createStock(stock1);
		stockBrokerService.createStock(stock2);
		
		// user1 searchs for stock
		List<Stock> searchedStocks = stockBrokerService.getStocks(null);
		
		// user1 buys 5 units of TATA stock
		stockBrokerService.buyStock(searchedStocks.get(0), userAccount1, 5);
		
		// TATA stock value raises
		stockBrokerService.updateStockValue(searchedStocks.get(0).getStockId(), 140);
		
		// user1 displays the portfolio
		stockBrokerService.showPortfolio(userAccount1);
		
		
		
		// user1 buy 10 units of JIO stock
		stockBrokerService.buyStock(searchedStocks.get(1), userAccount1, 10);
		
		// user1 displays the portfolio
		stockBrokerService.showPortfolio(userAccount1);
		
		// TATA stock value raises
		stockBrokerService.updateStockValue(searchedStocks.get(1).getStockId(), 5);
		
		// user1 displays the portfolio
		stockBrokerService.showPortfolio(userAccount1);
		
	}
}
