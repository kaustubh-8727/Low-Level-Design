/*

parking low level design
1. vehicle
2. vehicle type
3. parking spot
4. reservation
5. bill
6. payment
7. parking spot controller

*/

import java.util.List;
import java.util.ArrayList;
import java.util.Date;


enum VehicleType {
    CAR,
    BIKE,
    TRUCK
}

enum ParkingStatus {
    AVAILABLE,
    BOOKED
}

enum ReservationStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}

class Vehicle {
    
    private int vehicleNumber;
    private VehicleType vehicleType;
    
    public Vehicle(int vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }
    
    public int getVehicleNumber() {
        return vehicleNumber;
    }
    
    public void setVehicleNumber(int vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}

class Car extends Vehicle {
    
    public Car(int vehicleNumber) {
        super(vehicleNumber, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    
    public Truck(int vehicleNumber) {
        super(vehicleNumber, VehicleType.TRUCK);
    }
}

class Bike extends Vehicle {
    
    public Bike(int vehicleNumber) {
        super(vehicleNumber, VehicleType.BIKE);
    }
}

class ParkingSpot {

    protected VehicleType parkingType;
    protected ParkingStatus parkingStatus;
    protected int parkingSpotId;
    protected Vehicle vehicle;
    protected double price;
    
    public void freeParking() {
        this.parkingStatus = ParkingStatus.AVAILABLE;
        this.vehicle = null;
    }
    
    public boolean isAvailable() {
        if(parkingStatus == ParkingStatus.AVAILABLE) {
            return true;
        }
        return false;
    }
    
    public void setParking(Vehicle vehicle) {
        parkingStatus = ParkingStatus.BOOKED;
        this.vehicle = vehicle;
    }
    
    public double getPrice() {
        return price;
    }
    
    public ParkingStatus getParkingStatus() {
        return parkingStatus;
    }
    
    public VehicleType getParkingtype() {
        return parkingType;
    }

}

class CarParkingSpot extends ParkingSpot {
    
    public CarParkingSpot(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
        this.parkingStatus = ParkingStatus.AVAILABLE;
        this.price = 50.0;
        this.parkingType = VehicleType.CAR;
    }
}

class BikeParkingSpot extends ParkingSpot {
    
    public BikeParkingSpot(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
        this.parkingStatus = ParkingStatus.AVAILABLE;
        this.price = 40.0;
        this.parkingType = VehicleType.BIKE;
    }
}

class TruckParkingSpot extends ParkingSpot {
    
    public TruckParkingSpot(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
        this.parkingStatus = ParkingStatus.AVAILABLE;
        this.price = 80.0;
        this.parkingType = VehicleType.TRUCK;
    }
}

class ParkingSpotController {
    
    List<ParkingSpot> parkingSpotList = new ArrayList<>();
    List<Reservation> reservationList = new ArrayList<>();
    
    public void addParkingSpot(ParkingSpot parkingSpot) {
        parkingSpotList.add(parkingSpot);
    }
    
    public void removeParkingSpot(ParkingSpot parkingSpot) {
        parkingSpotList.remove(parkingSpot);
    }
    
    public ParkingSpot getFreeParkingSpace(VehicleType parkingtype) {
        // add business logic to get free space
        
        for(ParkingSpot spot : parkingSpotList) {
            if(spot.getParkingtype() == parkingtype && spot.getParkingStatus() == ParkingStatus.AVAILABLE) {
                return spot;
            }
        }
        
        System.out.println("sorry all parking spots are full");
        return null;
    }
    
    public Reservation reserveParking(ParkingSpot parkingSpot, Vehicle vehicle, int durationInMinutes) {
        Reservation reservation = new Reservation();
        reservation.createReservation(parkingSpot, vehicle, durationInMinutes);
        reservationList.add(reservation);
        return reservation;
    }
    
    public Reservation cancelReservation(int reservationId) {
        for(Reservation reservation : reservationList) {
            if(reservation.getId() == reservationId) {
                reservation.cancelReservation();
                reservationList.remove(reservation);
                return reservation;
            }
        }
        return null;
    }
    
    public Reservation completeReservation( int reservationId) {
        for(Reservation reservation : reservationList) {
            if(reservation.getId() == reservationId) {
                reservation.completeReservation();
                return reservation;
            }
        }
        return null;
    }
}

class Reservation {
    int reservationId;
    Vehicle vehicle;
    ParkingSpot parkingSpot;
    int durationInMinutes;
    ReservationStatus reservationStatus;
    
    public void createReservation(ParkingSpot parkingSpot, Vehicle vehicle, int durationInMinutes) {
        this.reservationId = (int)(Math.random() * 9000) + 1000;
        this.parkingSpot = parkingSpot;
        this.vehicle = vehicle;
        this.durationInMinutes = durationInMinutes;
        reservationStatus = ReservationStatus.PENDING;
    }
    
    public int getId() {
        return reservationId;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public int getDuration() {
        return durationInMinutes;
    }
    
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
    
    public void completeReservation() {
        reservationStatus = ReservationStatus.COMPLETED;
        parkingSpot.setParking(vehicle);
    }
    
    public void cancelReservation() {
        reservationStatus = ReservationStatus.CANCELLED;
        parkingSpot.freeParking();
    }
}

class Bill {
    
    int billNumber;
    double totalAmount;
    Date billDate;
    Reservation reservation;
    boolean paymentSuccess;
    
    
    public Bill generateBill(Reservation reservation) {
        this.reservation = reservation;
        billNumber = (int)(Math.random() * 9000) + 1000;
        billDate = new Date();
        totalAmount = calculateTotalAmount(reservation);
        paymentSuccess = false;
        
        return this;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    private double calculateTotalAmount(Reservation reservation) {
        double parkingPrice = reservation.getParkingSpot().getPrice();
        int duration = reservation.getDuration();
        int perMinute = 10;
        int timeBlock = 0;
        if(duration % perMinute == 0) {
            timeBlock = duration / perMinute;
        } else {
            timeBlock = (duration / perMinute) + 1;
        }
        
        return (double)(timeBlock * parkingPrice);
    }
    
    public void printBill() {
        System.out.println("----- Parking Lot Bill -----");
        System.out.println("Bill Number: " + billNumber);
        System.out.println("Date: " + billDate);
        System.out.println("Vehicle Number: " + reservation.getVehicle().getVehicleNumber());
        System.out.println("Vehicle Type: " + reservation.getVehicle().getVehicleType());
        System.out.println("Parking Spot ID: " + reservation.getParkingSpot().parkingSpotId);
        System.out.println("Duration: " + reservation.getDuration() + " minutes");
        System.out.println("Total Amount: $" + totalAmount);
        System.out.println("----------------------------\n");
    }
}

class Payment {
    public boolean makePayment(PaymentMode paymentMode, double amount) {
        return paymentMode.makePayment(amount);
    }
}

interface PaymentMode {
    public boolean makePayment(double amount);
}

class UpiPayment implements PaymentMode {
    public boolean makePayment(double amount) {
        return true;
    }
}

class CardPayment implements PaymentMode {
    public boolean makePayment(double amount) {
        return true;
    }
}

public class ParkingLotSystem {
    
    ParkingSpotController parkingSpotController = new ParkingSpotController();
    
	public static void main(String[] args) {
	    
	    ParkingLotSystem parkingLotSystem = new ParkingLotSystem();
	    
		System.out.println("welcome to parking lot low level design\n");
		
		parkingLotSystem.intialize();
		
		// park a car
	    int carId = (int)(Math.random() * 9000) + 1000;
	    Vehicle carVehicle = new Car(carId);
	    int durartion = 145;
		parkingLotSystem.parkVehicle(carVehicle, durartion);
		
		// park a truck
	    int truckId = (int)(Math.random() * 9000) + 1000;
	    Vehicle truckVehicle = new Truck(truckId);
	    durartion = 208;
		parkingLotSystem.parkVehicle(truckVehicle, durartion);
		
		// park another car
	    carId = (int)(Math.random() * 9000) + 1000;
	    carVehicle = new Car(carId);
	    durartion = 145;
		parkingLotSystem.parkVehicle(carVehicle, durartion);
	}
	
	public void intialize() {
	    
	    // create some parking spots
	    ParkingSpot parkingSpot1 = new CarParkingSpot(1001);
	    ParkingSpot parkingSpot3 = new TruckParkingSpot(1003);
	    ParkingSpot parkingSpot4 = new TruckParkingSpot(1004);
	    ParkingSpot parkingSpot5 = new BikeParkingSpot(1005);
	    ParkingSpot parkingSpot6 = new BikeParkingSpot(1006);
	    
	    // add parking spots to controller
	    parkingSpotController.addParkingSpot(parkingSpot1);
	    parkingSpotController.addParkingSpot(parkingSpot3);
	    parkingSpotController.addParkingSpot(parkingSpot4);
	    parkingSpotController.addParkingSpot(parkingSpot5);
	    parkingSpotController.addParkingSpot(parkingSpot6);
	}
	
	public void parkVehicle(Vehicle vehicle, int duration) {
	    
	    // get free available parking spot
	    ParkingSpot parkingSpot = parkingSpotController.getFreeParkingSpace(vehicle.getVehicleType());
	    if(parkingSpot == null) {
	        return;
	    }
	    
	    // reserve parking spot
	    Reservation reservation = parkingSpotController.reserveParking(parkingSpot, vehicle, duration);
	    
	    // store reservation id
	    int reservationId = reservation.getId();
	    
	    // get the bill recipt
	    Bill bill = new Bill();
	    bill.generateBill(reservation);
	    
	    // make payment
	    Payment payment = new Payment();
	    boolean paymentSuccess = payment.makePayment(new UpiPayment(), bill.getTotalAmount());
	    
	    // on payment success complete reservation
	    if(paymentSuccess == true) {
	        parkingSpotController.completeReservation(reservationId);
	        // print the bill
            bill.printBill();
	    } else {
	        parkingSpotController.cancelReservation(reservationId);
	    }
	}
}
