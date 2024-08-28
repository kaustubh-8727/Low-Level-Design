/*

1. user
2. location
3. rooms -> luxury rooms, normal rooms, sweet rooms
4. rooms inventory
5. hotel
6. hotel manager

*/

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;


enum RoomType {
    SINGLE,
    DOUBLE,
    SUIT,
    DELUX
}

enum RoomStatus {
    BOOKED,
    MAINTAINANCE,
    AVAILABLE,
    OCCUPIED
}

enum BillStatus {
    PENDING,
    COMPLETE,
    FAILED
}

enum ReservationStatus {
    RESERVED,
    CANCELLED
}

abstract class Room {
    
    protected String roomNumber;
    protected RoomType roomType;
    protected RoomStatus roomStatus;
    protected double roomPrice;
    
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }
    
    public void setReserve() {
        roomStatus = RoomStatus.BOOKED;
    }
    
    public void cancelReserve() {
        roomStatus = RoomStatus.AVAILABLE;
    }
    
    public void checkIn() {
        roomStatus = RoomStatus.OCCUPIED;
    }
    
    public void checkOut() {
        roomStatus = RoomStatus.AVAILABLE;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }
}

class SingleRoom extends Room {
    
    public SingleRoom(String roomNumber, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
        this.roomType = RoomType.SINGLE;
        this.roomPrice = 2000.0;
    }
}

class DoubleRoom extends Room {
    
    public DoubleRoom(String roomNumber, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
        this.roomType = RoomType.DOUBLE;
        this.roomPrice = 4000.0;
    }
}

class SuitRoom extends Room {
    
    public SuitRoom(String roomNumber, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
        this.roomType = RoomType.SUIT;
        this.roomPrice = 6000.0;
    }
}

class DeluxRoom extends Room {
    
    public DeluxRoom(String roomNumber, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
        this.roomType = RoomType.DELUX;
        this.roomPrice = 8000.0;
    }
}

class RoomController {
    
    List<Room> roomsList = new ArrayList<>();
    
    public void addRoom(Room room) {
        roomsList.add(room);
    }
    
    public void removeRoom(Room room) {
        roomsList.remove(room);
    }
    
    public Room getRoom(String roomNumber) {
        for(Room room : roomsList) {
            if(room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
    
    public Room getSingleRooms() {
        
        for(Room room : roomsList) {
            if(room.getRoomType() == RoomType.SINGLE && room.getRoomStatus() == RoomStatus.AVAILABLE) {
                return room;
            }
        }
        
        return null;
    }
    
    public Room getDoubleRooms() {
        for(Room room : roomsList) {
            if(room.getRoomType() == RoomType.DOUBLE && room.getRoomStatus() == RoomStatus.AVAILABLE) {
                return room;
            }
        }
        
        return null;
    }
    
    public Room getSuitRooms() {
        for(Room room : roomsList) {
            if(room.getRoomType() == RoomType.SUIT && room.getRoomStatus() == RoomStatus.AVAILABLE) {
                return room;
            }
        }
        
        return null;
    }
    
    public Room getDeluxRooms() {
        for(Room room : roomsList) {
            if(room.getRoomType() == RoomType.DELUX && room.getRoomStatus() == RoomStatus.AVAILABLE) {
                return room;
            }
        }
        
        return null;
    }
}

class Hotel {
    private String hotelName;
    private double hotelRating;
    private Location hotelAddress;
    private String contactNumber;
    private String emailId;
    private RoomController roomController;

    public String getHotelName() {
        return hotelName;
    }

    public double getHotelRating() {
        return hotelRating;
    }

    public Location getHotelAddress() {
        return hotelAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public RoomController getRoomController() {
        return roomController;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setHotelRating(double hotelRating) {
        this.hotelRating = hotelRating;
    }

    public void setHotelAddress(Location hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
    }
}


class HotelManagement {

    private Hotel hotel;
    private List<Reservation> roomReservationList = new ArrayList<>();

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    
    public Room getRoom(RoomType roomType) {
        switch(roomType) {
            case SINGLE:
                return hotel.getRoomController().getSingleRooms();
            case DOUBLE:
                return hotel.getRoomController().getDoubleRooms();
            case SUIT:
                return hotel.getRoomController().getSuitRooms();
            case DELUX:
                return hotel.getRoomController().getDeluxRooms();
            default:
                return null;
        }
    }
    
    public Reservation bookRoom(Room room, LocalDate checkInDate, LocalDate checkOutDate, User user) {
        Reservation reservation = new Reservation();
        roomReservationList.add(reservation);
        return reservation.createReservation(room, checkInDate, checkOutDate, user);
    }
    
    public Reservation cancelBooking(String reservationId) {
        Reservation reservation = getRoomReservation(reservationId);
        if (reservation == null) {
            throw new IllegalStateException("Reservation not found for the given reservation ID: " + reservationId);
        }
        reservation.cancelReservation();
        return reservation;
    }
    
    public void roomCheckIn(String reservationId) {
        Reservation reservation = getRoomReservation(reservationId);
        if (reservation == null) {
            throw new IllegalStateException("Reservation not found for the given reservation ID: " + reservationId);
        }
        reservation.checkIn();
    }
    
    public void roomCheckOut(String reservationId) {
        Reservation reservation = getRoomReservation(reservationId);
        if (reservation == null) {
            throw new IllegalStateException("Reservation not found for the given reservation ID: " + reservationId);
        }
        reservation.checkOut();
    }
    
    public void completePayment(String reservationId, PaymentMode paymentMode) {
        Reservation reservation = getRoomReservation(reservationId);
        if (reservation == null) {
            throw new IllegalStateException("Reservation not found for the given reservation ID: " + reservationId);
        }
        Bill bill = new Bill();
        bill.generateBill(reservation);
        Payment payment = new Payment();
        payment.makePayment(paymentMode, bill);
        bill.printBill();
    }
    
    public Reservation getRoomReservation(String reservationId) {
        for(Reservation reservation : roomReservationList) {
            return reservation;
        }
        
        return null;
    }
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
    String userName;
    String emailId;
    String contactNumber;
    String userId;
    
    public User(String userName, String emailId, String contactNumber, String userId) {
        this.userName = userName;
        this.emailId = emailId;
        this.contactNumber = contactNumber;
        this.userId = userId;
    }
}

class Reservation {
    
    String reservationId;
    Room room;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    User user;
    ReservationStatus reservationStatus;
    
    public Reservation createReservation(Room room, LocalDate checkInDate, LocalDate checkOutDate, User user) {
        if (validateReservation(room, user)) {
            this.reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.room = room;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.user = user;
            this.reservationStatus = ReservationStatus.RESERVED;
            room.setReserve();
            return this;
        } else {
            throw new IllegalStateException("Reservation validation failed for the room: " + room.getRoomNumber());
        }
    }
    
    public void cancelReservation() {
        if (reservationStatus == ReservationStatus.RESERVED) {
            this.reservationStatus = ReservationStatus.CANCELLED;
            room.cancelReserve();
        } else {
            throw new IllegalStateException("Cannot cancel a reservation that is not currently reserved.");
        }
    }
    
    public void checkIn() {
        if (room.getRoomStatus() == RoomStatus.BOOKED) {
            room.checkIn();
        } else {
            throw new IllegalStateException("Cannot check in. Room is not booked.");
        }
    }
    
    public void checkOut() {
        if (room.getRoomStatus() == RoomStatus.OCCUPIED) {
            room.checkOut();
        } else {
            throw new IllegalStateException("Cannot check out. Room is not occupied.");
        }
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public String getId() {
        return reservationId;
    }
    
    public boolean validateReservation(Room room, User user) {
        boolean roomValidation = true;
        boolean userValidation = true;
        
        if(room.getRoomStatus() != RoomStatus.AVAILABLE) {
            roomValidation = false;
        }
        
        // you can add user validation logic here
        
        return roomValidation && userValidation;
    }
}

class Bill {
    
    String billNumber;
    Reservation reservation;
    double totalAmount;
    BillStatus billStatus;
    
    public void generateBill(Reservation reservation) {
        this.billNumber = "BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.reservation = reservation;
        this.totalAmount = calculateAmount();
        this.billStatus = BillStatus.PENDING;
    }
    
    private double calculateAmount() {
        long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        return days * reservation.getRoom().getRoomPrice();
    }
    
    public double getBillAmount() {
        return totalAmount;
    }
    
    public void setBillStatusSuccess() {
        this.billStatus = BillStatus.COMPLETE;
    }
    
    public void setBillStatusFailed() {
        this.billStatus = BillStatus.FAILED;
    }
    
    public void printBill() {
        System.out.println("----- Bill Details -----");
        System.out.println("Bill Number: " + billNumber);
        System.out.println("Reservation ID: " + reservation.getId());
        System.out.println("User Name: " + reservation.user.userName);
        System.out.println("Room Number: " + reservation.getRoom().getRoomNumber());
        System.out.println("Room Type: " + reservation.getRoom().getRoomType());
        System.out.println("Check-In Date: " + reservation.getCheckInDate());
        System.out.println("Check-Out Date: " + reservation.getCheckOutDate());
        System.out.println("Total Amount: $" + totalAmount);
        System.out.println("Bill Status: " + billStatus);
        System.out.println("------------------------");
    }

}

class Payment {
    public boolean makePayment(PaymentMode paymentMode, Bill bill) {
        return paymentMode.makePayment(bill);
    }
}

interface PaymentMode {
    public boolean makePayment(Bill bill);
}

class UpiPayment implements PaymentMode {
    public boolean makePayment(Bill bill) {
        bill.setBillStatusSuccess();
        return true;
    }
}

class CardPayment implements PaymentMode {
    public boolean makePayment(Bill bill) {
        bill.setBillStatusSuccess();
        return true;
    }
}

class CashPayment implements PaymentMode {
    public boolean makePayment(Bill bill) {
        bill.setBillStatusSuccess();
        return true;
    }
}

public class HotelManagementSystem {
	public static void main(String[] args) {
		
		// create user
		User user = new User("milly", "milly@gmail.com", "988674845985", "PAN-WE56ER5");
		
		// create some rooms
		Room room1 = new SingleRoom("RS-001", RoomStatus.AVAILABLE);
		Room room2 = new SingleRoom("RS-002", RoomStatus.AVAILABLE);
		Room room3 = new DoubleRoom("RD-003", RoomStatus.AVAILABLE);
		Room room4 = new DoubleRoom("RD-004", RoomStatus.AVAILABLE);
		Room room5 = new SuitRoom("RSU-005", RoomStatus.AVAILABLE);
		Room room6 = new DeluxRoom("RDX-006", RoomStatus.AVAILABLE);
		
		// add rooms to controller
		RoomController roomController = new RoomController();
		roomController.addRoom(room1);
		roomController.addRoom(room2);
		roomController.addRoom(room3);
		roomController.addRoom(room4);
		roomController.addRoom(room5);
		roomController.addRoom(room6);
		
		// create hotel
		Hotel hotel = new Hotel();
		hotel.setHotelName("frostLife");
		hotel.setContactNumber("8785685854");
		hotel.setRoomController(roomController);
		hotel.setHotelAddress(new Location("USA", "Arizona", "Phoenix", "zx-6756"));
		hotel.setHotelRating(4.5);
		hotel.setEmailId("frostLife@gmail.com");
		
		// start hotel room booking
		HotelManagement hotelManagement = new HotelManagement();
		hotelManagement.setHotel(hotel);
		
		// 1. user wants are delux room
		Room room = hotelManagement.getRoom(RoomType.DELUX);
		
		// 2. user reserves the room
		LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(4);
		Reservation reservation = hotelManagement.bookRoom(room, checkInDate, checkOutDate, user);
		
		// 3. user check-in the room
		hotelManagement.roomCheckIn(reservation.getId());
		
		// 4. user check-out the room
		hotelManagement.roomCheckOut(reservation.getId());
		
		// 5. make payment (user can make payment at the time of checkin or at the time of checkout)
		PaymentMode paymentMode = new CardPayment();
		hotelManagement.completePayment(reservation.getId(), paymentMode);
	}
}
