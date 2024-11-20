package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionIdRequest;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionRequest;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionUpdateRequest;
import cz.cvut.fit.tjv.social_network.server.model.Transaction;
import cz.cvut.fit.tjv.social_network.server.service.BookService;
import cz.cvut.fit.tjv.social_network.server.service.TransactionService;
import cz.cvut.fit.tjv.social_network.server.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;
    private UserService userService;
    private BookService bookService;

    @GetMapping
    public Collection<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        try {
            Transaction createdTransaction = transactionService.createTransaction(transactionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody TransactionUpdateRequest transactionUpdateRequest) {
        try {
            Transaction updatedTransaction = transactionService.updateTransactionStatus(transactionUpdateRequest);
            return ResponseEntity.ok(updatedTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{uuid}")
    public void deleteTransaction(@Valid @RequestBody TransactionIdRequest uuid) {
        transactionService.removeTransaction(uuid.getUuid());
    }

    @GetMapping("/book")
    public Collection<Transaction> getTransactionsOfBook(@Valid @RequestBody TransactionIdRequest uuid) {
        return transactionService.getTransactionsOfBook(uuid.getUuid());
    }
}
