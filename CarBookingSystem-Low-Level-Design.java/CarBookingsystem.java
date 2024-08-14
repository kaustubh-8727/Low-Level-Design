import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

enum VehicleType {
    CAR,
    BIKE,
    Travller
}

enum vehicleStatus {
    BOOKED,
    AVAILABLE
}

enum ReservationStatus {
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

class Location {
    String country;
    String state;
    String city;
    String pincode;
    
    public Location(String country, String state, String city, String pincode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
    }
}

class User {
    String name;
    int age;
    String licenseId;
    String contactNo;
    
    public User(String name, int age, String licenseId, String contactNo) {
        this.name = name;
        this.age = age;
        this.licenseId = licenseId;
        this.contactNo = contactNo;
    }
}



class Vehicle {
    String vehicleCompanyName;
    int vehicleId;
    VehicleType vehicleType;
    int vehicleAverage;
    String modelName;
    int numberOfSeats;
    double hourlyRentalCost;
    vehicleStatus status;
    
    public void setVehicleCompanyName(String vehicleCompanyName) {
        this.vehicleCompanyName = vehicleCompanyName;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setVehicleAverage(int vehicleAverage) {
        this.vehicleAverage = vehicleAverage;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setHourlyRentalCost(double hourlyRentalCost) {
        this.hourlyRentalCost = hourlyRentalCost;
    }

    public void setVehicleStatus(vehicleStatus status) {
        this.status = status;
    }

}

class Car extends Vehicle {
    
    // car car can have some extra properties
}

class Bike extends Vehicle {
    
    // boke can have some extra properties
}

class VehicleInventoryManagement {
    
    List<Vehicle> vehicles;
    
    public VehicleInventoryManagement(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
    
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }
    
    public List<Vehicle> getVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        for(Vehicle vehicle : vehicles) {
            if(vehicle.status == vehicleStatus.AVAILABLE) {
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }
}

class Reservation {
    int reservationId;
    User user;
    Vehicle vehicle;
    ReservationStatus reservationStatus;
    
    public Reservation createReservation(Vehicle vehicle, User user) {
        reservationId = (int)(Math.random() * 9000) + 1000;
        this.vehicle = vehicle;
        this.user = user;
        this.reservationStatus = ReservationStatus.IN_PROGRESS;
        return this;
    }
    
    public int getId() {
        return reservationId;
    }
    
    public void completeReservation() {
        reservationStatus = ReservationStatus.COMPLETED;
        vehicle.setVehicleStatus(vehicleStatus.BOOKED);
    }
    
    public void cancelReservation() {
        reservationStatus = ReservationStatus.CANCELLED;
    }
}

class Store {
    VehicleInventoryManagement vehicleInventoryManagement;
    List<Reservation> reservations = new ArrayList<>();
    Location storeAddress;
    
    public void setLocation(Location location) {
        this.storeAddress = location;
    }
    
    public void setVehicleInventory(List<Vehicle> vehicles) {
        this.vehicleInventoryManagement = new VehicleInventoryManagement(vehicles);
    }
    
    public List<Vehicle> getVehicles() {
        return vehicleInventoryManagement.getVehicles();
    }
    
    public void addVehicle(Vehicle vehicle) {
        vehicleInventoryManagement.addVehicle(vehicle);
    }
    
    public Reservation createReservation(Vehicle vehicle, User user) {
        Reservation reservation = new Reservation();
        reservation.createReservation(vehicle, user);
        reservations.add(reservation);
        return reservation;
    }
    
    public boolean completeReservation(int reservationId) {
        for(Reservation reservation : reservations) {
            if(reservation.getId() == reservationId) {
                reservation.completeReservation();
                return true;
            }
        }
        return false;
    }
    
    public boolean cancelReservation(int reservationId) {
        for(Reservation reservation : reservations) {
            if(reservation.getId() == reservationId) {
                reservation.cancelReservation();
                return true;
            }
        }
        return false;
    }
    
}

class CarBookingManagement {
    List<User> users;
    Map<Location, List<Store>> vehiclesMap;
    
    public CarBookingManagement() {
        users = new ArrayList<>();
        vehiclesMap = new HashMap<>();
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public void  setStoreWiseVehicles(Map<Location, List<Store>> vehiclesMap) {
        this.vehiclesMap = vehiclesMap;
    }
    
    public void addStore(Location location, Store store) {
        List<Store> stores = new ArrayList<>();
        if(vehiclesMap.containsKey(location)) {
            stores = vehiclesMap.get(location);
        }
        stores.add(store);
        vehiclesMap.put(location, stores);
    }
    
    public List<Store> getStoresInLocation(Location location) {
        List<Store> stores = new ArrayList<>();
        if(vehiclesMap.containsKey(location)) {
            stores = vehiclesMap.get(location);
        }
        return stores;
    }
}

class Bill {
    Reservation reservation;
    int billId;
    boolean billGenerated = false;
    Date billDate;
    
    public Bill(Reservation reservation) {
        this.reservation = reservation;
        billId = (int)(Math.random() * 9000) + 1000;
        billDate = new Date();
        billGenerated = true;
    }
    
    public void showInvoice() {
        if(!billGenerated) {
            return;
        }
        System.out.println(
            "booking done - \n" + 
            "invoice generated below \n" +
            "vehicle name : " + reservation.vehicle.vehicleCompanyName + "\n" + 
            "model name : " + reservation.vehicle.modelName + "\n" +
            "vehicle number " + reservation.vehicle.vehicleId + "\n" +
            "booking date :  " + billDate + "\n" + 
            "bill number : " + billId + "\n" +
            "reservation number : " + reservation.reservationId + "\n"
        );
    }
}

class Payment {
    
    public void payBill(Bill bill) {
        if(bill.billGenerated) {
            bill.showInvoice();
        }
    }
}

public class CarBookingsystem {
	public static void main(String[] args) {
	    
	    // intialize car booking object
	    CarBookingManagement carBookingManagement = new CarBookingManagement();
	    
	    // create vehicles
		List<Vehicle> vehicleList1 = createVehicles();
		List<Vehicle> vehicleList2 = createVehicles();
		
		// create location
		Location location1 = new Location("USA", "Arizona", "Phoenix", "85001");
		Location location2 = new Location("USA", "New York", "Albany", "12207");
		
		// create stores
		Store store1 = new Store();
		store1.setLocation(location1);
		store1.setVehicleInventory(vehicleList1);

		Store store2 = new Store();
		store2.setLocation(location2);
		store2.setVehicleInventory(vehicleList2);
		
		// create users
		User user1 = new User("alex", 26, "AZ34-56", "999888999");
		User user2 = new User("jimmy", 28, "JP68-34", "77766222");
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
        // set users and stores for booking
		carBookingManagement.setUsers(users);
		carBookingManagement.addStore(location1, store1);
		carBookingManagement.addStore(location2, store2);
		
		// start car booking
		
		// 1. user come
		User user = users.get(0);
		
		// 2. user selects the location to get all the stores in that location
		Location location = location1;
		
		// 3. user fetch all the stores in that location
		List<Store> stores = carBookingManagement.getStoresInLocation(location1);
		
		// 4. user fetch all the available vehicles in first store
		Store store = stores.get(0);
		List<Vehicle> vehicles = store.getVehicles();
		
		// 5. user selects second vehicle that is a car for booking
		Vehicle vehicle = vehicles.get(1);
		
		// 6. user reserve the car
		Reservation reservation = store.createReservation(vehicle, user);
		
		// 7. user book the car and get the bill
        Bill bill = new Bill(reservation);

        // 8. user do the payment
        Payment payment = new Payment();
        payment.payBill(bill);
	}
	
	private static List<Vehicle> createVehicles() {
	    List<Vehicle> vehicles = new ArrayList<>();
	    vehicles.add(getNewVehicle());
	    vehicles.add(getNewVehicle());
	    
	    return vehicles;
	}
	
	public static Vehicle getNewVehicle() {
	    
	    Vehicle vehicle = new Vehicle();
	    VehicleType vehicleType = VehicleType.CAR;
        vehicleStatus status = vehicleStatus.AVAILABLE;
        int vehicleId = (int)(Math.random() * 9000) + 1000;
        vehicle.setVehicleCompanyName("Toyota");
        vehicle.setVehicleId(vehicleId);
        vehicle.setVehicleType(vehicleType);
        vehicle.setVehicleAverage(25);
        vehicle.setModelName("Camry");
        vehicle.setNumberOfSeats(5);
        vehicle.setHourlyRentalCost(15.99);
        vehicle.setVehicleStatus(status);
        
        return vehicle;
	}
}
