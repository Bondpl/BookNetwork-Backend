package cz.cvut.fit.tjv.social_network.server.dto.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionIdDTO {

    @NotNull(message = "Uuid is required")
    private UUID uuid;

}
