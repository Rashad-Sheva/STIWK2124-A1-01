package arl.backend.controller;

import arl.backend.model.Book;
import arl.backend.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Task Person 4: SEARCH & PAGINATION
    @GetMapping("/search")
    public Page<Book> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        
        // Kalau tak taip apa-apa kat search, dia tunjuk semua buku ikut page
        if (title == null || title.isEmpty()) {
            return bookRepository.findAll(PageRequest.of(page, size));
        }
        
        // Kalau user taip tajuk, dia cari tajuk tu + buat pagination
        return bookRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    // Task Person 4: VALIDATION
    @PostMapping
    public Book addBook(@Valid @RequestBody Book book) {
        // @Valid kat sini akan trigger syarat kat Book.java tadi
        return bookRepository.save(book);
    }
}