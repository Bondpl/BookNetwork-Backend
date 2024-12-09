package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionDTO;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionIdDTO;
import cz.cvut.fit.tjv.social_network.server.dto.transaction.TransactionUpdateDTO;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Transaction;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.service.BookService;
import cz.cvut.fit.tjv.social_network.server.service.TransactionService;
import cz.cvut.fit.tjv.social_network.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.UUID;

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

    @PostMapping("/admin/create")
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

    @DeleteMapping("/admin/delete")
    public void deleteTransaction(@Valid @RequestBody TransactionIdDTO uuid) {
        transactionService.removeTransaction(uuid.getUuid());
    }

    @GetMapping("/book")
    public Collection<Transaction> getTransactionsOfBook() {
        User user = userService.getCurrentUser();

        return transactionService.findBorrowedTransactionsOfBooksOwnedByUser(user.getEmail());
    }

    @GetMapping("/transactions/{bookId}")
    public Collection<Transaction> getTransactionsOfBook(@PathVariable UUID bookId, HttpServletRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        // Assuming the user information is stored in the session
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session is not available");
        }

        UUID userUuid = (UUID) session.getAttribute("userUuid");
        if (userUuid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User UUID is not available in session");
        }

        return transactionService.getTransactionsOfBook(bookId);
    }

    @GetMapping("/user/borrowed")
    public Collection<Book> findBooksBorrowedByUser() {

        User user = userService.getCurrentUser();

        return transactionService.findBooksBorrowedByUser(user.getEmail());
    }

    @GetMapping("/user/lent")
    public Collection<Book> findBooksLentByUser() {
        User user = userService.getCurrentUser();

        return transactionService.findBooksLentByUser(user.getUuid());
    }
}
