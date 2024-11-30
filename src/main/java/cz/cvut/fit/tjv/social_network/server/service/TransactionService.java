package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionDTO;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionUpdateDTO;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Transaction;
import cz.cvut.fit.tjv.social_network.server.model.TransactionStatus;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;
    private UserService userService;
    private BookService bookService;

    public Transaction createTransaction(TransactionDTO transactionDTO) {


        User borrower = userService.getUserById(transactionDTO.getBorrowerUuid());
        User lender = userService.getUserById(transactionDTO.getLenderUuid());
        Book book = bookService.getBookById(transactionDTO.getBookUuid());

        if (borrower == null) {
            throw new Exceptions.BorrowerNotFoundException("Invalid borrower UUID.");
        }
        if (lender == null) {
            throw new Exceptions.LenderNotFoundException("Invalid lender UUID.");
        }
        if (book == null) {
            throw new Exceptions.BookNotFoundException("Invalid book UUID.");
        }

        Optional<Transaction> ongoingTransaction = transactionRepository.findByBookAndStatus(book, TransactionStatus.ONGOING);

        if (ongoingTransaction.isPresent()) {
            throw new Exceptions.BookAlreadyBorrowedException("Book is already borrowed.");
        }

        Transaction transaction = new Transaction();
        transaction.setBorrower(borrower);
        transaction.setLender(lender);
        transaction.setBook(book);
        transaction.setStatus(transactionDTO.getStatus());

        return transactionRepository.save(transaction);
    }

    public void removeTransaction(UUID transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public Collection<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransaction(UUID transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Collection<Transaction> getTransactionsOfBook(UUID bookId) {
        return transactionRepository.findAllByBookUuid(bookId);
    }

    public Transaction updateTransactionStatus(TransactionUpdateDTO transactionUpdateDTO) {
        UUID transactionId = transactionUpdateDTO.getUuid();

        if (!transactionRepository.existsById(transactionId)) {
            throw new Exceptions.TransactionNotFoundException("Transaction not found.");
        }

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exceptions.TransactionNotFoundException("Transaction not found."));

        transaction.setStatus(transactionUpdateDTO.getStatus());
        return transactionRepository.save(transaction);
    }

    public Collection<Book> findBooksBorrowedByUser(UUID userUuid) {
        return transactionRepository.findBooksBorrowedByUser(userUuid);
    }

    public Collection<Book> findBooksLentByUser(UUID userUuid) {
        return transactionRepository.findBooksLentByUser(userUuid);
    }
}
