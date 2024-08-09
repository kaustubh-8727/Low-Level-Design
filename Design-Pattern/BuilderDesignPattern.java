class Vehicle {
    
    String type;
    String vehicleCompanyName;
    String model;
    String color;
    double FuelEfficiency;
    String price;
    String VIN;
    
    public Vehicle(VehicleBuilder vehicleBuilder) {
        type = vehicleBuilder.type;
        vehicleCompanyName = vehicleBuilder.vehicleCompanyName;
        model = vehicleBuilder.model;
        color = vehicleBuilder.color;
        FuelEfficiency = vehicleBuilder.FuelEfficiency;
        price = vehicleBuilder.price;
        VIN = vehicleBuilder.VIN;
    }
    
    public void getVehicle() {
        System.out.println(
            "type : " + type + "\n" +
            "company : "+ vehicleCompanyName + "\n" +
            "model : " + model + "\n" +
            "color : " + color + "\n" +
            "FuelEfficiency : " + FuelEfficiency + "\n" +
            "price : " + price + "\n" +
            "VIN : " + VIN + "\n"
        );
    }
}

abstract class VehicleBuilder {
    
    String type;
    String vehicleCompanyName;
    String model;
    String color;
    double FuelEfficiency;
    String price;
    String VIN;
    
    public VehicleBuilder setMake(String make) {
        this.vehicleCompanyName = make;
        return this;
    }
    
    public VehicleBuilder setModel(String model) {
        this.model = model;
        return this;
    }
    
    public VehicleBuilder setFuelEfficiency(double FuelEfficiency) {
        this.FuelEfficiency = FuelEfficiency;
        return this;
    }
    
    public VehicleBuilder setPrice(String price) {
        this.price = price;
        return this;
    }
    
    public Vehicle build() {
        return new Vehicle(this);
    }
    
    public abstract VehicleBuilder setVIN(String VIN);
    
    public abstract VehicleBuilder setType(String type);
}

class Car extends VehicleBuilder {
    
    public VehicleBuilder setType(String type) {
        this.type = type;
        return this;
    }
    
    public VehicleBuilder setVIN(String VIN) {
        this.VIN = VIN;
        return this;
    }
}

class Truck extends VehicleBuilder {
    
    public VehicleBuilder setType(String type) {
        this.type = type;
        return this;
    }
    
    public VehicleBuilder setVIN(String VIN) {
        this.VIN = VIN;
        return this;
    }
}

public class BuilderDesignPattern {
	public static void main(String[] args) {
		
		VehicleBuilder carBuilder = new Car();
		Vehicle car;
		car = carBuilder.setType("car").setMake("BMW").setModel("XC-15").setFuelEfficiency(245.89).setPrice("$5000").setVIN("LX 5C 67D").build();
		car.getVehicle();
		
		VehicleBuilder truckBuilder = new Truck();
		Vehicle truck;
		truck = truckBuilder.setType("truck").setMake("TATA").setModel("TC-99").setFuelEfficiency(1500.00).setPrice("$88000").setVIN("AZ 5D Z4").build();
		truck.getVehicle();
	}
}

