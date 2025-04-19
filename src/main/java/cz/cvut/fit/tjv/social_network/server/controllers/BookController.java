package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.dto.book.BookBorrowDTO;
import cz.cvut.fit.tjv.social_network.server.dto.book.BookDTO;
import cz.cvut.fit.tjv.social_network.server.dto.book.BookIdDTO;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.BookStatus;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.service.BookService;
import cz.cvut.fit.tjv.social_network.server.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    @GetMapping
    public Collection<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/ratingGreater")
    public Collection<Book> getBooksByRatingGreaterThan4() {
        return bookService.findBooksByRatingGreaterThan4();
    }

    @GetMapping("/status/{status}")
    public Collection<Book> getBooksByStatus(@PathVariable BookStatus status) {
        return bookService.findBooksByStatus(status);
    }

    @GetMapping("/{uuid}")
    public Book getBookById(@PathVariable UUID uuid) {
        return bookService.getBookById(uuid);
    }

    @PostMapping("/admin/create")
    public Book createBook(@Valid @RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO);
    }

    @PostMapping("/add")
    public Book addBook(@Valid @RequestBody BookDTO bookDTO) {
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
    public Collection<Book> getBooksOwnedByOwner() {
        User user = userService.getCurrentUser();

        return bookService.getBooksOwnedByUser(user.getEmail());
    }

    @GetMapping("/borrowed")
    public Collection<Book> getBooksBorrowedByUser() {
        User user = userService.getCurrentUser();

        return bookService.getBooksBorrowedByUser(user.getEmail());
    }

}
