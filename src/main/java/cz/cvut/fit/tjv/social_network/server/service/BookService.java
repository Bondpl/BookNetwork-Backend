package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.dto.book.BookBorrowDTO;
import cz.cvut.fit.tjv.social_network.server.dto.book.BookDTO;
import cz.cvut.fit.tjv.social_network.server.dto.book.BookIdDTO;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.*;
import cz.cvut.fit.tjv.social_network.server.repository.BookRepository;
import cz.cvut.fit.tjv.social_network.server.repository.TransactionRepository;
import cz.cvut.fit.tjv.social_network.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    private Book convertToBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setBookStatus(bookDTO.getBookStatus());
        book.setCoverUrl(bookDTO.getCoverUrl());

        User owner = userRepository.findById(bookDTO.getOwner().getUuid())
                .orElseThrow(() -> new Exceptions.OwnerNotFoundException("Owner not found"));
        book.setOwner(owner);

        return book;
    }


    public Book borrowBook(BookBorrowDTO bookBorrowDTO) {
        // Retrieve the currently authenticated user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Ensure user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exceptions.BookNotFoundException("User is not authenticated");
        }

        // Extract the current user (assumes the user is stored in the 'Principal' of the authentication object)
        User borrower = (User) authentication.getPrincipal(); // Assuming 'User' is your authenticated user class

        // Fetch the book from the repository
        Book book = bookRepository.findById(bookBorrowDTO.getBookId())
                .orElseThrow(() -> new Exceptions.BookNotFoundException("Book not found"));

        // Check if the book is already borrowed
        if (book.getBookStatus() == BookStatus.BORROWED) {
            throw new Exceptions.BookAlreadyBorrowedException("Book is already borrowed");
        }

        // Update the book status to BORROWED
        book.setBookStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        // Create a new transaction for borrowing
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setBorrower(borrower); // Set the authenticated user as the borrower

        // Fetch the lender (the book owner)
        User lender = userRepository.findById(book.getOwner().getUuid())
                .orElseThrow(() -> new Exceptions.LenderNotFoundException("Lender not found"));
        transaction.setLender(lender);

        // Set transaction status as ONGOING
        transaction.setStatus(TransactionStatus.ONGOING);

        // Save the transaction to the database
        transactionRepository.save(transaction);

        // Return the updated book
        return book;
    }


    public boolean hasBorrowerTransactionWithBook(UUID bookId, UUID borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new Exceptions.BookNotFoundException("Book not found"));
        User borrower = userRepository.findById(borrowerId)
                .orElseThrow(() -> new Exceptions.BorrowerNotFoundException("Borrower not found"));

        return transactionRepository.findByBookAndBorrower(book, borrower).isPresent();
    }

    public void returnBook(BookBorrowDTO bookBorrowDTO) {
        // Retrieve the currently authenticated user from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exceptions.BookNotFoundException("User is not authenticated");
        }

        User borrower = (User) authentication.getPrincipal(); // Assumes the authenticated user is stored in the Principal

        // Fetch the book from the repository
        Book book = bookRepository.findById(bookBorrowDTO.getBookId())
                .orElseThrow(() -> new Exceptions.BookNotFoundException("Book not found"));

        // Find the ongoing transaction for this book and authenticated borrower
        Transaction transaction = transactionRepository.findByBookAndBorrowerAndStatus(book, borrower, TransactionStatus.ONGOING)
                .orElseThrow(() -> new Exceptions.TransactionNotFoundException("No ongoing transaction found for this book and borrower"));

        // Mark the transaction as completed
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);

        // Mark the book as available
        book.setBookStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);
    }


    public Book createBook(BookDTO bookDTO) {
        if (bookDTO.getOwner() == null) {
            throw new Exceptions.OwnerRequiredException("Owner is required");
        }
        Book book = convertToBook(bookDTO);
        return bookRepository.save(book);
    }

    public void removeBook(BookIdDTO bookIdDTO) {
        Book book = bookRepository.findById(bookIdDTO.getUuid())
                .orElseThrow(() -> new Exceptions.BookNotFoundException("Book not found"));

        if (book.getBookStatus() == BookStatus.BORROWED) {
            throw new Exceptions.CantRemoveBorrowedBookException("Cannot remove a borrowed book");
        }

        bookRepository.deleteById(bookIdDTO.getUuid());
    }

    public Collection<Book> findBooksByStatus(BookStatus status) {
        return bookRepository.findBooksByBookStatus(status);
    }

    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Collection<Book> getBooksBorrowedByUser(UUID userUuid) {
        Collection<Transaction> transactions = transactionRepository.findByBorrower_Uuid(userUuid);
        return transactions.stream()
                .map(Transaction::getBook)
                .collect(Collectors.toList());
    }

    public Collection<Book> getBooksOwnedByUser(String userEmail) {
        return bookRepository.findBooksOwnedByOwner(userEmail);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public Collection<Book> findBooksByRatingGreaterThan(int minRating) {
        return bookRepository.findBooksByRatingGreaterThan(minRating);
    }

    public Book getBookById(UUID uuid) {
        if (bookRepository.findById(uuid).isEmpty()) {
            throw new Exceptions.BookNotFoundException("Book not found");
        }
        return bookRepository.findById(uuid).orElse(null);
    }
}
