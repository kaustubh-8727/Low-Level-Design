abstract class Product {
    
    double price;
    public abstract void accept(IVisitor visitor);
}

class Electronics extends Product {
    
    public Electronics(double price) {
        this.price = price;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}

class Clothing extends Product {
    
    public Clothing(double price) {
        this.price = price;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}

class Books extends Product {
    
    public Books(double price) {
        this.price = price;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}

interface IVisitor {
    
    public void visit(Electronics electronics);
    public void visit(Clothing clothing);
    public void visit(Books books);
}

class TaxVisitor implements IVisitor {
    
    double tax;
    
    public TaxVisitor(double tax) {
        this.tax = tax;
    }
    
    public void visit(Electronics electronics) {
        
        double totalCost = electronics.price + (electronics.price * tax) / 100;
        System.out.println("your total price after tax is : " + totalCost);
        
    }
    
    public void visit(Clothing clothing) {
        
        double totalCost = clothing.price + (clothing.price * tax) / 100;
        System.out.println("your total price after tax is : " + totalCost);
        
    }
    
    public void visit(Books books) {
        
        double totalCost = books.price + (books.price * tax) / 100;
        System.out.println("your total price after tax is : " + totalCost);
        
    }
}

class ShippingCostVisitor implements IVisitor {
    
    double shippingCost;
    
    public ShippingCostVisitor(double shippingCost) {
        this.shippingCost = shippingCost;
    }
    
    public void visit(Electronics electronics) {
        
        double totalCost = electronics.price + shippingCost;
        System.out.println("your total price after shipping charges is : " + totalCost);
        
    }
    
    public void visit(Clothing clothing) {
        
        double totalCost = clothing.price + shippingCost;
        System.out.println("your total price after shipping charges is : " + totalCost);
        
    }
    
    public void visit(Books books) {
        
        double totalCost = books.price + shippingCost;
        System.out.println("your total price after shipping charges is : " + totalCost);
        
    }
}

class PromotionalDiscountVisitor implements IVisitor {
    
    double discount;
    
    public PromotionalDiscountVisitor(double discount) {
        this.discount = discount;
    }
    
    public void visit(Electronics electronics) {
        
        double totalCost = electronics.price - (electronics.price * discount) / 100;
        System.out.println("your total price after discount is : " + totalCost);
        
    }
    
    public void visit(Clothing clothing) {
        
        double totalCost = clothing.price - (clothing.price * discount) / 100;
        System.out.println("your total price after discount is : " + totalCost);
        
    }
    
    public void visit(Books books) {
        
        double totalCost = books.price - (books.price * discount) / 100;
        System.out.println("your total price after discount is : " + totalCost);
        
    }
}

public class VisitorDesignPattern {
	public static void main(String[] args) {
		
		Product electronics = new Electronics(200.00);
		Product clothing = new Clothing(1005.00);
		Product books = new Books(80.00);
		
		IVisitor taxVisitor = new TaxVisitor(5.00);
		IVisitor shippingCostVisitor = new ShippingCostVisitor(100);
		IVisitor promotionalDiscountVisitor = new PromotionalDiscountVisitor(8);
		
		electronics.accept(taxVisitor);
		clothing.accept(taxVisitor);
		books.accept(taxVisitor);
		
		System.out.println();
		
		electronics.accept(shippingCostVisitor);
		clothing.accept(shippingCostVisitor);
		books.accept(shippingCostVisitor);
		
		System.out.println();
		
		electronics.accept(promotionalDiscountVisitor);
		clothing.accept(promotionalDiscountVisitor);
		books.accept(promotionalDiscountVisitor);
		
	}
}
