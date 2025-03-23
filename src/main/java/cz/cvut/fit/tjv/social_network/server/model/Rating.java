package cz.cvut.fit.tjv.social_network.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "ratings")
@Setter
@Getter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @NotNull(message = "Book is required")
    @JsonBackReference
    private Book book;

    @ManyToOne
    @NotNull(message = "User is required")
    @JsonBackReference
    private User user;

    @NotNull(message = "Rating is required")
    private int rating;

}
