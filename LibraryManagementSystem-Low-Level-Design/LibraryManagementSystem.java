/*

1. user
2. member, librarian
3. book
4. book controller
5. book service
6. library
7. library manager(librarian)
8. reservation
9. fine
10. address

*/

enum Role {
    ADMIN,
    MEMBER
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

    public Member(String name, String userId, Location address) {
        super(name, userId, address, Role.MEMBER);
    }

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(LibraryCard libraryCard) {
        this.libraryCard = libraryCard;
    }
}

class LibraryCard {
    
    Date issueDate;
    Date expiryDate;
    Status cardStatus;
    String cardId;
    List<Reservation> booksReservedList = new ArrayList<>();
    
    public LibraryCard(Date issueDate) {
        cardId = "1234";
        this.issueDate = issueDate;
        this.expiryDate = issueDate + 30;
        this.cardStatus = Status.ACTIVE;
    }
    
    public Status getCardStatus() {
        return cardStatus;
    }
    
    public void updateCardStatus(Status cardStatus) {
        this.cardStatus = cardStatus;
    }
    
    public void extendExpiryDate() {
        this.expiryDate = new Date() + 30;
    }
    
    public void addBookReservation(Reservation reservation) {
        booksReservedList.add(reservation);
    }
    
    public List<Reservation> getBookReservation() {
        return booksReservedList;
    }
}

class Book {
    
    String ISBN;
    String author;
    String publisher;
    String title;
    Category bookCategory;
    BookStatus bookStatus;
    
    public Book(String ISBN, String author, String publisher, String title, Category bookCategory) {
        this.ISBN = ISBN;
        this.author = author;
        this.publisher = publisher;
        this.title = title;
        this.bookCategory = bookCategory;
        this.bookStatus = BookStatus.AVAILABLE;
    }
}

// this class is used to store and manage multiple copies of books
class BookController {
    
    List<Book> similarBookList = new ArrayList();
    
    public void addBook(Book book) {
        similarBookList.add(book);
    }
    
    public void removeBook(Book book) {
        similarBookList.remove(book);
    }
    
    public Book getBookByAuthor(String author) {
        for(Book book : similarBookList) {
            if(book.getAuthorName() == author && book.getBookStatus() == BookStatus.AVAILABLE) {
                return book;
            }
        }
        return null;
    }
    
    public Book getBookByTitle(String title) {
        for(Book book : similarBookList) {
            if(book.getTitleName() == title && book.getBookStatus() == BookStatus.AVAILABLE) {
                return book;
            }
        }
        return null;
    }
    
    public Book getBookByCategory(Category bookCategory) {
        for(Book book : similarBookList) {
            if(book.getBookCategory() == bookCategory && book.getBookStatus() == BookStatus.AVAILABLE) {
                return book;
            }
        }
        return null;
    }
}


class BookManager {
    
    List<BookController> allBooksList = new ArrayList<>();
    
    public addBookController(BookController bookController) {
        allBooksList.add(bookController);
    }
    
    public removeBookController(BookController bookController) {
        allBooksList.remove(bookController);
    }
    
    public List<Book> searchBook(String keyword, SearchBookService searchBookService) {
        
        return searchBookService.searchBook(bookController, keyword);
    }
}

abstract class SearchBookService {
    
    public abstract List<Book> searchBook(List<BookController>, String keyword);
}

class SearchBookByAuthor extends SearchBookService {
    
    List<Book> searchBook(List<BookController> bookController, String author) {
        List<Book> books = new ArrayList<>();
        for(BookController bookController : allBooksList) {
            Book book = bookController.getBookByAuthor(author);
            if(book != null) {
                books.add(book);
            } 
        }
        
        return books;
    }
}

class SearchBookByTitle extends SearchBookService {
    
    List<Book> searchBook(List<BookController> bookController, String title) {
        List<Book> books = new ArrayList<>();
        for(BookController bookController : allBooksList) {
            Book book = bookController.getBookByTitle(title);
            if(book != null) {
                books.add(book);
            } 
        }
        
        return books;
    }
}

class SearchBookByISBN extends SearchBookService {
    
    List<Book> searchBook(List<BookController> bookController, String ISBN) {
        List<Book> books = new ArrayList<>();
        for(BookController bookController : allBooksList) {
            Book book = bookController.getBookByISBN(ISBN);
            if(book != null) {
                books.add(book);
            } 
        }
        
        return books;
    }
}

class Library {
    
    String libraryName;
    Location address;
    String description;
    BookManager bookManager;
    Librarian librarian;
    
    public Library(String libraryName, Location address, String description) {
        this.libraryName = libraryName;
        this.address = address;
        this.description = description;
    }
    
    public void setBookManager(BookManager bookManager) {
        this.bookManager = bookManager;
    }
    
    public setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }
}

class LibraryManager {
    
    Library library;
    List<Member> membersList = new ArrayList<>();
    List<Reservation> reservationsList = new ArrayList<>();
    
    public void setLibrary(Library library) {
        this.library = library;
    }
    
    public void registerMember(Member member) {
        LibraryCard libraryCard = new LibraryCard(new Date());
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
    
    public void renewMemberShip(Member member) {
        Member member = getMember(memberId);
        
        if(member !== null) {
            member.extendExpiryDate();
        }
    }
    
    public List<Book> searchBook(String keyword, SearchBookService searchBookService) {
        return library.getBookManager().searchBook(keyword, searchBookService);
    }
    
    public void issueBook(String memberId, String bookISBN) {
        Book book = library.getBookManager().searchBook(bookISBN, new SearchBookByISBN()).get(0);
        Member member = getMember(memberId);
        
        boolean isValid = validateBookAllotment(member, book);
        
        if(isValid == false) {
            System.out.println("book cannot be issued");
            return;
        }
        
        Reservation reservation = new Reservation();
        reservation.createReservation(memberId, book);
        
    }
    
    public void returnBook(String reservationId) {
        Reservation reservation = getReservation(reservationId);
        
        boolean isValid = validateBookDeallotment(reservation.member, reservation.book);
        
        if(isValid == false) {
            System.out.println("book cannot be returned");
            return;
        }
        
        reservation.cancelReservation();
        
    }
    
    public Member getMember(String memberId) {
        for (Member member : membersList) {
            if (member.userId.equals(memberId)) {
                return member;
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

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World");
	}
}
