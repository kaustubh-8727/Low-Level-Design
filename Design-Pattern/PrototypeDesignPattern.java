interface Person {
    
    public Person clone();
    
    public void getDetails();
}

class Man implements Person {
    
    String name;
    int height;
    int weight;
    
    public Man(String name, int height, int weight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
    }
    
    public Person clone() {
        return new Man(name, height, weight);
    }
    
    public void getDetails() {
        System.out.println("my name is " + name + " and my weight is " + weight);
    }
}

class PrototypeDesignPattern {
    public static void main(String args[]) {
        
        Person man1 = new Man("mike", 6, 60);
        
        man1.getDetails();
        
        Person man2 = man1.clone();
        
        man2.getDetails();
    }
}
