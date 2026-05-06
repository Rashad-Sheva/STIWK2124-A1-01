package arl.backend.service;

import arl.backend.entity.Book;
import arl.backend.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * Persist a new book entry.
     * Throws 409 CONFLICT if the ISBN is already registered.
     */
    public Book createBook(Book book) {
        if (book.getIsbn() != null && bookRepository.existsByIsbn(book.getIsbn())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "A book with ISBN '" + book.getIsbn() + "' already exists"
            );
        }
        return bookRepository.save(book);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    /**
     * Retrieve a single book by its ID.
     * Throws 404 NOT FOUND if no book matches.
     */
    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Book with ID " + id + " not found"
            ));
    }

    /**
     * Retrieve a single book by its ISBN.
     * Throws 404 NOT FOUND if no book matches.
     */
    @Transactional(readOnly = true)
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Book with ISBN '" + isbn + "' not found"
            ));
    }

    /**
     * Retrieve all books with pagination.
     * Optionally filter by genre or availability when those fields are provided.
     */
    @Transactional(readOnly = true)
    public Page<Book> getAllBooks(String genre, Boolean available, Pageable pageable) {
        if (genre != null && !genre.isBlank()) {
            return bookRepository.findByGenreIgnoreCase(genre, pageable);
        }
        if (available != null) {
            return bookRepository.findByAvailable(available, pageable);
        }
        return bookRepository.findAll(pageable);
    }

    /**
     * Search books by a keyword matched against title and author.
     * Returns an empty page (never throws) when nothing matches.
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return bookRepository.findAll(pageable);
        }
        return bookRepository.searchByKeyword(keyword.trim(), pageable);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    /**
     * Fully replace the fields of an existing book.
     * Throws 404 NOT FOUND if the book does not exist.
     * Throws 409 CONFLICT if the new ISBN belongs to a different book.
     */
    public Book updateBook(Long id, Book updated) {
        Book existing = getBookById(id);

        // ISBN uniqueness check: allow the same book to keep its own ISBN
        if (updated.getIsbn() != null
                && !updated.getIsbn().equals(existing.getIsbn())
                && bookRepository.existsByIsbn(updated.getIsbn())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "A book with ISBN '" + updated.getIsbn() + "' already exists"
            );
        }

        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setIsbn(updated.getIsbn());
        existing.setGenre(updated.getGenre());
        existing.setDescription(updated.getDescription());
        existing.setPublishedYear(updated.getPublishedYear());
        existing.setAvailable(updated.isAvailable());

        return bookRepository.save(existing);
    }

    /**
     * Toggle the availability status of a book.
     * Throws 404 NOT FOUND if the book does not exist.
     */
    public Book setAvailability(Long id, boolean available) {
        Book book = getBookById(id);
        book.setAvailable(available);
        return bookRepository.save(book);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    /**
     * Remove a book by its ID.
     * Throws 404 NOT FOUND if the book does not exist.
     */
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Book with ID " + id + " not found"
            );
        }
        bookRepository.deleteById(id);
    }
}