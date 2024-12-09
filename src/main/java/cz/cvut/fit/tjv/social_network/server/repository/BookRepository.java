package cz.cvut.fit.tjv.social_network.server.repository;

import cz.cvut.fit.tjv.social_network.server.model.Book;
import cz.cvut.fit.tjv.social_network.server.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Collection<Book> findBooksByBookStatus(BookStatus status);

    @Query("SELECT b FROM Book b WHERE b.owner.email = :userEmail")
    Collection<Book> findBooksOwnedByOwner(@Param("userEmail") String userEmail);

    @Query("SELECT b FROM Book b WHERE (SELECT AVG(r.rating) FROM Rating r WHERE r.book = b) > 4")
    List<Book> findBooksByRatingGreaterThan();


}
