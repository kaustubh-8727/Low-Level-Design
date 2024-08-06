abstract class Beverage {
    
    protected String name;
    protected int price;
    
    public Beverage(){
        
    }
    public  Beverage(String name){
        this.name = name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    protected void setPrice(int price){
        this.price = price;
    }
    protected int getPrice(){
        return price;
    }
    protected abstract void decorateBeverage();
    
}

class Tea extends Beverage{
    
    public Tea(String name){
        super(name);
        setPrice(10);
    }
    
    public void decorateBeverage(){
        System.out.println("Cost of:"+ name +":"+ price);
    }
}

class Coffee extends Beverage{
    
    public Coffee(String name){
        super(name);
        setPrice(15);
    }
    
    public void decorateBeverage(){
        System.out.println("Cost of:"+ name +":"+ price);
    }   
}

abstract class BeverageDecorator extends Beverage {
    
    protected Beverage beverage;
    
    public BeverageDecorator(Beverage beverage){    
        this.beverage = beverage;   
        setName(beverage.getName()+"+"+getDecoratedName());
        setPrice(beverage.getPrice()+getIncrementPrice());
    }
    
    public void decorateBeverage(){
        beverage.decorateBeverage();
        System.out.println("Cost of:"+getName()+":"+getPrice());
    }   
    
    public abstract int getIncrementPrice();
    
    public abstract String getDecoratedName();
}

class SugarDecorator extends BeverageDecorator{
    
    public SugarDecorator(Beverage beverage){
        super(beverage);
    }
    
    public void decorateBeverage(){
        super.decorateBeverage();
        decorateSugar();        
    }
    
    public void decorateSugar(){
        System.out.println("Added Sugar to:"+beverage.getName());
    }
    
    public int getIncrementPrice(){
        return 5;
    }
    
    public String getDecoratedName(){
        return "Sugar";
    }
}

class LemonDecorator extends BeverageDecorator{
    
    public LemonDecorator(Beverage beverage){
        super(beverage);
    }
    
    public void decorateBeverage(){
        super.decorateBeverage();
        decorateLemon();    
    }
    
    public void decorateLemon(){
        System.out.println("Added Lemon to:"+beverage.getName());       
    }
    
    public int getIncrementPrice(){
        return 3;
    }
    
    public String getDecoratedName(){
        return "Lemon";
    }
}

public class DecoratorDesignPattern {  
    public static void main(String args[]){
        
        Beverage beverage = new SugarDecorator(new LemonDecorator(new Tea("Assam Tea")));
        beverage.decorateBeverage();
        
        System.out.print("\n");
        
        beverage = new SugarDecorator(new LemonDecorator(new Coffee("Cappuccino")));
        beverage.decorateBeverage();
    }
}
