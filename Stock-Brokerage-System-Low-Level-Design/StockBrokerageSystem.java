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
        orderHistory.put(order.getOrderId(), Order);
        
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
    
    public Stock getStock(Stock stock) {
        string stockId = stock.getStockId();
        
        if(!stocksHolding.containsKey(stockId)) {
            return null;
        }
        
        return stocksHolding.get(stockId);
    } 
    
    public void updatePortfolioBalance() {
        // this class will update currentProfit, currentLoss, totalAmountInvested
    }
}

class UserManagement {
    
    List<UserAccount> allUsers;
    
    public void addUser(UserAccount userAccount) {
        allUsers.add(userAccount);
    }
    
    public void removeUser(UserAccount userAccount) {
        allUsers.remove(userAccount);
    }
    
    public UserAccount getUserAccount(String userId) {
        UserAccount userAccount = getUserById(userId);
        return userAccount;
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
    List<Stock> stocksList = new ArrayList<>();
    
    public void addStock(Stock stock) {
        stocksList.add(stock);
    }
    
    public void removeStokc(Stock stock) {
        stocksList.remove(stock);
    }
    
    public List<Stock> getStocks(StockFilter stockFilter) {
        if(stockFilter == null) {
            return stockList;
        }
        return stockList;
    }
}

class StockFilter {
    
}

abstract class Order {
    String orderId;
    UserAccount userAccount;
    Stock stock;
    double quantity;
    
    public Order(UserAccount userAccount, Stock stock) {
        this.orderId = "1234";
        this.userAccount = userAccount;
        this.stock = stock;
        this.quantity = quantity;
    }
    
    public Stock getStock() {
        return stock;
    }
    
    public void processOrder();
}

class BuyOrder extends Order {
    
    double price = quantity * stock.getStockValue();
    
    public void prodessOrder() {
        
        if(this.validateOrder() == false) {
            // throw exception
        }
        
        userAccount.debitBalance(totalPrice);
        userAccount.getPortfolio().buyStockUpdate(Order)
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
    double price = quantity * stock.getStockValue();
    
    public void prodessOrder() {
        
        if(this.validateOrder() == false) {
            // throw exception
        }
        userAccount.creditBalance(totalPrice);
        userAccount.getPortfolio().sellStockUpdate(Order);
    }
    
    public boolean validateOrder() {
        boolean isValid = true;
        
        if(userAccount.getPortfolio().getStock(this.order.getStock()) == null) {
            isValid = false;
        }
        
        if(order.getQuantity() > userAccount.getPortfolio().getStock(order.getStock().getStockId()).getQuantity()) {
            isValid = false;
        }
        return isValid;
    }
}

public class StockBrokerageSystem {
	public static void main(String[] args) {
		System.out.println("Hello World");
	}
}
