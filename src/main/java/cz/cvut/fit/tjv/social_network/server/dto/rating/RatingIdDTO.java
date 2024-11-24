package cz.cvut.fit.tjv.social_network.server.dto.rating;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RatingIdDTO {
    @NotNull(message = "Rating ID is required")
    private UUID uuid;
}
