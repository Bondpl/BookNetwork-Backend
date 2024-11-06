package cz.cvut.fit.tjv.social_network.server.service;

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

    public Book borrowBook(UUID bookUuid, UUID userUuid) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getBookStatus() == BookStatus.BORROWED) {
            throw new RuntimeException("Book is already borrowed");
        }

        book.setBookStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        User borrower = userRepository.findById(userUuid)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        transaction.setBorrower(borrower);

        User lender = userRepository.findById(book.getOwner().getUuid())
                .orElseThrow(() -> new RuntimeException("Lender not found"));
        transaction.setLender(lender);


        transaction.setStatus(TransactionStatus.ONGOING);
        transactionRepository.save(transaction);

        return book;
    }

    public Book createBook(Book book) {
        if (book.getOwner() == null) {
            throw new RuntimeException("Owner is required");
        }
        return bookRepository.save(book);
    }

    public void removeBook(UUID bookUuid) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getBookStatus() == BookStatus.BORROWED) {
            throw new RuntimeException("Cannot remove a borrowed book");
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
        return bookRepository.findById(uuid).orElse(null);
    }
}
