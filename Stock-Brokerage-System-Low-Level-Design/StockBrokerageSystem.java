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

class Portfolio {
    
    String portfolioId;
    Map<String, Order> stocksHolding = new HashMap<>();
    Map<String, Order> orderHistory = new HashMap<>();
    double currentProfit;
    double totalAmountInvested;
    double currentLoss;
    
    public Portfolio() {
        this.portfolioId = "1234";
    }
    
    public void buyStockUpdate(Order order) {
        stocksHolding.put(order.getOrderId(), Order);
        orderHistory.put(order.getOrderId(), Order);
        
        this.updatePortfolioBalance();
    }
    
    public void sellStockUpdate(Order order) {
        if(stocksHolding.containsKey(order.getOrderId())) {
            stocksHolding.remove(order.getOrderId());
        }
        if(orderHistory.containsKey(order.getOrderId())) {
            orderHistory.remove(order.getOrderId());
        }
        
        this.updatePortfolioBalance();
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

public class StockBrokerageSystem {
	public static void main(String[] args) {
		System.out.println("Hello World");
	}
}
