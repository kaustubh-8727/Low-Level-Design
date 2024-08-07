import java.util.List;
import java.util.ArrayList;

interface NotificationObservable {
    
    public void addObserver(NotificationObserver obs);
    
    public void removeObserver(NotificationObserver obs);
    
    public void notifyUser();
    
    public void addProductStock(int stock);
    
    public String getproductName();
    
}

class NotificationObservable_ElectronicProduct implements NotificationObservable {
    
    List<NotificationObserver> observerList = new ArrayList<NotificationObserver>();
    
    int stockCount = 0;
    public String productName;
    
    NotificationObservable_ElectronicProduct(int stockCount, String productName) {
        this.stockCount = stockCount;
        this.productName = productName;
    }
    
    public void addObserver(NotificationObserver obs) {
        observerList.add(obs);
    }
    
    public void removeObserver(NotificationObserver obs) {
        observerList.remove(obs);
    }
    
    public void notifyUser() {
        for(NotificationObserver obs : observerList) {
            obs.update(this);
        }
    }
    
    public void addProductStock(int stock) {
        if(stockCount == 0) {
            notifyUser();
        }
        stockCount += stock;
    }
    
    public String getproductName() {
        return productName;
    }
}

interface NotificationObserver {
    
    public void update(NotificationObservable notificationObservable);
    
    public void notifyMe(NotificationObservable notificationObservable);
}

class NotificationObserver_user implements NotificationObserver {
    
    String userEmail;

    NotificationObserver_user(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public void notifyMe(NotificationObservable notificationObservable) {
        notificationObservable.addObserver(this);
    }
    
    public void update(NotificationObservable notificationObservable) {
        System.out.println("email sent to " + userEmail + " : " + notificationObservable.getproductName() + " stock is available go ahead and shop before it ends");
    }
}

public class ObserverDesignPattern {
	public static void main(String[] args) {
		
		int intialStockCount = 0;
		
		NotificationObservable IphoneObservable = new NotificationObservable_ElectronicProduct(intialStockCount, "IPhone");
		
		NotificationObserver user1 = new NotificationObserver_user("abc1@gmail.com");
		NotificationObserver user2 = new NotificationObserver_user("abc2@gmail.com");
		NotificationObserver user3 = new NotificationObserver_user("abc3@gmail.com");
		NotificationObserver user4 = new NotificationObserver_user("abc4@gmail.com");
		
		user1.notifyMe(IphoneObservable);
		user2.notifyMe(IphoneObservable);
		user3.notifyMe(IphoneObservable);
		user4.notifyMe(IphoneObservable);
		
		IphoneObservable.addProductStock(20);
		
	}
}
