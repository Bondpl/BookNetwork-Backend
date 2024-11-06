package cz.cvut.fit.tjv.social_network.server.service;

import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Rating;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RatingService {
    private RatingRepository ratingRepository;

    public Rating SetRating(Book book, User user, int rating) {

        Rating r = new Rating();
        r.setBook(book);
        r.setUser(user);
        r.setRating(rating);
        return ratingRepository.save(r);
    }

    public Rating getRatingById(UUID uuid) {
        return ratingRepository.findById(uuid).orElse(null);
    }
    

    public Rating updateRating(Rating rating) {
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

}
