package cz.cvut.fit.tjv.social_network.server.dto.rating;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RatingRequest {

    @NotNull(message = "Book ID is required")
    private UUID bookId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Rating is required")
    private int rating;

}
