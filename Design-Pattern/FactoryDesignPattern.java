interface Vehicle {
    public void createVehicle();
}

class Car implements Vehicle {
    public void createVehicle() {
        System.out.println("car is created....");
    }
}

class Bike implements Vehicle {
    public void createVehicle() {
        System.out.println("bike is created....");
    }
}

class Truck implements Vehicle {
    public void createVehicle() {
        System.out.println("truck is created....");
    }
}

class VehicleFactory {
    public Vehicle getVehicle(String vehicleName) {
        switch(vehicleName) {
            case "Car" :
                return new Car();
            case "Bike" :
                return new Bike();
            case "Truck" :
                return new Truck();
            default :
                return null;
        }
    }
}

class FactoryDesignPattern {
    public static void main(String args[]) {

        VehicleFactory vehicleFactory = new VehicleFactory();

        Vehicle vehicle = vehicleFactory.getVehicle("Car");

        vehicle.createVehicle();
    }
}
