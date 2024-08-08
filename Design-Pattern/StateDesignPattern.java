import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Product {
    String name;
    double price;
    
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

enum Coin {
    PENNY(1),
    NICKEL(5),
    DIME(10),
    QUARTER(25);

    public int value;

    Coin(int value) {
        this.value = value;
    }
}

class Inventory {
    
    int code = 101;
    Map<Integer, Product> productStore = new HashMap<>();
    
    public Inventory() {
        Product product1 = new Product("pepsi", 30);
        Product product2 = new Product("coke", 40);
        Product product3 = new Product("redBull", 105);
        Product product4 = new Product("coke Diet", 90);
    
        productStore.put(code++, product1);
        productStore.put(code++, product2);
        productStore.put(code++, product3);
        productStore.put(code++, product4);
    }
    
    public void addNewProduct(Product product) {
        productStore.put(code, product);
        code += 1;
    }
    
    public void updateProduct(int code, Product product) {
        productStore.put(code, product);
    }
    
    public Product getProduct(int productCode) {
        return productStore.get(productCode);
    }
}

interface State {
    
    public void clickOnSelectProductButton(VendingMachine vendingMachine) throws Exception;
    
    public void clickOnInsertCoinButton(VendingMachine vendingMachine) throws Exception;
    
    public void selectProduct(VendingMachine vendingMachine, int productCode) throws Exception;
    
    public void insertCoin(VendingMachine vendingMachine, Coin coin) throws Exception;
    
    public List<Coin> getRefund(VendingMachine vendingMachine) throws Exception;
    
    public double getChange(VendingMachine vendingMachine) throws Exception;
    
    public void despenseProduct(VendingMachine vendingMachine) throws Exception;
    
    public void clickOnDespenseProduct(VendingMachine vendingMachine) throws Exception;
    
}

class IdleState implements State {
    
    public IdleState() {
        System.out.println("> vending machine is in idle State");
    }
    
    public void clickOnSelectProductButton(VendingMachine vendingMachine) {
        vendingMachine.setState(new SelectionState());
    }
    
    public void clickOnInsertCoinButton(VendingMachine vendingMachine) throws Exception {
        throw new Exception("first you need select the product");
    }
    
    public void selectProduct(VendingMachine vendingMachine, int productCode) throws Exception {
        throw new Exception("you cannot choose product in idle State");
    }
    
    public void insertCoin(VendingMachine vendingMachine, Coin coin) throws Exception {
        throw new Exception("you cannot insert coin in idle State");
    }
    
    public void despenseProduct(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot despense product in idle State");
    }
    
    public double getChange(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot get change in idle State");
    }
    
    public List<Coin> getRefund(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot get refund in idle State");
    }
    
    public void clickOnDespenseProduct(VendingMachine vendingMachine) throws Exception {
        throw new Exception("invalid operation in idle State");
    }
}

class SelectionState implements State {
    
    public SelectionState() {
        System.out.println("> vending machine is in selection State");
    }
    
    public void selectProduct(VendingMachine vendingMachine, int productCode) {
        Product product = vendingMachine.getProduct(productCode);
        vendingMachine.selectedProduct = product;
        System.out.println(product.name + " is selected");
    }
    
    public void clickOnInsertCoinButton(VendingMachine vendingMachine) {
        vendingMachine.setState(new MoneyState());
    }
    
    public void clickOnSelectProductButton(VendingMachine vendingMachine) throws Exception {
        throw new Exception("invalid operation in selection State");
    }
    
    public void insertCoin(VendingMachine vendingMachine, Coin coin) throws Exception {
        throw new Exception("you cannot insert coin in selection State");
    }
    
    public void despenseProduct(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot despense product in selection State");
    }
    
    public double getChange(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot get change in selection State");
    }
    
    public List<Coin> getRefund(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot get refund in selection State");
    }
    
    public void clickOnDespenseProduct(VendingMachine vendingMachine) throws Exception {
        throw new Exception("invalid operation in selection State");
    }
}

class MoneyState implements State {
    
    public MoneyState() {
        System.out.println("> vending machine is in money State");
    }
    
    public void insertCoin(VendingMachine vendingMachine, Coin coin) {
        vendingMachine.getCoinList().add(coin);
    }
    
    public List<Coin> getRefund(VendingMachine vendingMachine) {
        vendingMachine.setState(new IdleState());
        return vendingMachine.getCoinList();
    }
    
    public void clickOnDespenseProduct(VendingMachine vendingMachine) {
        vendingMachine.setState(new DespenseState());
    }
    
    public void clickOnSelectProductButton(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot select product in money state");
    }
    
    public void clickOnInsertCoinButton(VendingMachine vendingMachine) throws Exception {
        throw new Exception("invalid operation in money state");
    }
    
    public void selectProduct(VendingMachine vendingMachine, int productCode) throws Exception {
        throw new Exception("you cannot choose product in idle State");
    }
    
    public void despenseProduct(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot despense product in idle State");
    }
    
    public double getChange(VendingMachine vendingMachine) throws Exception {
        throw new Exception("you cannot get change in idle State");
    }
}

class DespenseState implements State {
    
    public DespenseState() {
        System.out.println("> vending machine is in despense State");
    }
    
    public void clickOnSelectProductButton(VendingMachine vendingMachine) throws Exception {
        throw new Exception("invalid operation in despense state");
    }
    
    public void clickOnInsertCoinButton(VendingMachine vendingMachine) throws Exception {
        throw new Exception("first you need select the product");
    }
    
    public void selectProduct(VendingMachine vendingMachine, int productCode) throws Exception {
        throw new Exception("you cannot choose product in idle State");
    }
    
    public void insertCoin(VendingMachine vendingMachine, Coin coin) throws Exception {
        throw new Exception("you cannot insert coin in idle State");
    }
    
    public boolean validAmount(VendingMachine vendingMachine) {
        if(vendingMachine.getTotalAmount() >= vendingMachine.selectedProduct.price) {
            return true;
        }
        
        return false;
    }
    
    public void despenseProduct(VendingMachine vendingMachine) throws Exception {
        
        boolean isValidAmount = validAmount(vendingMachine);
        
        if(isValidAmount) {
            System.out.println(vendingMachine.selectedProduct.name +  " has been despensed");
            getChange(vendingMachine);
            vendingMachine.setState(new IdleState());
        } 
        else {
            getRefund(vendingMachine);
            throw new Exception("insufficient amount to buy the product");
        }
    }
    
    public double getChange(VendingMachine vendingMachine) {
        double returnAmount = vendingMachine.getTotalAmount() - vendingMachine.selectedProduct.price;
        System.out.println(returnAmount + " amount has been returned");
        return returnAmount;
    }
    
    public List<Coin> getRefund(VendingMachine vendingMachine) {
        
        System.out.println("the amount submitted by you has been refunded back");
        
        vendingMachine.clearCoinList();
        vendingMachine.setState(new IdleState());
        return vendingMachine.getCoinList();
    }
    
    public void clickOnDespenseProduct(VendingMachine vendingMachine) throws Exception {
        throw new Exception("invalid operation in idle State");
    }
}

class VendingMachine {
    
    State currentState;
    Product selectedProduct;
    List<Coin> coinsList = new ArrayList<>();
    Inventory inventory;
    
    public VendingMachine() {
        currentState = new IdleState();
        inventory = new Inventory();
    }
    
    public void setState(State nextState) {
        currentState = nextState;
    }
    
    public State getState() {
        return currentState;
    }
    
    public Product getProduct(int productCode) {
        return inventory.getProduct(productCode);
    }
    
    public List<Coin> getCoinList() {
        return coinsList;
    }
    
    public void clearCoinList() {
        coinsList.clear();
    }
    
    public double getTotalAmount() {
        double paidByUser = 0.0;
        
        for(Coin coin : coinsList) {
            paidByUser = paidByUser + coin.value;
        }
        
        return paidByUser;
    }
}

public class StateDesignPattern {
	public static void main(String[] args) {
	    
	    try {
    		System.out.println("Vending Machine starts");
    		VendingMachine vendingMachine = new VendingMachine();
    		vendingMachine.getState().clickOnSelectProductButton(vendingMachine);
    		vendingMachine.getState().selectProduct(vendingMachine, 102);
    		vendingMachine.getState().clickOnInsertCoinButton(vendingMachine);
    		vendingMachine.getState().insertCoin(vendingMachine, Coin.QUARTER);
    		vendingMachine.getState().insertCoin(vendingMachine, Coin.QUARTER);
    		vendingMachine.getState().clickOnDespenseProduct(vendingMachine);
    		vendingMachine.getState().despenseProduct(vendingMachine);
    		
	    }
	    catch(Exception e) {
	        System.out.println(e);
	    }
		
	}
}
