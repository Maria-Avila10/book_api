package com.example.book_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private JdbcTemplate jdbc;

    private boolean isValidIsbn(String isbn) {
        try {
            String url = "https://openlibrary.org/isbn/" + isbn + ".json";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllBooks() {
        var books = jdbc.queryForList("SELECT * FROM book");
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookById(@PathVariable Long id) {
        var books = jdbc.queryForList("SELECT * FROM book WHERE id = ?", id);
        if (books.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(books.get(0));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBook(@RequestBody Map<String, Object> book) {
        String isbn = (String) book.get("isbn");
        if (isbn != null && !isValidIsbn(isbn)) {
            return ResponseEntity.badRequest().body(Map.of("error", "ISBN inválido"));
        }

        jdbc.update(
                "INSERT INTO book(title, author, isbn, published_year, url) VALUES(?, ?, ?, ?, ?)",
                book.get("title"),
                book.get("author"),
                isbn,
                book.get("published_year"),
                isbn != null ? "https://openlibrary.org/isbn/" + isbn + ".json" : null
        );

        Long id = jdbc.queryForObject("SELECT MAX(id) FROM book", Long.class);
        return getBookById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBook(@PathVariable Long id, @RequestBody Map<String, Object> book) {
        String isbn = (String) book.get("isbn");
        if (isbn != null && !isValidIsbn(isbn)) {
            return ResponseEntity.badRequest().body(Map.of("error", "ISBN inválido"));
        }

        int rows = jdbc.update(
                "UPDATE book SET title = ?, author = ?, isbn = ?, published_year = ?, url = ? WHERE id = ?",
                book.get("title"),
                book.get("author"),
                isbn,
                book.get("published_year"),
                isbn != null ? "https://openlibrary.org/isbn/" + isbn + ".json" : null,
                id
        );

        if (rows == 0) return ResponseEntity.notFound().build();
        return getBookById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        int rows = jdbc.update("DELETE FROM book WHERE id = ?", id);
        return rows > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
