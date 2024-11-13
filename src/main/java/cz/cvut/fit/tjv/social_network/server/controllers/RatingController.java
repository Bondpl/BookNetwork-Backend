package cz.cvut.fit.tjv.social_network.server.controllers;

import cz.cvut.fit.tjv.social_network.server.dto.rating.RatingIdRequest;
import cz.cvut.fit.tjv.social_network.server.dto.rating.RatingRequest;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Rating;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.service.BookService;
import cz.cvut.fit.tjv.social_network.server.service.RatingService;
import cz.cvut.fit.tjv.social_network.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ratings")
@AllArgsConstructor
public class RatingController {
    private RatingService ratingService;
    private BookService bookService;
    private UserService userService;

    @GetMapping("/all")
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @PostMapping("/add")
    public ResponseEntity<?> createRating(@RequestBody RatingRequest ratingRequest) {
        Book book = bookService.getBookById(ratingRequest.getBookId());
        User user = userService.getUserById(ratingRequest.getUserId());

        if (book == null || user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid book or user ID");
        }

        Rating rating = ratingService.CreateRating(book, user, ratingRequest.getRating());
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRating(@RequestBody RatingIdRequest ratingIdRequest) {
        boolean deleted = ratingService.deleteRatingById(ratingIdRequest.getRatingId());

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating not found");
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Rating> getRatingById(@PathVariable UUID uuid) {
        try {
            return ResponseEntity.ok(ratingService.getRatingById(uuid));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/book/{bookId}")
    public List<Integer> getRatingsByBookId(@PathVariable UUID bookId) {
        Book book = bookService.getBookById(bookId);
        return ratingService.getRatingsByBook(book);
    }

    @GetMapping("/average/{bookId}")
    public double getAverageRatingByBookId(@PathVariable UUID bookId) {
        Book book = bookService.getBookById(bookId);
        return ratingService.getAverageRatingByBook(book);
    }

}
