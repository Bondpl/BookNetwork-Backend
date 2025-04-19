//package service;
//
//import cz.cvut.fit.tjv.social_network.server.dto.book.BookBorrowDTO;
//import cz.cvut.fit.tjv.social_network.server.dto.book.BookDTO;
//import cz.cvut.fit.tjv.social_network.server.dto.book.BookIdDTO;
//import cz.cvut.fit.tjv.social_network.server.model.Book;
//import cz.cvut.fit.tjv.social_network.server.model.BookStatus;
//import cz.cvut.fit.tjv.social_network.server.model.Transaction;
//import cz.cvut.fit.tjv.social_network.server.model.User;
//import cz.cvut.fit.tjv.social_network.server.repository.BookRepository;
//import cz.cvut.fit.tjv.social_network.server.repository.TransactionRepository;
//import cz.cvut.fit.tjv.social_network.server.repository.UserRepository;
//import cz.cvut.fit.tjv.social_network.server.service.BookService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BookServiceTest {
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @InjectMocks
//    private BookService bookService;
//
//    private UUID bookUuid;
//    private UUID userUuid;
//    private Book book;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        bookUuid = UUID.randomUUID();
//        userUuid = UUID.randomUUID();
//
//        book = new Book();
//        book.setUuid(bookUuid);
//        book.setBookStatus(BookStatus.AVAILABLE);
//
//        user = new User();
//        user.setUuid(userUuid);
//
//        book.setOwner(user);
//    }
//
//    @Test
//    void borrowBook_Success() {
//
//        BookBorrowDTO bookBorrowDTO = new BookBorrowDTO();
//        bookBorrowDTO.setBookId(bookUuid);
//        bookBorrowDTO.setBorrower(userUuid);
//        when(bookRepository.findById(bookUuid)).thenReturn(Optional.of(book));
//        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));
//        when(userRepository.findById(book.getOwner().getUuid())).thenReturn(Optional.of(user));  // Mock owner lookup
//
//
//        Book result = bookService.borrowBook(bookBorrowDTO);
//
//        assertEquals(BookStatus.BORROWED, result.getBookStatus());
//        verify(transactionRepository, times(1)).save(any(Transaction.class));
//        verify(bookRepository, times(1)).save(book);
//    }
//
//    @Test
//    void borrowBook_BookAlreadyBorrowed() {
//        BookBorrowDTO bookBorrowDTO = new BookBorrowDTO();
//        bookBorrowDTO.setBookId(bookUuid);
//        bookBorrowDTO.setBorrower(userUuid);
//        book.setBookStatus(BookStatus.BORROWED);
//        when(bookRepository.findById(bookUuid)).thenReturn(Optional.of(book));
//
//        try {
//            bookService.borrowBook(bookBorrowDTO);
//        } catch (RuntimeException e) {
//            assertEquals("Book is already borrowed", e.getMessage());
//        }
//    }
//
//    @Test
//    void borrowBook_BookNotFound() {
//        BookBorrowDTO bookBorrowDTO = new BookBorrowDTO();
//        bookBorrowDTO.setBookId(bookUuid);
//        bookBorrowDTO.setBorrower(userUuid);
//        when(bookRepository.findById(bookUuid)).thenReturn(Optional.empty());
//
//        try {
//            bookService.borrowBook(bookBorrowDTO);
//        } catch (RuntimeException e) {
//            assertEquals("Book not found", e.getMessage());
//        }
//    }
//
//    @Test
//    void borrowBook_BorrowerNotFound() {
//        BookBorrowDTO bookBorrowDTO = new BookBorrowDTO();
//        bookBorrowDTO.setBookId(bookUuid);
//        bookBorrowDTO.setBorrower(userUuid);
//        when(bookRepository.findById(bookUuid)).thenReturn(Optional.of(book));
//        when(userRepository.findById(userUuid)).thenReturn(Optional.empty());
//        when(userRepository.findById(book.getOwner().getUuid())).thenReturn(Optional.of(user));
//
//        try {
//            bookService.borrowBook(bookBorrowDTO);
//        } catch (RuntimeException e) {
//            assertEquals("Borrower not found", e.getMessage());
//        }
//    }
//
//    @Test
//    void createBook_Success() {
//        BookDTO bookDTO = new BookDTO();
//        bookDTO.setTitle("Title");
//        bookDTO.setAuthor("Author");
//        bookDTO.setBookStatus(BookStatus.AVAILABLE);
//        bookDTO.setOwner(user);
//
//        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));
//        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Book result = bookService.createBook(bookDTO);
//
//        assertEquals("Title", result.getTitle());
//        assertEquals("Author", result.getAuthor());
//        assertEquals(BookStatus.AVAILABLE, result.getBookStatus());
//        assertEquals(user, result.getOwner());
//        verify(bookRepository, times(1)).save(any(Book.class));
//    }
//
//    @Test
//    void createBook_NoOwner() {
//        BookDTO bookDTO = new BookDTO();
//        bookDTO.setTitle("Title");
//        bookDTO.setAuthor("Author");
//        bookDTO.setBookStatus(BookStatus.AVAILABLE);
//
//
//        assertThrows(RuntimeException.class, () -> bookService.createBook(bookDTO));
//    }
//
//    @Test
//    void removeBook_BookAvailable() {
//        BookIdDTO bookIdDTO = new BookIdDTO();
//        bookIdDTO.setUuid(bookUuid);
//        when(bookRepository.findById(bookUuid)).thenReturn(Optional.of(book));
//
//        bookService.removeBook(bookIdDTO);
//
//        verify(bookRepository, times(1)).deleteById(bookUuid);
//    }
//
//    @Test
//    void removeBook_BookLent() {
//        BookIdDTO bookIdDTO = new BookIdDTO();
//        bookIdDTO.setUuid(bookUuid);
//
//        book.setBookStatus(BookStatus.BORROWED);
//        when(bookRepository.findById(bookUuid)).thenReturn(Optional.of(book));
//
//        RuntimeException thrown = assertThrows(
//                RuntimeException.class,
//                () -> bookService.removeBook(bookIdDTO)
//        );
//
//        assertEquals("Cannot remove a borrowed book", thrown.getMessage());
//        verify(bookRepository, never()).deleteById(bookUuid);
//    }
//
//    @Test
//    void removeBook_BookNotFound() {
//        UUID nonExistentBookUuid = UUID.randomUUID();
//        BookIdDTO bookIdDTO = new BookIdDTO();
//        bookIdDTO.setUuid(nonExistentBookUuid);
//
//        RuntimeException thrown = assertThrows(
//                RuntimeException.class,
//                () -> bookService.removeBook(bookIdDTO)
//        );
//
//        assertEquals("Book not found", thrown.getMessage());
//        verify(bookRepository, never()).deleteById(nonExistentBookUuid);
//    }
//
//    @Test
//    void findBooksByStatus_twoBooks() {
//        BookStatus status = BookStatus.AVAILABLE;
//
//        Book book2 = new Book();
//        book2.setUuid(UUID.randomUUID());
//        book2.setBookStatus(status);
//
//        when(bookRepository.findBooksByBookStatus(status)).thenReturn(List.of(book, book2));
//
//        Collection<Book> result = bookService.findBooksByStatus(status);
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(book));
//        assertTrue(result.contains(book2));
//
//        verify(bookRepository, times(1)).findBooksByBookStatus(status);
//    }
//
//    @Test
//    void findBooksByStatus_NoBooks() {
//        BookStatus status = BookStatus.AVAILABLE;
//
//        when(bookRepository.findBooksByBookStatus(status)).thenReturn(List.of());
//
//        Collection<Book> result = bookService.findBooksByStatus(status);
//
//        assertEquals(0, result.size());
//
//        verify(bookRepository, times(1)).findBooksByBookStatus(status);
//    }
//
//    @Test
//    void getAllBooks_threeBooks() {
//        Book book2 = new Book();
//        Book book3 = new Book();
//        book2.setUuid(UUID.randomUUID());
//        book3.setUuid(UUID.randomUUID());
//
//        when(bookRepository.findAll()).thenReturn(List.of(book, book2, book3));
//
//        Collection<Book> result = bookService.getAllBooks();
//
//        assertEquals(3, result.size());
//        assertTrue(result.contains(book));
//        assertTrue(result.contains(book2));
//        assertTrue(result.contains(book3));
//
//        verify(bookRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getAllBooks_NoBooks() {
//        when(bookRepository.findAll()).thenReturn(List.of());
//
//        Collection<Book> result = bookService.getAllBooks();
//
//        assertEquals(0, result.size());
//
//        verify(bookRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getBooksBorrowedByUser_twoBooks() {
//        Book book2 = new Book();
//        book2.setUuid(UUID.randomUUID());
//        book2.setOwner(user);
//
//        Transaction transaction1 = new Transaction();
//        transaction1.setBook(book);
//        transaction1.setBorrower(user);
//
//        Transaction transaction2 = new Transaction();
//        transaction2.setBook(book2);
//        transaction2.setBorrower(user);
//
//        when(transactionRepository.findByBorrower_Uuid(userUuid)).thenReturn(List.of(transaction1, transaction2));
//
//        Collection<Book> result = bookService.getBooksBorrowedByUser(userUuid);
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(book));
//        assertTrue(result.contains(book2));
//
//        verify(transactionRepository, times(1)).findByBorrower_Uuid(userUuid);
//    }
//
//    @Test
//    void GetBooksOwnedByUser() {
//
//        Book book1 = new Book();
//        book1.setUuid(bookUuid);
//        book1.setBookStatus(BookStatus.BORROWED);
//        book1.setOwner(user);
//
//        when(bookRepository.findBooksOwnedByOwner(userUuid)).thenReturn(Arrays.asList(book, book1));
//
//        Collection<Book> books = bookService.getBooksOwnedByUser(userUuid);
//        assertEquals(2, books.size());
//    }
//
//
//    @Test
//    void testUpdateBook() {
//        when(bookRepository.save(any(Book.class))).thenReturn(book);
//
//        book.setTitle("Updated Title");
//        book.setAuthor("Updated Author");
//        book.setBookStatus(BookStatus.BORROWED);
//        Book updatedBook = bookService.updateBook(book);
//
//        verify(bookRepository).save(book);
//
//        assertEquals("Updated Title", updatedBook.getTitle());
//        assertEquals("Updated Author", updatedBook.getAuthor());
//        assertEquals(BookStatus.BORROWED, updatedBook.getBookStatus());
//    }
//}
