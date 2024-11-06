package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.BookStatus;
import cz.cvut.fit.tjv.social_network.server.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private BookService bookService;

    @GetMapping
    public Collection<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/user/borrowed/{uuid}")
    public Collection<Book> getBooksBorrowedByUser(@PathVariable UUID uuid) {
        return bookService.getBooksBorrowedByUser(uuid);
    }

    @GetMapping("/user/owned/{uuid}")
    public Collection<Book> getBooksOwnedByUser(@PathVariable UUID uuid) {
        return bookService.getBooksOwnedByUser(uuid);
    }

    @GetMapping("/rating/{rating}")
    public Collection<Book> getBooksByRatingGreaterThan(@PathVariable int rating) {
        return bookService.findBooksByRatingGreaterThan(rating);
    }

    @GetMapping("/status/{status}")
    public Collection<Book> getBooksByStatus(@PathVariable BookStatus status) {
        return bookService.findBooksByStatus(status);
    }

    @PostMapping
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.createBook(book);
    }
}
