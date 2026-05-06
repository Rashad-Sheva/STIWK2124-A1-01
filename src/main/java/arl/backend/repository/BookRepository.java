package arl.backend.repository;

import arl.backend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Full-text keyword search across title and author (case-insensitive),
     * with pagination support.
     *
     * Example: GET /books/search?keyword=tolkien&page=0&size=10
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title)  LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Filter books by genre (case-insensitive), with pagination support.
     *
     * Example: GET /books?genre=Fantasy&page=0&size=10
     */
    Page<Book> findByGenreIgnoreCase(String genre, Pageable pageable);

    /**
     * Look up a book by its ISBN.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Check whether an ISBN is already registered (used during create/update
     * to enforce uniqueness at the service layer before hitting the DB constraint).
     */
    boolean existsByIsbn(String isbn);

    /**
     * Filter by availability, with pagination support.
     *
     * Example: GET /books?available=true
     */
    Page<Book> findByAvailable(boolean available, Pageable pageable);
}