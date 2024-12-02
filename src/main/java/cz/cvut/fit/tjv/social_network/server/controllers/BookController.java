package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.dto.book.BookBorrowDTO;
import cz.cvut.fit.tjv.social_network.server.dto.book.BookDTO;
import cz.cvut.fit.tjv.social_network.server.dto.book.BookIdDTO;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.BookStatus;
import cz.cvut.fit.tjv.social_network.server.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Collection<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/ratingGreater")
    public Collection<Book> getBooksByRatingGreaterThan(HttpServletRequest request) {
        // Retrieve the user's authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Retrieve the user's UUID from the session
        String userEmail = authentication.getName();

        // Ensure the user is authenticated
        if (userEmail == null) {
            throw new IllegalStateException("User is not authenticated.");
        }
        return bookService.findBooksByRatingGreaterThan();
    }

    @GetMapping("/status/{status}")
    public Collection<Book> getBooksByStatus(@PathVariable BookStatus status) {
        return bookService.findBooksByStatus(status);
    }

    @GetMapping("/{uuid}")
    public Book getBookById(@PathVariable UUID uuid) {
        return bookService.getBookById(uuid);
    }

    @PostMapping
    public Book createBook(@Valid @RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO);
    }

    @DeleteMapping
    public void deleteBook(@Valid @RequestBody BookIdDTO bookIdDTO) {
        bookService.removeBook(bookIdDTO);
    }

    @PostMapping("/borrow")
    public Book borrowBook(@Valid @RequestBody BookBorrowDTO bookBorrowDTO) {
        return bookService.borrowBook(bookBorrowDTO);
    }

    @PostMapping("/return")
    public void returnBook(@Valid @RequestBody BookBorrowDTO bookBorrowDTO) {
        bookService.returnBook(bookBorrowDTO);
    }

    @GetMapping("/owned")
    public Collection<Book> getBooksOwnedByOwner(HttpServletRequest request) {
        // Retrieve the user's authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Retrieve the user's UUID from the session
        String userEmail = authentication.getName();

        // Ensure the user is authenticated
        if (userEmail == null) {
            throw new IllegalStateException("User is not authenticated.");
        }

        // Fetch owned books for the user
        return bookService.getBooksOwnedByUser(userEmail);
    }

    @GetMapping("/borrowed")
    public Collection<Book> getBooksBorrowedByUser(HttpServletRequest request) {
        // Retrieve the user's authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Retrieve the user's UUID from the session
        String userEmail = authentication.getName();

        // Ensure the user is authenticated
        if (userEmail == null) {
            throw new IllegalStateException("User is not authenticated.");
        }

        // Fetch borrowed books for the user
        return bookService.getBooksBorrowedByUser(userEmail);
    }

}
