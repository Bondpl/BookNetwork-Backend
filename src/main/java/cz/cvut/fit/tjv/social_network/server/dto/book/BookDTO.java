package cz.cvut.fit.tjv.social_network.server.dto.book;

import cz.cvut.fit.tjv.social_network.server.model.BookStatus;
import cz.cvut.fit.tjv.social_network.server.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDTO {
    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    private String author;

    private String isbn;

    @NotNull(message = "Title is required")
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    private String coverUrl;

    @NotNull(message = "Owner is required")
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

}
