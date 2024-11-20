package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.dto.book.BookBorrowRequest;
import cz.cvut.fit.tjv.social_network.server.dto.book.BookRequest;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.*;
import cz.cvut.fit.tjv.social_network.server.repository.BookRepository;
import cz.cvut.fit.tjv.social_network.server.repository.TransactionRepository;
import cz.cvut.fit.tjv.social_network.server.repository.UserRepository;
import lombok.AllArgsConstructor;
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

    // Converts BookRequest DTO to Book entity
    private Book convertToBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setBookStatus(bookRequest.getBookStatus());
        book.setCoverUrl(bookRequest.getCoverUrl());

        // Fetch the owner from the database
        User owner = userRepository.findById(bookRequest.getOwner().getUuid())
                .orElseThrow(() -> new Exceptions.OwnerNotFoundException("Owner not found"));
        book.setOwner(owner);

        return book;
    }

    public Book borrowBook(BookBorrowRequest bookBorrowRequest) {
        Book book = bookRepository.findById(bookBorrowRequest.getBookId())
                .orElseThrow(() -> new Exceptions.BookNotFoundException("Book not found"));
        if (book.getBookStatus() == BookStatus.BORROWED) {
            throw new Exceptions.BookAlreadyBorrowedException("Book is already borrowed");
        }

        book.setBookStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        User borrower = userRepository.findById(bookBorrowRequest.getBorrower())
                .orElseThrow(() -> new Exceptions.BorrowerNotFoundException("Borrower not found"));
        transaction.setBorrower(borrower);

        User lender = userRepository.findById(book.getOwner().getUuid())
                .orElseThrow(() -> new Exceptions.LenderNotFoundException("Lender not found"));
        transaction.setLender(lender);

        transaction.setStatus(TransactionStatus.ONGOING);
        transactionRepository.save(transaction);

        return book;
    }

    public Book returnBook(UUID bookUuid) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new Exceptions.BookNotFoundException("Book not found"));
        if (book.getBookStatus() == BookStatus.AVAILABLE) {
            throw new Exceptions.BookAlreadyReturnedException("Book is already returned");
        }

        book.setBookStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        Transaction transaction = transactionRepository.findByBook_Uuid(bookUuid)
                .orElseThrow(() -> new Exceptions.TransactionNotFoundException("Transaction not found"));
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);

        return book;
    }

    public Book createBook(BookRequest bookRequest) {
        if (bookRequest.getOwner() == null) {
            throw new Exceptions.OwnerRequiredException("Owner is required");
        }
        Book book = convertToBook(bookRequest);
        return bookRepository.save(book);
    }

    public void removeBook(UUID bookUuid) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new Exceptions.BookNotFoundException("Book not found"));

        if (book.getBookStatus() == BookStatus.BORROWED) {
            throw new Exceptions.CantRemoveBorrowedBookException("Cannot remove a borrowed book");
        }

        bookRepository.deleteById(bookUuid);
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

    public Collection<Book> getBooksOwnedByUser(UUID userUuid) {
        return bookRepository.findBooksOwnedByOwner(userUuid);
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
