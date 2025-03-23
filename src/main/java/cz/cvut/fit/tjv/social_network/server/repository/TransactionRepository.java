package cz.cvut.fit.tjv.social_network.server.repository;

import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.Transaction;
import cz.cvut.fit.tjv.social_network.server.model.TransactionStatus;
import cz.cvut.fit.tjv.social_network.server.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByBook_Uuid(UUID bookId);

    Collection<Transaction> findAllByBookUuid(UUID uuid);

    Optional<Transaction> findByBookAndStatus(Book book, TransactionStatus transactionStatus);

    Optional<Transaction> findByBookAndLender(Book book, User lenderUser);

    Optional<Transaction> findByBookAndBorrower(Book book, User borrower);

    Optional<Transaction> findByBookAndBorrowerAndStatus(@NotNull(message = "Book is required") Book book, @NotNull(message = "Borrower is required") User borrower, TransactionStatus attr0);

    @Query("SELECT t.book FROM Transaction t WHERE t.borrower.email = :userEmail")
    Collection<Book> findBooksBorrowedByUser(@Param("userEmail") String userEmail);

    @Query("SELECT t.book FROM Transaction t WHERE t.lender.uuid = :userUuid")
    Collection<Book> findBooksLentByUser(@Param("userUuid") UUID userUuid);

    Collection<Transaction> findByBorrower_Email(@NotNull(message = "Email is required") String borrowerEmail);

    @Query("SELECT t FROM Transaction t WHERE t.book.owner.email = :userEmail AND t.status = cz.cvut.fit.tjv.social_network.server.model.TransactionStatus.ONGOING")
    Collection<Transaction> findBorrowedTransactionsOfBooksOwnedByUser(@Param("userEmail") String userEmail);


}
