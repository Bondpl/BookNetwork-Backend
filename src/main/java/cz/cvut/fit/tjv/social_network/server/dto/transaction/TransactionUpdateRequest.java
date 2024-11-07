package cz.cvut.fit.tjv.social_network.server.dto.transaction;

import cz.cvut.fit.tjv.social_network.server.model.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionUpdateRequest {

    @NotNull(message = "Uuid is required")
    private UUID uuid;
    
    @NotNull(message = "Status is required")
    private TransactionStatus status;
}
