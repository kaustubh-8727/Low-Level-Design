import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

enum SeatType {
    PREMIUM,
    MIDDLE,
    LOWER
}

class Movie {
    String movieName;
    String gener;
    int movieLengthMinutes;
    
    // many other properties can be added here
    
    public Movie(String movieName) {
        this.movieName = movieName;
    }
    
    public void setMovieGener(String gener) {
        this.gener = gener;
    }
    
    public void setMovieLength(int duration) {
        this.movieLengthMinutes = duration;
    }
    
    public String getMovieName() {
        return movieName;
    }
    
    // other getters and setters can be added here
}

class MovieManager {
    
    Map<Location, List<Movie>> movieLocationList;
    List<Movie> allMovies;
    
    public MovieManager() {
        movieLocationList = new HashMap<>();
        allMovies = new ArrayList<>();
    }
    
    public void setMovieByLocation(Location location, Movie movie) {
        List<Movie> movieList = new ArrayList<>();
        if(movieLocationList.containsKey(location)) {
            movieList = movieLocationList.get(location);
        }
        
        movieList.add(movie);
        allMovies.add(movie);
        movieLocationList.put(location, movieList);
    }
    
    public List<Movie> getAllMoviesByLocation(Location location) {
        List<Movie> movieList = new ArrayList<>();
        if(movieLocationList.containsKey(location)) {
            movieList = movieLocationList.get(location);
        }
        
        return movieList;
    }
    
    public void removeMovieBylocation(Location location, Movie movie) {
        if(movieLocationList.containsKey(location)) {
            List<Movie> movieList = new ArrayList<>();
            movieList = movieLocationList.get(location);
            movieList.remove(movie);
            movieLocationList.put(location, movieList);
        }
    }
    
    public Movie getMovieByName(String movieName) {

        for(Movie movie : allMovies) {
            if((movie.getMovieName()).equals(movieName)) {
                return movie;
            }
        }
        return null;
    }

    // other getters and setters can be implemented here
}

class MovieTheater {
    
    String theaterName;
    Location theaterLocation;
    List<Hall> halls;
    List<Show> shows;
    
    public MovieTheater(String theaterName) {
        this.theaterName = theaterName;
        halls = new ArrayList<>();
        shows = new ArrayList<>();
    }
    
    public void setHall(Hall hall) {
        halls.add(hall);
    }
    
    public void setShow(Show show) {
        shows.add(show);
    }
    
    public void setLocation(Location location) {
        this.theaterLocation = location;
    }
    
    public List<Show> getListOfShowsByMovie(Movie movie) {
        List<Show> movieShows = new ArrayList<>();
        for(Show show : shows) {
            if(show.getMovieName().equals(movie.getMovieName())) {
                movieShows.add(show);
            }
        }
        
        return movieShows;
    }
    
    public List<Show> getAllShows() {
        return shows;
    }
    
    public List<Hall> getAllHalls() {
        return halls;
    }
    
    public Location getLocation() {
        return theaterLocation;
    }
    
}

class TheaterManager {
    
    Map<Location, List<MovieTheater>> theaterLocationList;
    List<MovieTheater> allTheaterList;
    
    public TheaterManager() {
        theaterLocationList = new HashMap<>();
        allTheaterList = new ArrayList<>();
    }
    
    public void addTheater(Location location, MovieTheater movieTheater) {
        List<MovieTheater> theaterList = new ArrayList<>();
        if(theaterLocationList.containsKey(location)) {
            theaterList = theaterLocationList.get(location);
        }
        
        theaterList.add(movieTheater);
        theaterLocationList.put(location, theaterList);
    }
    
    public List<MovieTheater> getTheaterListByCity(Location location) {
        List<MovieTheater> theaterList = new ArrayList<>();
        if(theaterLocationList.containsKey(location)) {
            theaterList = theaterLocationList.get(location);
        }
        
        return theaterList;
    }
    
    public Map<MovieTheater, List<Show>> getAllShows(Movie movie, Location location) {
        
        Map<MovieTheater, List<Show>> movieTheaterShows = new HashMap<>();
        List<Show> theaterShows = new ArrayList<>();
        List<MovieTheater> theaterList = new ArrayList<>();
        if(theaterLocationList.containsKey(location)) {
            theaterList = theaterLocationList.get(location);
        }
        
        for(MovieTheater movieTheater : theaterList) {
            theaterShows = movieTheater.getListOfShowsByMovie(movie);
            if(theaterShows.size() > 0) {
                movieTheaterShows.put(movieTheater, theaterShows);
            }
        }
        
        return movieTheaterShows;
        
    }
}

class Hall {
    String auditoriumName;
    List<Seat> seats;
    
    public Hall(String auditoriumName) {
        this.auditoriumName = auditoriumName;
        seats = new ArrayList<>();
    }
    
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
    
    public List<Seat> getSeats() {
        return seats;
    }
    
    public String getHallName() {
        return auditoriumName;
    }
    
    public Seat getSeat(int seatNumber) {
        for(Seat seat : seats) {
            if(seat.getSeatNumber() == seatNumber) {
                return seat;
            }
        }
        
        return null;
    }
}

class Show {
    Movie movie;
    Hall hall;
    List<Seat> bookedSeats;
    int startTime;
    
    public Show() {
        bookedSeats = new ArrayList<>();
    }
    
    public void createShow(Movie movie, Hall hall, int startTime) {
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
    }
    
    public String getMovieName() {
        return movie.getMovieName();
    }
    
    public List<Seat> getBookedSeats() {
        return bookedSeats;
    }
    
    public void setBookedSeat(Seat seat) {
        bookedSeats.add(seat);
    }
    
    public void addSeats(List<Seat> seats) {
        for(Seat seat : seats) {
            setBookedSeat(seat);
        }
    }
    
    public void removeBookedSeat(Seat seat) {
        bookedSeats.remove(seat);
    }
    
    public void setHall(Hall hall) {
        this.hall = hall;
    }
    
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    
    public Hall getHall() {
        return hall;
    }
}

class Seat {
    int seatNumber;
    SeatType seatType;
    
    public Seat(int seatNumber, SeatType seatType) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }
    
    public int getSeatNumber() {
        return seatNumber;
    }
    
    public SeatType getSeatType() {
        return seatType;
    }
    
    public double getSeatPrice() {
        if(seatType == SeatType.PREMIUM) {
            return 300.00;
        }
        else if(seatType == SeatType.MIDDLE) {
            return 250.00;
        }
        else if(seatType == SeatType.LOWER) {
            return 170.00;
        }
        
        return 0.00;
    }
    
    // other getters and setters can be implemented here
}

class Location {
    String country;
    String state;
    String city;
    
    public Location(String country, String state, String city) {
        this.country = country;
        this.state = state;
        this.city = city;
    }
    
    public void showLocation() {
        System.out.println(
            "country : " + country +
            "state : " + state +
            "city : " + city
        );
    }
}

class Booking {
    Movie movie;
    Show show;
    double price = 0.0;
    
    public Booking bookMovieTicket(Movie movie, Show show, List<Seat> bookedSeats) {
        this.movie = movie;
        this.show = show;
        this.show.addSeats(bookedSeats);
        price = getTotalAmount(bookedSeats);
        
        return this;
    }
    
    public double getTotalAmount(List<Seat> bookedSeats) {
        double totalSeatPrice = 0.00;
        
        for(Seat seat : bookedSeats) {
            totalSeatPrice += seat.getSeatPrice();
        }
        
        return totalSeatPrice;
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


public class MovieBookingApp {
    
    // creater managers
	MovieManager movieManager = new MovieManager();
	TheaterManager theaterManager = new TheaterManager();
	Location location1;
	Location location2;
		
	public static void main(String[] args) {
	    
	    MovieBookingApp bookingApp = new Main();
	    
		System.out.println("welcome to movie booking low level design\n");
		
		bookingApp.intialSetup();
		
		bookingApp.performBooking();
		
		
	}
	
	public void intialSetup() {
	    
	    // create some movies
		Movie movie1 = new Movie("superman");
		movie1.setMovieLength(150);
		Movie movie2 = new Movie("avengers:endgame");
		movie2.setMovieLength(150);
		
		// create some locations
		location1 = new Location("USA", "Arizona", "Phoenix");
		location2 = new Location("USA", "New York", "Albany");
		
		// add movie to specific locations
		movieManager.setMovieByLocation(location1, movie1);
		movieManager.setMovieByLocation(location2, movie2);
		
		// create theaters
	    MovieTheater theater1 = new MovieTheater("pvr movies");
	    theater1.setLocation(location1);
	    List<Seat> seats1 = getSeatsList();
	    Hall hall1 = new Hall("A1");
	    hall1.setSeats(seats1);
	    Show show1 = new Show();
	    show1.createShow(movie1, hall1, 150);
	    theater1.setHall(hall1);
	    theater1.setShow(show1);
	    
	    MovieTheater theater2 = new MovieTheater("srs movies");
	    theater2.setLocation(location2);
	    List<Seat> seats2 = getSeatsList();
	    Hall hall2 = new Hall("S1");
	    hall1.setSeats(seats2);
	    Show show2 = new Show();
	    show2.createShow(movie1, hall1, 150);
	    theater2.setHall(hall1);
	    theater2.setShow(show2);
	    
	    // add theater to specific locations
	    theaterManager.addTheater(location1, theater1);
	    theaterManager.addTheater(location2, theater2);
	    
	}
	
	public List<Seat> getSeatsList() {
	    List<Seat> seatsList = new ArrayList<>();
	    for(int ind = 0 ; ind <= 11 ; ind++) {
	        if(ind >= 0 && ind <= 3) {
	            seatsList.add(new Seat(ind + 1, SeatType.PREMIUM));
	        }
	        if(ind >= 4 && ind <= 7) {
	            seatsList.add(new Seat(ind + 1, SeatType.MIDDLE));
	        }
	        if(ind >= 8 && ind <= 11) {
	            seatsList.add(new Seat(ind + 1, SeatType.LOWER));
	        }
	    }
	    
	    return seatsList;
	}
	
	public void performBooking() {
	    
	    // get List of movies in Arizona
	    List<Movie> movieList = movieManager.getAllMoviesByLocation(location1);
	    
	    // get movie you want to watch
	    Movie movie = movieList.get(0);
	    
	    // get list of shows for that movie
	    Map<MovieTheater, List<Show>> theatersList = theaterManager.getAllShows(movie, location1);
	    List<Show> shows = theatersList.entrySet().iterator().next().getValue();
	    
	    // get the show you want to watch
	    Show show = shows.get(0);
	    
	    // selecte the seats you want to select
	    List<Seat> favouriteSeats = new ArrayList<>();
	    int seatNumber1 = 1;
	    int seatNumber2 = 2;
	    if(!validateSeatNumber(seatNumber1, show) || !validateSeatNumber(seatNumber2, show)) {
	        System.out.println("seats are already booked try again");
	        return;
	    }
	    
	    favouriteSeats.add(show.getHall().getSeat(2));
	    favouriteSeats.add(show.getHall().getSeat(1));
	    
	    // create booking
	    Booking movieBook = new Booking();
	    Booking booked = movieBook.bookMovieTicket(movie, show, favouriteSeats);
	    
	    // make payment
	    Payment payment = new Payment();
	    boolean paymentDone = payment.makePayment(new UpiPayment(), booked.price);
	    
	    if (paymentDone) {
            // Print booking details
            System.out.println("Payment Successful!");
            System.out.println("Movie: " + booked.movie.getMovieName());
            System.out.println("Theater: " + theatersList.entrySet().iterator().next().getKey().theaterName);
            System.out.println("Show Time: " + show.startTime);
            System.out.println("Seats Booked: ");
            for (Seat seat : favouriteSeats) {
                System.out.println("Seat Number: " + seat.getSeatNumber() + " (Type: " + seat.getSeatType() + ")");
            }
            System.out.println("Total Price: $" + booked.price);
        } else {
            System.out.println("Payment Failed! Please try again.");
        }
	}
	
	boolean validateSeatNumber(int seatNumber, Show show) {
	    for(Seat seat : show.getBookedSeats()) {
	        if(seat.getSeatNumber() == seatNumber) {
	            return false;
	        }
	    }
	    
	    return true;
	}
}
