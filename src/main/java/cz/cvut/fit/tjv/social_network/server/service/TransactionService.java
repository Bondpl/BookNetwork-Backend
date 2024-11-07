package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionRequest;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionUpdateRequest;
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

    public Transaction createTransaction(TransactionRequest transactionRequest) {


        User borrower = userService.getUserById(transactionRequest.getBorrowerUuid());
        User lender = userService.getUserById(transactionRequest.getLenderUuid());
        Book book = bookService.getBookById(transactionRequest.getBookUuid());

        if (borrower == null || lender == null || book == null) {
            throw new IllegalArgumentException("Invalid borrower, lender, or book UUID.");
        }

        Optional<Transaction> ongoingTransaction = transactionRepository.findByBookAndStatus(book, TransactionStatus.ONGOING);

        if (ongoingTransaction.isPresent()) {
            throw new IllegalArgumentException("Book is already borrowed.");
        }
        
        Transaction transaction = new Transaction();
        transaction.setBorrower(borrower);
        transaction.setLender(lender);
        transaction.setBook(book);
        transaction.setStatus(transactionRequest.getStatus());

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
        return transactionRepository.findByBook_Uuid(bookId);
    }

    public Transaction updateTransactionStatus(TransactionUpdateRequest transactionUpdateRequest) {

        if (transactionUpdateRequest.getUuid() == null) {
            throw new IllegalArgumentException("Transaction UUID is required.");
        }

        Transaction transaction = transactionRepository.findById(transactionUpdateRequest.getUuid())
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found."));

        transaction.setStatus(transactionUpdateRequest.getStatus());
        return transactionRepository.save(transaction);
    }
}
