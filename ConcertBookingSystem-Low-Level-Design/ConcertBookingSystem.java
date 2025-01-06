/*

1. show
2. user
3. user management
4. show manager
5. artist
6. seat - multiple seat types
7. reservation
8. payment
9. bill
10. notification
11. concert booking system

*/

import java.util.*;

enum SeatStatus {
    AVAILABLE,
    RESERVED,
    BOOKED,
    UNDER_MAINTAINANCE
}

enum ReservartionStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}

enum BillStatus {
    PENDING,
    SUCCESS,
    FAILED
}

class User {
    String username;
    String emailId;
    String contactNumber;
    Location address;

    public User(String username, String emailId, String contactNumber, Location address) {
        this.username = username;
        this.emailId = emailId;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public void notifyUser(Reservation reservation) {
        System.out.println("Reservation Details:");
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("User: " + username + ", Email: " + emailId);
        System.out.println("Concert: " + reservation.concert.getConcertName() + " at " + reservation.concert.getVenue().city);
        System.out.println("Seats Reserved: ");
        for (Seat seat : reservation.selectedSeats) {
            System.out.println(" - Seat ID: " + seat.getSeatId() + ", Price: " + seat.getSeatPrice());
        }
        System.out.println("Total Price: " + reservation.getBillDetails().totalPrice);
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

class Concert {
    String concertId;
    private String concertName;
    private Date startDate;
    private Location venue;
    private Artist artist;
    private String description;
    private double price;
    private List<Seat> seats = new ArrayList<>();

    public Concert() {
        this.concertId = "1234";
    }

    public String getConcertId() {
        return concertId;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Location getVenue() {
        return venue;
    }

    public void setVenue(Location venue) {
        this.venue = venue;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public List<Seat> getAllSeats() {
        return seats;
    }

    public Seat getSeatById(String seatId) {
        for(Seat seat : seats) {
            if(seat.getSeatId() == seatId) {
                return seat;
            }
        }
        return null;
    }
}

class ConcertController {

    List<Concert> concertList = new ArrayList<>();

    public void addConcert(Concert concert) {
        concertList.add(concert);
    }

    public Concert getConcert(String concertId) {
        for(Concert concert : concertList) {
            if(concert.getConcertId() == concertId) {
                return concert;
            }
        }
        return null;
    }

    public void removeConcert(String concertId) {
        Concert concert = this.getConcert(concertId);

        if(concert != null) {
            concertList.remove(concert);
        }
    }

    public void updateConcert(String concertId, Concert concert) {
        Concert fetchedConcert = this.getConcert(concertId);

        if(fetchedConcert != null) {
            concertList.remove(fetchedConcert);
            this.addConcert(concert);
        }
    }

    public boolean validateSeats(Concert concert, List<Seat> selectedSeatIds) {
        for(Seat seat : selectedSeatIds) {
            if(seat.getSeatStatus() != SeatStatus.AVAILABLE) {
                return false;
            }
        }
        return true;
    }

    public List<Concert> fetchConcert(ConcertFilter concertFilter) {
        List<Concert> filteredConcerts = new ArrayList<>();
        for (Concert concert : concertList) {
            if ((concertFilter.concertName == null || concert.getConcertName().contains(concertFilter.concertName)) &&
                (concertFilter.artist == null || concert.getArtist().artistName.equalsIgnoreCase(concertFilter.artist.artistName))) {
                filteredConcerts.add(concert);
            }
        }
        return filteredConcerts;
    }
}

class Artist {
    String artistName;
    String description;
    String gener;

    public Artist(String artistName, String description, String gener) {
        this.artistName = artistName;
        this.description = description;
        this.gener = gener;
    }
}

abstract class Seat {

    String seatId;
    double price;
    SeatStatus seatStatus;

    public Seat(double price, SeatStatus seatStatus) {
        this.seatId = "1234";
        this.price = price;
        this.seatStatus = seatStatus;
    }

    public String getSeatId() {
        return seatId;
    }

    public double getSeatPrice() {
        return price;
    }

    public void updateSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

}

class FirstClassSeat extends Seat {
    
    public FirstClassSeat() {
        super(5000.00, SeatStatus.AVAILABLE);
    }
}

class SecondClassSeat extends Seat {
    
    public SecondClassSeat() {
        super(3000.00, SeatStatus.AVAILABLE);
    }
}

class ThirdClassSeat extends Seat {
    
    public ThirdClassSeat() {
        super(1000.00, SeatStatus.AVAILABLE);
    }
}

class Reservation {
    String reservationId;
    ReservartionStatus reservationStatus;
    Bill bill;
    Concert concert;
    User user;
    List<Seat> selectedSeats;


    public Reservation(User user, Concert concert, List<Seat> selectedSeats) {
        this.reservationId = "1234";
        this.concert = concert;
        this.user = user;
        reservationStatus = ReservartionStatus.PENDING;
        for(Seat seat : selectedSeats) {
            seat.updateSeatStatus(SeatStatus.RESERVED);
        }
        this.selectedSeats = selectedSeats;
        this.bill = new Bill();
        this.bill.generateBill(selectedSeats, concert);
    }

    public String getReservationId() {
        return reservationId;
    }

    public void updateReservationStatus(ReservartionStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void completeReservation() {
        this.updateReservationStatus(ReservartionStatus.COMPLETED);
        bill.updateBillStatus(BillStatus.SUCCESS);
        for(Seat seat : selectedSeats) {
            seat.updateSeatStatus(SeatStatus.BOOKED);
        }
    }

    public void cancelReservation() {
        this.updateReservationStatus(ReservartionStatus.CANCELLED);
        for(Seat seat : selectedSeats) {
            seat.updateSeatStatus(SeatStatus.AVAILABLE);
        }
    }

    public Bill getBillDetails() {
        return bill;
    }

    public User getUser() {
        return user;
    }
}

class Bill {
    String billId;
    String concertName;
    double totalPrice;
    Date billDate;
    BillStatus billStatus;

    public Bill() {
        this.billId = "1234";
        billDate = new Date();
        billStatus = BillStatus.PENDING;
    }

    public void generateBill(List<Seat>  selectedSeats, Concert concert) {
        this.concertName = concert.getConcertName();
        this.totalPrice = getPrice(selectedSeats, concert);
    }

    public double getPrice(List<Seat> selectedSeats, Concert concert) {
        double price = 0.0;
        price = concert.getPrice();

        for(Seat seat : selectedSeats) {
            price = price + seat.getSeatPrice() + ((seat.getSeatPrice() * 5) / 100);
        }

        return price;
    }

    public void updateBillStatus(BillStatus billStatus) {
        this.billStatus = billStatus;
    }
}

class PaymentService {
    Payment payment;

    public PaymentService(Payment payment) {
        this.payment = payment;
    }

    public boolean makePayment(Bill bill) {
        return payment.makePayment(bill);
    }
}

interface Payment {
    boolean makePayment(Bill bill);
}

class UPIPayment implements Payment {
    public boolean makePayment(Bill bill) {
        return true;
    }
}

class CardPayment implements Payment {
    public boolean makePayment(Bill bill) {
        return true;
    }
}

class ConcertFilter {

    Artist artist;
    String concertName;

    public ConcertFilter(Artist artist, String concertName) {
        this.artist = artist;
        this.concertName = concertName;
    }
}

class ConcertBookingService {
    PaymentService paymentService = null;
    ConcertController concertController = null;
    NotificationService notificationService = null;
    List<Reservation> reservationList = new ArrayList<>();

    public ConcertBookingService(ConcertController concertController, PaymentService paymentService) {
        this.concertController = concertController;
        this.paymentService = paymentService;
        this.notificationService = new NotificationService();
    }

    public void addConcert(Concert concert) {
        concertController.addConcert(concert);
    }

    public void removeConcert(String concertId) {
        concertController.removeConcert(concertId);
    }

    public void updateConcert(String concertId, Concert concert) {
        concertController.updateConcert(concertId, concert);
    }

    public List<Concert> fetchConcert(ConcertFilter concertFilter) {
        return concertController.fetchConcert(concertFilter);
    }

    public Reservation bookConcert(User user, Concert concert, List<Seat> selectedSeats) {
        if(concertController.validateSeats(concert, selectedSeats)) {
            Reservation reservation = new Reservation(user, concert, selectedSeats);
            reservationList.add(reservation);
            return reservation;
        } else {
            System.out.println("seats are already booked please try other seats");
        }
        return null;
    }
    
    public Reservation getReservation(String reservationId) {
        for(Reservation reservation : reservationList) {
            if(reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        
        return null;
    }

    public void makeConcertPayment(String reservationId) {
        Reservation reservation = getReservation(reservationId);

        if(reservation == null) {
            return;
        }

        Bill bill = reservation.getBillDetails();
        boolean paymentStatus = paymentService.makePayment(bill);

        if(paymentStatus == true) {
            reservation.completeReservation();
        }
        notificationService.notifyUser(reservation);
    }
}

class NotificationService {

    public void notifyUser(Reservation reservation) {
        User user = reservation.getUser();
        user.notifyUser(reservation);
    }
}

class Main {
    public static void main(String []args) {

        ConcertController concertController = new ConcertController();
        PaymentService paymentService = new PaymentService(new UPIPayment());

        // create seats
        Seat seat1 = new FirstClassSeat();
        Seat seat2 = new SecondClassSeat();
        Seat seat3 = new ThirdClassSeat();
        Seat seat4 = new FirstClassSeat();

        // create concert location
        Location concertLocation = new Location("India", "Himachal Pradesh", "Shimla", "171004");

        // create artist for concert
        Artist artist = new Artist("ed shreen", "music artist", "music");

        // create concert
        Concert concert = new Concert();
        concert.setConcertName("magic music show");
        concert.setStartDate(new Date());
        concert.setDescription("music show that will rock the universe");
        concert.setVenue(concertLocation);
        concert.setArtist(artist);
        concert.addSeat(seat1);
        concert.addSeat(seat2);
        concert.addSeat(seat3);
        concert.addSeat(seat4);
        concert.setPrice(2000.00);
        
        concertController.addConcert(concert);
        ConcertBookingService concertBookingService = new ConcertBookingService(concertController, paymentService);
        

        // create user address
        Location userLocation = new Location("India", "UP", "Banaras", "374745");

        // create user
        User user = new User("jack", "jack@gmail.com", "4757457575", userLocation);

        // 1. user searchs for concert based on concert name
        ConcertFilter concertFilter = new ConcertFilter(null, "magic music show");
        List<Concert> fetchedConcerts = concertBookingService.fetchConcert(concertFilter);
        
        // 2. user selects the favourite concert
        Concert favouriteConcert = fetchedConcerts.get(0);

        // 3. user selects seats for booking
        List<Seat> favouriteSeats = new ArrayList<>();
        seat1 = favouriteConcert.getAllSeats().get(0);
        seat2 = favouriteConcert.getAllSeats().get(1);
        favouriteSeats.add(seat1);
        favouriteSeats.add(seat2);

        // 4. user book concert tickets
        Reservation reservation = concertBookingService.bookConcert(user, favouriteConcert, favouriteSeats);
        
        if(reservation != null) {
            // 5. user make final payment
            concertBookingService.makeConcertPayment(reservation.getReservationId());
        }

    }
}
