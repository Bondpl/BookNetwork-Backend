package service;


import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionDTO;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionUpdateDTO;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Transaction;
import cz.cvut.fit.tjv.social_network.server.model.TransactionStatus;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.TransactionRepository;
import cz.cvut.fit.tjv.social_network.server.service.BookService;
import cz.cvut.fit.tjv.social_network.server.service.TransactionService;
import cz.cvut.fit.tjv.social_network.server.service.UserService;
import jdk.jfr.Name;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private TransactionService transactionService;

    private UUID borrowerUuid;
    private UUID lenderUuid;
    private UUID bookUuid;
    private UUID transactionUuid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        borrowerUuid = UUID.randomUUID();
        lenderUuid = UUID.randomUUID();
        bookUuid = UUID.randomUUID();
        transactionUuid = UUID.randomUUID();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Test PASSED: " + testInfo.getDisplayName());
    }

    @Test
    void createTransaction_success() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setBorrowerUuid(borrowerUuid);
        transactionDTO.setLenderUuid(lenderUuid);
        transactionDTO.setBookUuid(bookUuid);
        transactionDTO.setStatus(TransactionStatus.ONGOING);

        User borrower = new User();
        User lender = new User();
        Book book = new Book();

        when(userService.getUserById(borrowerUuid)).thenReturn(borrower);
        when(userService.getUserById(lenderUuid)).thenReturn(lender);
        when(bookService.getBookById(bookUuid)).thenReturn(book);
        when(transactionRepository.findByBookAndStatus(book, TransactionStatus.ONGOING)).thenReturn(Optional.empty());

        Transaction savedTransaction = new Transaction();
        savedTransaction.setBorrower(borrower);
        savedTransaction.setLender(lender);
        savedTransaction.setBook(book);
        savedTransaction.setStatus(TransactionStatus.ONGOING);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction transaction = transactionService.createTransaction(transactionDTO);

        assertNotNull(transaction);
        assertEquals(borrower, transaction.getBorrower());
        assertEquals(lender, transaction.getLender());
        assertEquals(book, transaction.getBook());
        assertEquals(TransactionStatus.ONGOING, transaction.getStatus());

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void createTransaction_bookAlreadyBorrowed_throwsException() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setBorrowerUuid(borrowerUuid);
        transactionDTO.setLenderUuid(lenderUuid);
        transactionDTO.setBookUuid(bookUuid);
        transactionDTO.setStatus(TransactionStatus.ONGOING);

        User borrower = new User();
        User lender = new User();
        Book book = new Book();

        when(userService.getUserById(borrowerUuid)).thenReturn(borrower);
        when(userService.getUserById(lenderUuid)).thenReturn(lender);
        when(bookService.getBookById(bookUuid)).thenReturn(book);
        when(transactionRepository.findByBookAndStatus(book, TransactionStatus.ONGOING))
                .thenReturn(Optional.of(new Transaction()));

        Exceptions.BookAlreadyBorrowedException exception = assertThrows(Exceptions.BookAlreadyBorrowedException.class, () ->
                transactionService.createTransaction(transactionDTO));

        assertEquals("Book is already borrowed.", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void updateTransactionStatus_success() {
        TransactionUpdateDTO transactionUpdateDTO = new TransactionUpdateDTO();
        transactionUpdateDTO.setUuid(transactionUuid);
        transactionUpdateDTO.setStatus(TransactionStatus.COMPLETED);

        Transaction existingTransaction = new Transaction();
        existingTransaction.setUuid(transactionUuid);
        existingTransaction.setStatus(TransactionStatus.ONGOING);

        when(transactionRepository.existsById(transactionUuid)).thenReturn(true);
        when(transactionRepository.findById(transactionUuid)).thenReturn(Optional.of(existingTransaction));

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setUuid(transactionUuid);
        updatedTransaction.setStatus(TransactionStatus.COMPLETED);

        when(transactionRepository.save(existingTransaction)).thenReturn(updatedTransaction);

        Transaction result = transactionService.updateTransactionStatus(transactionUpdateDTO);

        assertNotNull(result);
        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        verify(transactionRepository).existsById(transactionUuid);
        verify(transactionRepository).findById(transactionUuid);
        verify(transactionRepository).save(existingTransaction);
    }


    @Test
    void updateTransactionStatus_transactionNotFound_throwsException() {
        TransactionUpdateDTO transactionUpdateDTO = new TransactionUpdateDTO();
        transactionUpdateDTO.setUuid(transactionUuid);
        transactionUpdateDTO.setStatus(TransactionStatus.COMPLETED);

        when(transactionRepository.findById(transactionUuid)).thenReturn(Optional.empty());

        Exceptions.TransactionNotFoundException exception = assertThrows(Exceptions.TransactionNotFoundException.class, () ->
                transactionService.updateTransactionStatus(transactionUpdateDTO));

        assertEquals("Transaction not found.", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void removeTransaction_success() {
        doNothing().when(transactionRepository).deleteById(transactionUuid);

        transactionService.removeTransaction(transactionUuid);

        verify(transactionRepository).deleteById(transactionUuid);
    }

    @Test
    void getAllTransactions_success() {
        transactionService.getAllTransactions();

        verify(transactionRepository).findAll();
    }
}
