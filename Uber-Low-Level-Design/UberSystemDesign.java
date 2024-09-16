/*

1. Rider: Represents the person booking the ride. Attributes could include rider ID, name, contact details, and payment preferences.
2. Driver: Represents the person offering the ride. Attributes could include driver ID, name, rating, vehicle details, and availability.
3. Location: Used to represent the pickup and drop-off points. You could refine this with attributes like latitude, longitude, and address.
4. Vehicle: Represents the vehicle being used for the ride. Attributes include vehicle type, registration number, and capacity.
5. Ride: Central entity representing a ride instance, with details like rider, driver, pickup location, drop-off location, ride status, fare, and timestamps (start/end).
6. Ride Service: Manages ride bookings, matching riders with drivers, scheduling, and ride status updates.
7. Payment: Handles payments for rides, including attributes like amount, payment method, and status.
8. Driver Service: Manages driver-related operations like availability, assignment, and notifications.

code is in progress

*/

enum CarType {
    FOUR_SEATER,
    SIX_SEATER
}

interface Vehicle {
    String vehicleId;
    String vehicleName;
    double vehicleFarePrice;
}

class Bike implements Vehicle {
    
    public Bike(String vehicleId, String vehicleName, double vehicleFarePrice) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.vehicleFarePrice = vehicleFarePrice;
    }
}

class Auto implements Vehicle {
    
    public Auto(String vehicleId, String vehicleName, double vehicleFarePrice) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.vehicleFarePrice = vehicleFarePrice;
    }
}

class Car implements Vehicle {
    
    CarType carType;
    
    public Car(String vehicleId, String vehicleName, double vehicleFarePrice, CarType carType) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.vehicleFarePrice = vehicleFarePrice;
        this.carType = carType;
    }
}

class Driver {
    
    String driverId;
    String driverName;
    String contactNumber;
    Vehicle driverVehicle;
    DriverStatus driverStatus;
    Location currentLocation;
    
    public Driver(String driverName, String contactNumber, Vehicle driverVehicle, Location currentLocation) {
        this.driverId = "1234";
        this.driverName = driverName;
        this.contactNumber = contactNumber;
        this.driverVehicle = driverVehicle;
        this.currentLocation = currentLocation;
        this.driverStatus = DriverStatus.AVAILABLE;
    }
    
    public String getDriverId() {
        return driverId;
    }
    
    public void updateDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }
    
    public void notifyDriver(Ride ride) {
        // ride request notification sent to the driver
    }
}

public class UberSystemDesign {
	public static void main(String[] args) {
		
	}
}
