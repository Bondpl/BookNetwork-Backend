package cz.cvut.fit.tjv.social_network.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid = UUID.randomUUID();

    @NotNull(message = "Borrower is required")
    @ManyToOne(fetch = FetchType.EAGER)
    private User borrower;

    @NotNull(message = "Lender is required")
    @ManyToOne(fetch = FetchType.EAGER)
    private User lender;

    @NotNull(message = "Book is required")
    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}

