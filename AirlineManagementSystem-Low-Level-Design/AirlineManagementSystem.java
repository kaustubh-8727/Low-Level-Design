/*

1. flights -> fare details
2. flight manager -> each airline has a flight manager that maintains list of flights
3. airline -> manages flights and reservations
4. airline manager -> manages all the available airlines, and reterive user needed flights.
5. user
6. reservation
7. booking
8. payment
9. notification
10. seat -> user decorator design pattern for calculating seat price 

*/

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

enum SeatStatus {
    AVAILABLE,
    RESERVED,
    BOOKED
}

enum SeatType {
    ECONOMY,
    BUSINESS,
    FIRST_CLASS
}

class Flight {
    
    String flightNumber;
    String source;
    String destination;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    List<Seat> availableSeats;
    double flightDefaultPrice;
    
    public Flight(String source, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, double flightDefaultPrice) {
        this.source = source;
        this.departureTime = departureTime;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
        this.flightDefaultPrice = flightDefaultPrice;
        flightNumber = "FL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        availableSeats = new ArrayList<>();
    }
    
    public void addSeat(Seat seat) {
        availableSeats.add(seat);
    }
    
    public void removeSeat(Seat seat) {
        availableSeats.remove(seat);
    }
    
    public double getFlightPrice() {
        return flightDefaultPrice;
    }
}

class FlightManager {
    
    List<Flight> flights = new ArrayList<>();
    
    public void FlightManager() {
        flights = new ArrayList<>();
    }
    
    public void addFlight(Flight flight) {
        flights.add(flight);
    }
    
    public void removeFlight(Flight flight) {
        flights.remove(flight);
    }
    
    public List<Flight> getFlights(FlightFilter flightFilter) {
        List<Flight> searchedFlights = new ArrayList<>();
        
        for (Flight flight : flights) {
            boolean sourceMatches = flightFilter.source == null || flight.source.equalsIgnoreCase(flightFilter.source);
            boolean destinationMatches = flightFilter.destination == null || flight.destination.equalsIgnoreCase(flightFilter.destination);
            boolean timeMatches = flightFilter.time == null || flight.departureTime.isAfter(flightFilter.time);
    
            if (sourceMatches && destinationMatches && timeMatches) {
                searchedFlights.add(flight);
            }
        }
        
        return searchedFlights;
    }

}

class FlightFilter {
    String source;
    String destination;
    LocalDateTime time;
    // more filters can be applied
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

class Airline {
    
    String airlineName;
    FlightManager flightManager;
    List<Reservation> reservationList;
    
    public Airline(String airlineName) {
        this.airlineName = airlineName;
        flightManager = new FlightManager();
        reservationList = new ArrayList<>();
    }
    
    public void setFlightManager(FlightManager flightManager) {
        this.flightManager = flightManager;
    }
    
    public List<Flight> getFlights(FlightFilter flightFilter) {
        return flightManager.getFlights(flightFilter);
    }
    
    public Reservation createReservation(Flight flight, User user, List<Seat> seats) {
        if(validateReservation(flight, seats)) {
            Reservation reservation = new Reservation();
            reservation.createReservation(flight, user, seats);
            return reservation;
        }
        
        return null;
    }
    
    public void cancelReservation(String reservationId) {
        Reservation reservation = getReservation(reservationId);
        reservation.cancelReservation();
    }
    
    public void completeReservation(String reservationId) {
        Reservation reservation = getReservation(reservationId);
        reservation.completeReservation();
    }
    
    public Reservation getReservation(String reservationId) {
        for(Reservation reservation : reservationList) {
            if(reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }
    
    public boolean validateReservation(Flight flight, List<Seat> seats) {
        // logic to validate the available seats;
        return true;
    }
}

class AirlineManager {
    
    List<Airline> airlines;
    
    public AirlineManager() {
        airlines = new ArrayList<>();
    }
    
    public void addAirline(Airline airline) {
        airlines.add(airline);
    }
    
    public void removeAirline(Airline airline) {
        airlines.remove(airline);
    }
    
    public Map<Airline, List<Flight>> getFlights(FlightFilter flightFilter) {
        
        List<Flight> searchedFlights = new ArrayList<>();
        Map<Airline, List<Flight>> searchedFlightsMap = new HashMap<>();
        
        for(Airline airline : airlines) {
            searchedFlights = airline.getFlights(flightFilter);
            if(searchedFlights.size() > 0) {
                searchedFlightsMap.put(airline, searchedFlights);
            }
        }
        
        return searchedFlightsMap;
    }
    
    public Reservation bookFlight(Airline airline, Flight flight, User user, List<Seat> seats) {
        return airline.createReservation(flight, user, seats);
    }
    
    public void cancelBooking(Airline airline, String reservationId) {
        airline.cancelReservation(reservationId);
    }
}

abstract class Seat {
    
    String seatNumber;
    SeatType seatType;
    SeatStatus seatStatus;
    double seatPrice;
    
    public Seat(String seatNumber, SeatType seatType, SeatStatus seatStatus, double seatPrice) {
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
        this.seatPrice = seatPrice;
    }
    
    public void updateSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }
    
    public double getPrice() {
        return seatPrice;
    }
}

class EconomyClassSeat extends Seat {
    
    public EconomyClassSeat(String seatNumber) {
        super(seatNumber, SeatType.ECONOMY, SeatStatus.AVAILABLE, 200);
    }
}

class BusinessClassSeat extends Seat {
    
    public BusinessClassSeat(String seatNumber) {
        super(seatNumber, SeatType.BUSINESS, SeatStatus.AVAILABLE, 600);
    }
}

class FirstClassSeat extends Seat {
    
    public FirstClassSeat(String seatNumber) {
        super(seatNumber, SeatType.FIRST_CLASS, SeatStatus.AVAILABLE, 1200);
    }
}

class User {
    String userName;
    String userId;
    int age;
    String contactNumber;
    String emailId;
    
    public User(String userName, String userId, String contactNumber, String emailId, int age) {
        this.userId = userId;
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.emailId = emailId;
        this.age = age;
    }
}

class Reservation {
    String reservationId;
    Flight flight;
    List<Seat> seats;
    User user;
    double price = 0.0;
    
    public void createReservation(Flight flight, User user, List<Seat> seats) {
        this.reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.flight = flight;
        this.user = user;
        this.seats = seats;
        this.price = calculatePrice();
        for(Seat seat : seats) {
            seat.updateSeatStatus(SeatStatus.RESERVED);
        }
    }
    
    public void cancelReservation() {
        for(Seat seat : seats) {
            seat.updateSeatStatus(SeatStatus.AVAILABLE);
            flight.removeSeat(seat);
        }
        
    }
    
    public void completeReservation() {
        for(Seat seat : seats) {
            seat.updateSeatStatus(SeatStatus.BOOKED);
            flight.addSeat(seat);
        }
        
        printTicketDetails();
    }
    
    public String getReservationId() {
        return reservationId;
    }
    
    public double calculatePrice() {
        double totalPrice = 0.0;
        double flightPrice = flight.getFlightPrice();
        for(Seat seat : seats) {
            totalPrice += (seat.getPrice() + flightPrice);
        }
        return totalPrice;
    }
    
    public double getReservationPrice() {
        return price;
    }
    
    public void printTicketDetails() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("User: " + user.userName + " (ID: " + user.userId + ")");
        System.out.println("Contact: " + user.contactNumber + ", Email: " + user.emailId);
        System.out.println("Flight Details:");
        System.out.println("Flight Number: " + flight.flightNumber);
        System.out.println("Source: " + flight.source + ", Destination: " + flight.destination);
        System.out.println("Departure: " + flight.departureTime + ", Arrival: " + flight.arrivalTime);
        System.out.println("Seats Reserved:");
        for(Seat seat : seats) {
            System.out.println(" - Seat Number: " + seat.seatNumber + " (" + seat.seatType + ") - Price: " + seat.getPrice());
        }
        System.out.println("Total Price: " + getReservationPrice());
        System.out.println("Reservation Status: Completed");
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

public class Main {
	public static void main(String[] args) {
		
		// create flights
		Flight flight1 = new Flight("seattle", "paris", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(6), 2000);
		Flight flight2 = new Flight("ontario", "Brisbane", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusHours(19), 2600);
		
		// create flight manager
		FlightManager flightManager = new FlightManager();
		flightManager.addFlight(flight1);
		flightManager.addFlight(flight2);
		
		// create airline
		Airline airline = new Airline("air india");
		airline.setFlightManager(flightManager);
		
		// create airline manager
		AirlineManager airlineManager = new AirlineManager();
		airlineManager.addAirline(airline);
		
		// create user
		User user = new User("jimmy", "BG6767JK", "86765656655", "jimmy@gmail.com", 28);
		
		// create flight filter
		FlightFilter flightFilter = new FlightFilter();
		flightFilter.setSource("ontario");
		flightFilter.setDestination("brisbane");
		
		// user search for flight
		Map<Airline, List<Flight>> flightMap = airlineManager.getFlights(flightFilter);
		
		// user selected the desired flight
		Airline favouriteAirline = null;
		Flight favouriteFlight = null;
		if (!flightMap.isEmpty()) {
            Map.Entry<Airline, List<Flight>> firstEntry = flightMap.entrySet().iterator().next();
            favouriteAirline = firstEntry.getKey();
            favouriteFlight = firstEntry.getValue().get(0);
        }
        
        // selets favourite seats
        List<Seat> favouriteSeats = new ArrayList<>();
        Seat seat1 = new EconomyClassSeat("SEC-1");
        Seat seat2 = new BusinessClassSeat("SEB-2");
        Seat seat3 = new FirstClassSeat("SEF-4");
        favouriteSeats.add(seat1);
        favouriteSeats.add(seat2);
        favouriteSeats.add(seat3);
        
        Reservation reservation = airlineManager.bookFlight(favouriteAirline, favouriteFlight, user, favouriteSeats);
        
        if(reservation != null) {
            Payment payment = new Payment();
            boolean paymentStatus = payment.makePayment(new UpiPayment(), reservation.getReservationPrice());
            
            if(paymentStatus == true) {
                reservation.completeReservation();
            } else {
                reservation.cancelReservation();
            }
        }
        
	}
}
