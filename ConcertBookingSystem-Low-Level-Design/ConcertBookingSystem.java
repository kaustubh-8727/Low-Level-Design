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

class User {
    String username;
    String emailId;
    String contactNumber;
    Location address;

    public User(String username, Strinf emailId, String contactNumber, Location address) {
        this.username = username;
        this.emailId = emailId;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public notifyUser(Reservation reservation) {
        // print complete reservation details
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
}

class ConcertController {

    List<Concert> concertList = new ArrayList<>();

    public void addConcert(Concert concert) {
        concertList.add(concert);
    }

    public void getConcert(String concertId) {
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
        Concert concert = this.getConcert(concertId);

        if(concert != null) {
            concertList.remove(concert);
            this.addConcert(concert);
        }
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

    public Seat(String price, SeatStatus seatStatus) {
        this.price = price;
        this.seatStatus = seatStatus;
    }

    public double getSeatPrice() {
        return price;
    }

    public void updateSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }

}

class FirstClassSeat extends Seat {
    
    public FirstClassSeat(String price, SeatStatus seatStatus) {
        super(5000.00, SeatStatus.AVAILABLE);
    }
}

class SecondClassSeat extends Seat {
    
    public SecondClassSeat(String price, SeatStatus seatStatus) {
        super(3000.00, SeatStatus.AVAILABLE);
    }
}

class thirdClassSeat extends Seat {
    
    public ThirdClassSeat(String price, SeatStatus seatStatus) {
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


    public Reservation(Concert concert, User user, List<Seat> selectedSeats) {
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
