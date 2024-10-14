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

enum RideStatus {
    BOOKED,
    PENDING,
    FAILED,
    CANCELLED
}

enum DriverStatus {
    AVAILABLE,
    NOT_AVAILABLE,
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

class DriverService {
    List<Driver> driverList = new ArrayList<>();
    
    public DriverService() {
        
    }
    
    public void addDriver(Driver driver) {
        driverList.add(driver);
    }
    
    public void removeDriver(String driverId) {
        Driver driver = getDriver(driverId);
        driverList.remove(driver);
    }
    
    public List<Driver> getNearestDriver(Location location) {
        // logic to return all the available drivers that are closest to the given location
        return driverList;
    }
}

class Rider {
    String name;
    String riderId;
    String contacNumber;
    String emailId;
    
    public Rider(String name, String riderId, String contacNumber, String emailId) {
        this.riderId = "1234";
        this.name = this.name;
        this.contacNumber = contacNumber;
        this.emailId = emailId;
    }
    
    public void notifyRider(Ride ride) {
        // ride confirmation request sent to rider
    }
}

class Location {
    String country;
    String state;
    String city;
    String area;
    String pincode;
    
    public Location(String country, String state, String city, String area, String pincode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.area = area;
        this.pincode = pincode;
    }
}

class Ride {
    String rideId;
    Location source;
    Location destination;
    Rider rider;
    Driver driver;
    double fare;
    RideStatus rideStatus;
    
    public Ride(String source, String destination, Rider rider) {
        this.rideId = "1234";
        this.source = source;
        this.destination = destination;
        this.rideStatus = RideStatus.PENDING;
        this.rider = rider;
    }
    
    public void setRideer(Rider rider) {
        this.rider = rider;
    }
    
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    
    public void setFare(double fare) {
        this.fare = fare;
    }
    
    public void setRideStatus(String rideStatus) {
        this.rideStatus = ridestatus;
    }
}

class RideService {
    List<Ride> bookedRides = new ArrayList<>();
    DriverService driverService;
    
    double oneKmPrice = 5.00;
    
    public RideService(DriverService driverService) {
        this.driverService = driverService;
    }
    
    public double getRideFare(Ride ride) {
        // logic to calculate fare
        return 1.00;
    }
    
    public List<Driver> getDriverForRide(Ride ride) {
        return driverService.getNearestDriver(ride.getSourceLocation);
    }
    
    public Ride bookRide(Ride ride, Driver driver) {
        ride.setDriver(driver);
        ride.setFare(getRideFare(ride));
        ride.setRideStatus(rideStatus.BOOKED);
        driver.updateDriverStatus(DriverStatus.NOT_AVAILABLE);
        driver.notifyDriver();
        bookedRides.add(ride);
        return ride;
    }
    
    public Ride cancelRide(String rideId) {
        Ride ride = getRide(rideId);
        if(ride != NULL) {
            ride.setRideStatus(RideStatus.CANCELLED);
            driver.updateDriverStatus(DriverStatus.AVAILABLE);
            driver.notifyDriver();
        }
        
        return ride;
    }
}

public class UberSystemDesign {
	public static void main(String[] args) {
		
	}
}
