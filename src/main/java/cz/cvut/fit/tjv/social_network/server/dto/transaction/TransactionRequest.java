package cz.cvut.fit.tjv.social_network.server.dto.transaction;

import cz.cvut.fit.tjv.social_network.server.model.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionRequest {

    @NotNull(message = "Borrower is required")
    private UUID borrowerUuid;

    @NotNull(message = "Lender is required")
    private UUID lenderUuid;

    @NotNull(message = "Book is required")
    private UUID bookUuid;

    @NotNull(message = "Status is required")
    private TransactionStatus status;
}

