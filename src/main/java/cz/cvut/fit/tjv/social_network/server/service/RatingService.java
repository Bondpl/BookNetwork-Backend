package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.dto.rating.RatingIdDTO;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Rating;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingService {
    private RatingRepository ratingRepository;

    public Rating createRating(Book book, User user, int rating) {
        Rating r = new Rating();

        if (rating < 0 || rating > 5) {
            throw new Exceptions.RatingNotInScopeException("Rating must be between 0 and 5");
        }

        if (ratingRepository.findByBookAndUser(book, user).isPresent()) {
            throw new Exceptions.RatingAlreadyExistsException("Rating already exists");
        }

        r.setBook(book);
        r.setUser(user);
        r.setRating(rating);
        return ratingRepository.save(r);
    }

    public Rating getRatingById(RatingIdDTO ratingIdDTO) {

        if (!ratingRepository.existsById(ratingIdDTO.getUuid())) {
            throw new Exceptions.RatingNotFoundException("Rating not found");
        }
        return ratingRepository.findById(ratingIdDTO.getUuid()).get();
    }

    public Rating updateRating(Rating rating) {
        UUID ratingId = rating.getUuid();
        if (!ratingRepository.existsById(ratingId)) {
            throw new Exceptions.RatingNotFoundException("Rating not found");
        }
        return ratingRepository.save(rating);
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public boolean deleteRatingById(UUID ratingId) {
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        if (rating.isPresent()) {
            ratingRepository.delete(rating.get());
            return true;
        }
        return false;
    }

    public List<Integer> getRatingsByBook(Book book) {
        return ratingRepository.findByBook(book).stream()
                .map(Rating::getRating)
                .collect(Collectors.toList());
    }

    public float getAverageRatingByBook(Book book) {
        List<Integer> ratings = getRatingsByBook(book);
        if (ratings.isEmpty()) {
            return 0;
        }
        return (float) ratings.stream().mapToInt(Integer::intValue).sum() / ratings.size();
    }
}
