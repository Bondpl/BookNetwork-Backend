package cz.cvut.fit.tjv.social_network.server.repository;

import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Transaction;
import cz.cvut.fit.tjv.social_network.server.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Collection<Transaction> findByBorrower_Uuid(UUID uuid);

    Collection<Transaction> findByBook_Uuid(UUID bookId);

    Optional<Transaction> findByBookAndStatus(Book book, TransactionStatus transactionStatus);
}
