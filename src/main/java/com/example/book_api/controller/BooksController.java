package com.example.book_api.controller;

import com.example.book_api.model.BookEntity;
import com.example.book_api.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // 👈 Directamente en el Controller

    public BooksController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Integer id) {
        Optional<BookEntity> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public BookEntity createBook(@RequestBody BookEntity book) {
        // 1. Consultar la API de OpenLibrary con el ISBN recibido
        if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
            String apiUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:" + book.getIsbn() + "&format=json&jscmd=data";

            try {
                Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

                if (response != null && !response.isEmpty()) {
                    String key = "ISBN:" + book.getIsbn();
                    Map<String, Object> bookData = (Map<String, Object>) response.get(key);

                    if (bookData != null && bookData.get("url") != null) {
                        String url = "https://openlibrary.org" + (String) bookData.get("url");
                        book.setUrl(url);
                    } else {
                        book.setUrl("URL no disponible");
                    }
                } else {
                    book.setUrl("URL no disponible");
                }
            } catch (Exception e) {
                book.setUrl("URL no disponible");
            }
        } else {
            book.setUrl("ISBN no proporcionado");
        }

        // 2. Guardar el libro en la base de datos
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookEntity> updateBook(@PathVariable Integer id, @RequestBody BookEntity updatedBook) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            BookEntity book = optionalBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setIsbn(updatedBook.getIsbn());
            book.setPublishedYear(updatedBook.getPublishedYear());
            book.setUrl(updatedBook.getUrl());

            return ResponseEntity.ok(bookRepository.save(book));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
