package service;


import cz.cvut.fit.tjv.social_network.server.dto.rating.RatingIdDTO;
import cz.cvut.fit.tjv.social_network.server.exceptions.Exceptions;
import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Rating;
import cz.cvut.fit.tjv.social_network.server.model.User;
import cz.cvut.fit.tjv.social_network.server.repository.RatingRepository;
import cz.cvut.fit.tjv.social_network.server.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRating() {
        Book book = new Book();
        User user = new User();
        int ratingValue = 5;

        Rating rating = new Rating();
        rating.setBook(book);
        rating.setUser(user);
        rating.setRating(ratingValue);

        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating savedRating = ratingService.CreateRating(book, user, ratingValue);

        assertNotNull(savedRating);
        assertEquals(book, savedRating.getBook());
        assertEquals(user, savedRating.getUser());
        assertEquals(ratingValue, savedRating.getRating());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testGetRatingById() {

        RatingIdDTO ratingIdDTO = new RatingIdDTO();
        ratingIdDTO.setUuid(UUID.randomUUID());
        UUID ratingId = ratingIdDTO.getUuid();
        Rating rating = new Rating();
        rating.setUuid(ratingId);

        when(ratingRepository.existsById(ratingId)).thenReturn(true);
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));


        Rating foundRating = ratingService.getRatingById(ratingIdDTO);

        assertNotNull(foundRating);
        assertEquals(ratingId, foundRating.getUuid());
        verify(ratingRepository, times(1)).existsById(ratingId);
        verify(ratingRepository, times(1)).findById(ratingId);
    }

    @Test
    void testGetRatingById_NotFound() {
        RatingIdDTO ratingIdDTO = new RatingIdDTO();
        ratingIdDTO.setUuid(UUID.randomUUID());
        when(ratingRepository.existsById(ratingIdDTO.getUuid())).thenReturn(false);

        assertThrows(Exceptions.RatingNotFoundException.class, () -> {
            ratingService.getRatingById(ratingIdDTO);
        });
        verify(ratingRepository, times(1)).existsById(ratingIdDTO.getUuid());
    }

    @Test
    void testUpdateRating() {
        UUID ratingId = UUID.randomUUID();
        Rating rating = new Rating();
        rating.setUuid(ratingId);
        rating.setRating(4);

        when(ratingRepository.existsById(ratingId)).thenReturn(true);
        when(ratingRepository.save(rating)).thenReturn(rating);


        Rating updatedRating = ratingService.updateRating(rating);

        assertNotNull(updatedRating);
        assertEquals(4, updatedRating.getRating());
        verify(ratingRepository, times(1)).existsById(ratingId);
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    void testGetAllRatings() {
        Rating rating1 = new Rating();
        Rating rating2 = new Rating();
        List<Rating> ratings = List.of(rating1, rating2);

        when(ratingRepository.findAll()).thenReturn(ratings);

        List<Rating> allRatings = ratingService.getAllRatings();

        assertEquals(2, allRatings.size());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void testDeleteRatingById_Success() {
        UUID ratingId = UUID.randomUUID();
        Rating rating = new Rating();
        rating.setUuid(ratingId);

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        boolean result = ratingService.deleteRatingById(ratingId);

        assertTrue(result);
        verify(ratingRepository, times(1)).findById(ratingId);
        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    void testDeleteRatingById_NotFound() {
        UUID ratingId = UUID.randomUUID();

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        boolean result = ratingService.deleteRatingById(ratingId);

        assertFalse(result);
        verify(ratingRepository, times(1)).findById(ratingId);
        verify(ratingRepository, times(0)).delete(any(Rating.class));
    }
}
