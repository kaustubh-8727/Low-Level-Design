import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

enum BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED,
    LOST
}

enum UserType {
    STUDENT,
    FACULTY,
    LIBRARIAN
}

enum ReservationStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}

enum FineStatus {
    UNPAID,
    PAID
}

class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final String category;

    private final List<BookCopy> copies = new ArrayList<>();

    public Book(String isbn, String title, String author, String category) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    public void addCopy(BookCopy copy) {
        copies.add(copy);
    }

    public List<BookCopy> getCopies() {
        return copies;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }
}

class BookCopy {

    private final String copyId;
    private final Book book;

    private BookStatus status;

    public BookCopy(String copyId, Book book) {
        this.copyId = copyId;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    public void borrow() {
        if (!isAvailable()) {
            throw new IllegalStateException("Book copy is not available");
        }

        status = BookStatus.BORROWED;
    }

    public void makeAvailable() {
        status = BookStatus.AVAILABLE;
    }

    public String getCopyId() {
        return copyId;
    }

    public Book getBook() {
        return book;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}

abstract class User {

    protected final String userId;
    protected final String name;
    protected final String email;

    protected User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public abstract int getMaxBorrowLimit();

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

class Student extends User {

    private static final int MAX_BORROW_LIMIT = 3;

    public Student(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxBorrowLimit() {
        return MAX_BORROW_LIMIT;
    }
}

class Faculty extends User {

    private static final int MAX_BORROW_LIMIT = 10;

    public Faculty(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxBorrowLimit() {
        return MAX_BORROW_LIMIT;
    }
}

class Librarian extends User {

    public Librarian(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxBorrowLimit() {
        return 0;
    }
}

class Loan {

    private final String loanId;
    private final User user;
    private final BookCopy bookCopy;

    private final LocalDate issueDate;
    private final LocalDate dueDate;

    private LocalDate returnDate;

    public Loan(
            String loanId,
            User user,
            BookCopy bookCopy,
            LocalDate issueDate,
            LocalDate dueDate) {

        this.loanId = loanId;
        this.user = user;
        this.bookCopy = bookCopy;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public void returnBook() {
        if (returnDate != null) {
            throw new IllegalStateException("Book already returned");
        }

        returnDate = LocalDate.now();
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue() {
        LocalDate date = returnDate != null
                ? returnDate
                : LocalDate.now();

        return date.isAfter(dueDate);
    }

    public long getOverdueDays() {

        LocalDate date = returnDate != null
                ? returnDate
                : LocalDate.now();

        if (!date.isAfter(dueDate)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(dueDate, date);
    }

    public String getLoanId() {
        return loanId;
    }

    public User getUser() {
        return user;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }
}

class Reservation {

    private final String reservationId;
    private final User user;
    private final Book book;
    private final LocalDateTime createdAt;

    private ReservationStatus status;

    public Reservation(
            String reservationId,
            User user,
            Book book) {

        this.reservationId = reservationId;
        this.user = user;
        this.book = book;
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.ACTIVE;
    }

    public void complete() {
        status = ReservationStatus.COMPLETED;
    }

    public void cancel() {
        status = ReservationStatus.CANCELLED;
    }

    public boolean isActive() {
        return status == ReservationStatus.ACTIVE;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getReservationId() {
        return reservationId;
    }
}

class Fine {

    private final String fineId;
    private final Loan loan;
    private final double amount;

    private FineStatus status;

    public Fine(String fineId, Loan loan, double amount) {
        this.fineId = fineId;
        this.loan = loan;
        this.amount = amount;
        this.status = FineStatus.UNPAID;
    }

    public void pay() {
        status = FineStatus.PAID;
    }

    public boolean isPaid() {
        return status == FineStatus.PAID;
    }

    public double getAmount() {
        return amount;
    }

    public FineStatus getStatus() {
        return status;
    }
}

interface FineCalculator {

    double calculate(Loan loan);
}

class DefaultFineCalculator implements FineCalculator {

    private static final double FINE_PER_DAY = 5.0;

    @Override
    public double calculate(Loan loan) {

        long overdueDays = loan.getOverdueDays();

        return overdueDays * FINE_PER_DAY;
    }
}

interface BorrowingPolicy {

    boolean canBorrow(User user, int currentBorrowedBooks);
}

class DefaultBorrowingPolicy implements BorrowingPolicy {

    @Override
    public boolean canBorrow(User user, int currentBorrowedBooks) {

        return currentBorrowedBooks < user.getMaxBorrowLimit();
    }
}

interface UserRepository {

    void save(User user);

    User findById(String userId);
}

class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public User findById(String userId) {
        return users.get(userId);
    }
}

interface BookRepository {

    void save(Book book);

    Book findByIsbn(String isbn);

    BookCopy findAvailableCopy(String isbn);
}

class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> books = new HashMap<>();

    @Override
    public void save(Book book) {
        books.put(book.getIsbn(), book);
    }

    @Override
    public Book findByIsbn(String isbn) {
        return books.get(isbn);
    }

    @Override
    public BookCopy findAvailableCopy(String isbn) {

        Book book = books.get(isbn);

        if (book == null) {
            return null;
        }

        for (BookCopy copy : book.getCopies()) {
            if (copy.isAvailable()) {
                return copy;
            }
        }

        return null;
    }
}

interface LoanRepository {

    void save(Loan loan);

    Loan findById(String loanId);

    List<Loan> findActiveLoansByUser(String userId);
}

class InMemoryLoanRepository implements LoanRepository {

    private final Map<String, Loan> loans = new HashMap<>();

    @Override
    public void save(Loan loan) {
        loans.put(loan.getLoanId(), loan);
    }

    @Override
    public Loan findById(String loanId) {
        return loans.get(loanId);
    }

    @Override
    public List<Loan> findActiveLoansByUser(String userId) {

        return loans.values()
                .stream()
                .filter(loan ->
                        loan.getUser().getUserId().equals(userId)
                        && !loan.isReturned())
                .toList();
    }
}

interface ReservationRepository {

    void save(Reservation reservation);

    List<Reservation> findActiveReservations(Book book);
}

class InMemoryReservationRepository
        implements ReservationRepository {

    private final Map<String, Reservation> reservations = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        reservations.put(
                reservation.getReservationId(),
                reservation
        );
    }

    @Override
    public List<Reservation> findActiveReservations(Book book) {

        return reservations.values()
                .stream()
                .filter(r ->
                        r.getBook().equals(book)
                        && r.isActive())
                .sorted(Comparator.comparing(
                        Reservation::getCreatedAt))
                .toList();
    }
}

interface NotificationService {

    void notifyBookAvailable(User user, Book book);
}

class EmailNotificationService
        implements NotificationService {

    @Override
    public void notifyBookAvailable(User user, Book book) {

        System.out.println(
                "Email sent to "
                + user.getEmail()
                + " : "
                + book.getTitle()
                + " is now available."
        );
    }
}

class LibraryService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    private final BorrowingPolicy borrowingPolicy;
    private final FineCalculator fineCalculator;
    private final NotificationService notificationService;

    public LibraryService(
            UserRepository userRepository,
            BookRepository bookRepository,
            LoanRepository loanRepository,
            ReservationRepository reservationRepository,
            BorrowingPolicy borrowingPolicy,
            FineCalculator fineCalculator,
            NotificationService notificationService) {

        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
        this.borrowingPolicy = borrowingPolicy;
        this.fineCalculator = fineCalculator;
        this.notificationService = notificationService;
    }

    public Loan borrowBook(String userId, String isbn) {

        User user = userRepository.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Loan> activeLoans =
                loanRepository.findActiveLoansByUser(userId);

        if (!borrowingPolicy.canBorrow(
                user,
                activeLoans.size())) {

            throw new IllegalStateException(
                    "Borrowing limit exceeded");
        }

        Book book = bookRepository.findByIsbn(isbn);

        if (book == null) {
            throw new IllegalArgumentException(
                    "Book not found");
        }

        BookCopy copy =
                bookRepository.findAvailableCopy(isbn);

        if (copy == null) {
            throw new IllegalStateException(
                    "No copy available");
        }

        copy.borrow();

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);

        Loan loan = new Loan(
                UUID.randomUUID().toString(),
                user,
                copy,
                issueDate,
                dueDate
        );

        loanRepository.save(loan);

        return loan;
    }

    public Fine returnBook(String loanId) {

        Loan loan = loanRepository.findById(loanId);

        if (loan == null) {
            throw new IllegalArgumentException(
                    "Loan not found");
        }

        if (loan.isReturned()) {
            throw new IllegalStateException(
                    "Book already returned");
        }

        double fineAmount =
                fineCalculator.calculate(loan);

        loan.returnBook();

        loan.getBookCopy().makeAvailable();

        if (fineAmount == 0) {
            notifyNextReservation(loan.getBookCopy().getBook());
            return null;
        }

        Fine fine = new Fine(
                UUID.randomUUID().toString(),
                loan,
                fineAmount
        );

        notifyNextReservation(loan.getBookCopy().getBook());

        return fine;
    }

    public Reservation reserveBook(
        String userId,
        String isbn) {

        User user = userRepository.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException(
                    "User not found");
        }

        Book book = bookRepository.findByIsbn(isbn);

        if (book == null) {
            throw new IllegalArgumentException(
                    "Book not found");
        }

        BookCopy availableCopy =
                bookRepository.findAvailableCopy(isbn);

        if (availableCopy != null) {
            throw new IllegalStateException(
                    "Book is already available");
        }

        Reservation reservation =
                new Reservation(
                        UUID.randomUUID().toString(),
                        user,
                        book
                );

        reservationRepository.save(reservation);

        return reservation;
    }

    private void notifyNextReservation(Book book) {

        List<Reservation> reservations =
                reservationRepository
                        .findActiveReservations(book);

        if (reservations.isEmpty()) {
            return;
        }

        Reservation reservation = reservations.get(0);

        notificationService.notifyBookAvailable(
                reservation.getUser(),
                book
        );

        reservation.complete();
    }
}

public class LibraryManagementSystem {

    public static void main(String[] args) {

        UserRepository userRepository = new InMemoryUserRepository();

        BookRepository bookRepository = new InMemoryBookRepository();

        LoanRepository loanRepository = new InMemoryLoanRepository();

        ReservationRepository reservationRepository = new InMemoryReservationRepository();

        BorrowingPolicy borrowingPolicy = new DefaultBorrowingPolicy();

        FineCalculator fineCalculator = new DefaultFineCalculator();

        NotificationService notificationService = new EmailNotificationService();


        LibraryService libraryService =
                new LibraryService(
                        userRepository,
                        bookRepository,
                        loanRepository,
                        reservationRepository,
                        borrowingPolicy,
                        fineCalculator,
                        notificationService
                );

        User student = new Student(
                "U1",
                "Kaustubh",
                "kaustubh@gmail.com"
        );

        User faculty = new Faculty(
                "U2",
                "Rahul",
                "rahul@gmail.com"
        );

        userRepository.save(student);
        userRepository.save(faculty);

        Book book = new Book(
                "978-0132350884",
                "Clean Code",
                "Robert C. Martin",
                "Programming"
        );

        BookCopy copy1 =
                new BookCopy("C1", book);

        BookCopy copy2 =
                new BookCopy("C2", book);

        book.addCopy(copy1);
        book.addCopy(copy2);

        bookRepository.save(book);

        System.out.println("\n--- Borrow Book ---");

        Loan loan1 =
                libraryService.borrowBook(
                        "U1",
                        "978-0132350884"
                );

        System.out.println(
                "Book borrowed successfully"
        );

        System.out.println(
                "Loan ID: " + loan1.getLoanId()
        );

        System.out.println(
                "Book: " +
                loan1.getBookCopy()
                        .getBook()
                        .getTitle()
        );

        System.out.println(
                "Copy: " +
                loan1.getBookCopy().getCopyId()
        );

        System.out.println(
                "Due Date: " +
                loan1.getDueDate()
        );

        System.out.println("\n--- Borrow Second Copy ---");

        Loan loan2 =
                libraryService.borrowBook(
                        "U2",
                        "978-0132350884"
                );

        System.out.println(
                "Second copy borrowed successfully"
        );

        System.out.println(
                "Copy: " +
                loan2.getBookCopy().getCopyId()
        );

        System.out.println(
                "\n--- Try Borrowing Third Copy ---"
        );

        try {

            libraryService.borrowBook(
                    "U1",
                    "978-0132350884"
            );

        } catch (Exception e) {

            System.out.println(
                    "Borrow failed: " +
                    e.getMessage()
            );
        }

        System.out.println(
                "\n--- Reserve Book ---"
        );

        Reservation reservation =
                libraryService.reserveBook(
                        "U1",
                        "978-0132350884"
                );

        System.out.println(
                "Reservation created successfully"
        );

        System.out.println(
                "Reserved by: " +
                reservation.getUser().getName()
        );

        System.out.println(
                "\n--- Return Book ---"
        );

        Fine fine =
                libraryService.returnBook(
                        loan1.getLoanId()
                );

        System.out.println(
                "Book returned successfully"
        );

        if (fine != null) {

            System.out.println(
                    "Fine: ₹" +
                    fine.getAmount()
            );

        } else {

            System.out.println(
                    "No fine"
            );
        }

        System.out.println(
                "\n--- Book Copy Status ---"
        );

        for (BookCopy copy : book.getCopies()) {

            System.out.println(
                    "Copy " +
                    copy.getCopyId() +
                    " -> " +
                    copy.getStatus()
            );
        }
    }
}
