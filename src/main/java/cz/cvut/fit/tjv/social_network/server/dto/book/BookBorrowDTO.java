package cz.cvut.fit.tjv.social_network.server.dto.book;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BookBorrowDTO {
    @NotNull(message = "book Uuid is required")
    private UUID bookId;
}
