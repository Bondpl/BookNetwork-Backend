package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionDTO;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionIdDTO;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionUpdateDTO;
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
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        try {
            Transaction createdTransaction = transactionService.createTransaction(transactionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody TransactionUpdateDTO transactionUpdateDTO) {
        try {
            Transaction updatedTransaction = transactionService.updateTransactionStatus(transactionUpdateDTO);
            return ResponseEntity.ok(updatedTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public void deleteTransaction(@Valid @RequestBody TransactionIdDTO uuid) {
        transactionService.removeTransaction(uuid.getUuid());
    }

    @GetMapping("/book")
    public Collection<Transaction> getTransactionsOfBook(@Valid @RequestBody TransactionIdDTO uuid) {
        return transactionService.getTransactionsOfBook(uuid.getUuid());
    }
}
