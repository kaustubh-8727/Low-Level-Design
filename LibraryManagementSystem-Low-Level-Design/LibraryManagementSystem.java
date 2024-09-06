import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

enum Role {
    ADMIN,
    MEMBER
}

enum Status {
    ACTIVE,
    NON_ACTIVE
}

enum ReservationStatus {
    RESERVED,
    RETURNED
}

enum Category {
    COMIC,
    DOCUMENTRY,
    SCIENCE,
    HISTORY,
    FICTION,
    NON_FICTION,
    FANTASY
}

enum BookStatus {
    AVAILABLE,
    ISSUED
}

abstract class User {
    String name;
    String userId;
    Location address;
    Role role;

    public User(String name, String userId, Location address, Role role) {
        this.name = name;
        this.userId = userId;
        this.address = address;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public Location getAddress() {
        return address;
    }

    public Role getRole() {
        return role;
    }
}

class Librarian extends User {
    public Librarian(String name, String userId, Location address) {
        super(name, userId, address, Role.ADMIN);
    }
}

class Member extends User {
    
    LibraryCard libraryCard;
    private List<Fine> fines;

    public Member(String name, String userId, Location address) {
        super(name, userId, address, Role.MEMBER);
    }

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(LibraryCard libraryCard) {
        this.libraryCard = libraryCard;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.libraryCard.setExpiryDate(expiryDate);
    }
    
    public int getActiveBookReservation() {
        return libraryCard.getActiveBookReservation().size();
    }
    
    public void addFine(Fine fine) {
        fines.add(fine);
    }

    public List<Fine> getFines() {
        return fines;
    }
    
    public void printMemberDetails() {
        System.out.println("Member Name: " + this.getName());
        System.out.println("Member ID: " + this.getUserId());
        System.out.println("Address: " + this.getAddress().city + ", " + this.getAddress().state + ", " + this.getAddress().country + " - " + this.getAddress().pincode);
        System.out.println("Library Card ID: " + this.libraryCard.cardId);
        System.out.println("Library Card Status: " + this.libraryCard.getCardStatus());
        System.out.println("Issued Date: " + this.libraryCard.issueDate);
        System.out.println("Expiry Date: " + this.libraryCard.expiryDate);
        System.out.println("Active Reservations: " + this.getActiveBookReservation());

        System.out.println("\nReservation History:");
        for (Reservation reservation : this.libraryCard.reservedHistoryList) {
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Book Title: " + reservation.book.getBookTitle());
            System.out.println("Book Author: " + reservation.book.getBookAuthor());
            System.out.println("Reserved Date: " + reservation.reservedDate);
            System.out.println("Expiry Date: " + reservation.expiryDate);
            System.out.println("Reservation Status: " + reservation.reservationStatus);
            System.out.println("------------------------------");
        }
    }
}

class LibraryCard {
    
    LocalDate issueDate;
    LocalDate expiryDate;
    Status cardStatus;
    String cardId;
    List<Reservation> reservedHistoryList = new ArrayList<>();
    List<Reservation> activeReservedList = new ArrayList<>();
    
    public LibraryCard(LocalDate issueDate) {
        cardId = "LC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.issueDate = issueDate;
        this.expiryDate = issueDate.plusDays(30);
        this.cardStatus = Status.ACTIVE;
    }
    
    public Status getCardStatus() {
        return cardStatus;
    }
    
    public void updateCardStatus(Status cardStatus) {
        this.cardStatus = cardStatus;
    }
    
    public void addBookReservation(Reservation reservation) {
        activeReservedList.add(reservation);
        reservedHistoryList.add(reservation);
    }
    
    public List<Reservation> getActiveBookReservation() {
        return activeReservedList;
    }
    
    public void removeReservation(Reservation reservation) {
        activeReservedList.remove(reservation);
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}

class Book {
    
    String ISBN;
    String author;
    String publisher;
    String title;
    Category bookCategory;
    BookStatus bookStatus;
    
    public Book(String author, String publisher, String title, Category bookCategory) {
        this.ISBN = "ISBN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.author = author;
        this.publisher = publisher;
        this.title = title;
        this.bookCategory = bookCategory;
        this.bookStatus = BookStatus.AVAILABLE;
    }
    
    public void updateBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }
    
    public String getBookAuthor() {
        return author;
    }
    
    public String getBookTitle() {
        return title;
    }
    
    public String getBookISBN() {
        return ISBN;
    }
    
    public Category getBookCategory() {
        return bookCategory;
    }
    
}

class BookService {
    
    List<Book> allBooksList = new ArrayList<>();

    public void addBook(Book book) {
        allBooksList.add(book);
    }

    public void removeBook(Book book) throws Exception {
        if (!allBooksList.remove(book)) {
            throw new Exception("Book with ISBN " + book.getBookISBN() + " not found");
        }
    }

    public List<Book> searchBook(String keyword, SearchBookService searchBookService) throws Exception {
        List<Book> matchedBooks = new ArrayList<>();

        for (Book book : allBooksList) {
            if (searchBookService.searchBook(book, keyword)) {
                matchedBooks.add(book);
            }
        }

        if (matchedBooks.isEmpty()) {
            throw new Exception("No book found matching the keyword: " + keyword);
        }

        return matchedBooks;
    }
}


abstract class SearchBookService {
    
    public abstract boolean searchBook(Book book, String keyword);
    public abstract boolean searchBook(Book book, Category category);
}

class SearchBookByAuthor extends SearchBookService {
    
    public boolean searchBook(Book book, String keyword) {
        if(book.getBookAuthor() == keyword) {
            return true;
        }
        return false;
    }
    
    public boolean searchBook(Book book, Category category) {
        // Not applicable for title search, return false
        return false;
    }
}

class SearchBookByTitle extends SearchBookService {
    
    public boolean searchBook(Book book, String keyword) {
        if(book.getBookTitle() == keyword) {
            return true;
        }
        return false;
    }
    
    public boolean searchBook(Book book, Category category) {
        // Not applicable for title search, return false
        return false;
    }
}

class SearchBookByISBN extends SearchBookService {
    
    public boolean searchBook(Book book, String keyword) {
        if(book.getBookISBN() == keyword) {
            return true;
        }
        return false;
    }
    
    public boolean searchBook(Book book, Category category) {
        // Not applicable for title search, return false
        return false;
    }
}

class SearchBookByCategory extends SearchBookService {
    
    public boolean searchBook(Book book, Category keyword) {
        if(book.getBookCategory() == keyword) {
            return true;
        }
        return false;
    }
    
    public boolean searchBook(Book book, String keyword) {
        // Not applicable for title search, return false
        return false;
    }
}

class Library {
    
    String libraryName;
    Location address;
    String description;
    BookService bookService;
    Librarian librarian;
    
    public Library(String libraryName, Location address, String description) {
        this.libraryName = libraryName;
        this.address = address;
        this.description = description;
    }
    
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
    
    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }
    
    public BookService getBookService() {
        return bookService;
    }
}

class LibraryManager {
    
    Library library;
    List<Member> membersList = new ArrayList<>();
    List<Reservation> reservationsList = new ArrayList<>();
    int MAX_BOOK_ALLOTMENT = 5;
    
    public void setLibrary(Library library) {
        this.library = library;
    }
    
    public void registerMember(Member member) {
        LibraryCard libraryCard = new LibraryCard(LocalDate.now());
        member.setLibraryCard(libraryCard);
        membersList.add(member);
    }
    
    public void cancelMemberShip(String memberId) {
        Member member = getMember(memberId);
        
        if(member != null) {
            member.getLibraryCard().updateCardStatus(Status.NON_ACTIVE);
            membersList.remove(member);
        }
    }
    
    public void renewMemberShip(String memberId) {
        Member member = getMember(memberId);
        
        if(member != null) {
            member.setExpiryDate(LocalDate.now().plusDays(30));
        }
    }
    
    public List<Book> searchBook(String keyword, SearchBookService searchBookService) throws Exception {
        return library.getBookService().searchBook(keyword, searchBookService);
    }
    
    public Reservation issueBook(String memberId, String bookISBN) throws Exception {
        Book book = library.getBookService().searchBook(bookISBN, new SearchBookByISBN()).get(0);
        Member member = getMember(memberId);
        
        boolean isValid = validateBookAllotment(member, book);
        
        if(isValid == false) {
            System.out.println("book cannot be issued");
            return null;
        }
        
        Reservation reservation = new Reservation();
        reservation.createReservation(member, book);
        reservation.printReservationDetails();
        
        reservationsList.add(reservation);
        
        return reservation;
    }
    
    public void returnBook(String reservationId) {
        Reservation reservation = getReservation(reservationId);
        
        boolean isValid = validateBookDeallotment(reservation.member, reservation.book);
        
        if(isValid == false) {
            System.out.println("book cannot be returned");
            return;
        }
        
        reservation.endReservation();
        reservationsList.remove(reservation);
    }
    
    public Member getMember(String memberId) {
        for (Member member : membersList) {
            if (member.userId.equals(memberId)) {
                return member;
            }
        }
        return null;
    }
    
    public Reservation getReservation(String reservationId) {
        
        for(Reservation reservation : reservationsList) {
            if(reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        
        return null;
    }
    
    public boolean validateBookAllotment(Member member, Book book) {

        if (member == null) {
            System.out.println("Member not found");
            return false;
        }
        
        if (book == null || book.bookStatus != BookStatus.AVAILABLE) {
            System.out.println("Book not available for allotment");
            return false;
        }
        
        if (member.getLibraryCard().getCardStatus() != Status.ACTIVE) {
            System.out.println("Member's library card is not active");
            return false;
        }
        
        if(member.getActiveBookReservation() == MAX_BOOK_ALLOTMENT) {
            System.out.println("Member's reserved books limit exceeded");
            return false;
        }
        
        return true;
    }
    
    public boolean validateBookDeallotment(Member member, Book book) {

        if (member == null) {
            System.out.println("Member not found");
            return false;
        }
        
        if (book == null || book.bookStatus != BookStatus.ISSUED) {
            System.out.println("Book not available for allotment");
            return false;
        }
        
        if (member.getLibraryCard().getCardStatus() != Status.ACTIVE) {
            System.out.println("Member's library card is not active");
            return false;
        }
        
        return true;
    }

}

class Reservation {
    
    String reservationId;
    Book book;
    Member member;
    LocalDate reservedDate;
    LocalDate expiryDate;
    ReservationStatus reservationStatus;
    Fine fine;
    
    public void createReservation(Member member, Book book) {
        this.reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.book = book;
        this.member = member;
        this.reservedDate = LocalDate.now();
        this.expiryDate = reservedDate.plusDays(10);
        this.reservationStatus = ReservationStatus.RESERVED;
        
        this.member.getLibraryCard().addBookReservation(this);
        this.book.updateBookStatus(BookStatus.ISSUED);
    }
    
    public String getReservationId() {
        return reservationId;
    }
    
    public void endReservation() {
        
        LocalDate returnDate = LocalDate.now();
        
        if (returnDate.isAfter(expiryDate)) {
            long daysLate = ChronoUnit.DAYS.between(expiryDate, returnDate);
            double fineAmount = calculateFine(daysLate);
            this.fine = new Fine(fineAmount, returnDate);
            member.addFine(fine);
            System.out.println("Book returned late! Fine imposed: " + fineAmount);
        }
        
        this.reservationStatus = ReservationStatus.RETURNED;
        this.member.getLibraryCard().removeReservation(this);
        this.book.updateBookStatus(BookStatus.AVAILABLE);
    }
    
    private double calculateFine(long daysLate) {
        return daysLate * 10;
    }

    public Fine getFine() {
        return fine;
    }
    
    public void printReservationDetails() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Book Title: " + book.getBookTitle());
        System.out.println("Book Author: " + book.getBookAuthor());
        System.out.println("Member Name: " + member.getName());
        System.out.println("Member ID: " + member.getUserId());
        System.out.println("Reservation Date: " + reservedDate);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("Reservation Status: " + reservationStatus);
        System.out.println();
    }
}

class Fine {
    private double amount;
    private LocalDate fineDate;
    private boolean isPaid;

    public Fine(double amount, LocalDate fineDate) {
        this.amount = amount;
        this.fineDate = fineDate;
        this.isPaid = false;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getFineDate() {
        return fineDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void payFine() {
        this.isPaid = true;
    }

    public String toString() {
        return "Fine Amount: " + amount + ", Fine Date: " + fineDate + ", Paid: " + isPaid;
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

public class Main {
	public static void main(String[] args) {
		
		try {
            // create books
            Book book1 = new Book("George Orwell", "Secker & Warburg", "1984", Category.FICTION);
            Book book2 = new Book("Stephen Hawking", "Bantam Books", "A Brief History of Time", Category.SCIENCE);
            Book book3 = new Book("J.K. Rowling", "Bloomsbury", "Harry Potter and the Philosopher's Stone", Category.FANTASY);
            Book book4 = new Book("Yuval Noah Harari", "Harper", "Sapiens: A Brief History of Humankind", Category.NON_FICTION);
            Book book5 = new Book("F. Scott Fitzgerald", "Charles Scribner's Sons", "The Great Gatsby", Category.FICTION);

            // create book service
            BookService bookService = new BookService();
            bookService.addBook(book1);
            bookService.addBook(book2);
            bookService.addBook(book3);
            bookService.addBook(book4);
            bookService.addBook(book5);
            
            // create Librarian
            Librarian librarian = new Librarian("jack", "lib-6556", new Location("USA", "Arizona", "Phoenix", "zx-6756"));
            
            // create member
            Member member = new Member("mike", "MEM-9090", new Location("USA", "Arizona", "Phoenix", "zx-6756"));
            
            // create library
            Library library = new Library("book world", new Location("USA", "Arizona", "Phoenix", "zx-6756"), "book reader world");
            library.setBookService(bookService);
            
            // create library manager
            LibraryManager libraryManager = new LibraryManager();
            libraryManager.setLibrary(library);
            
            // 1. member comes and apply for membership
            libraryManager.registerMember(member);
            
            // 2. member searches for book by title
            List<Book> matchedBooks = libraryManager.searchBook("A Brief History of Time", new SearchBookByTitle());
            
            // 3. member selects favourite book
            Book favouriteBook = matchedBooks.get(0);
            
            // 4. member issue the book
            Reservation reservation = libraryManager.issueBook(member.getUserId(), favouriteBook.getBookISBN());
            
            // 5. member cancels reservation
            libraryManager.returnBook(reservation.getReservationId());
            
            // 6. print reservation after return
            reservation.printReservationDetails();
		
		}
		catch(Exception e) {
		    System.out.println(e);
		}
	}
}
