package com.example.book_api.controller;

import com.example.book_api.api.BooksApi;
import com.example.book_api.mapper.BookMapper;
import com.example.book_api.model.Book;
import com.example.book_api.model.BookEntity;
import com.example.book_api.model.BookRequest;
import com.example.book_api.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BooksController implements BooksApi {

    private final BookService bookService;
    private final BookMapper mapper;

    public BooksController(BookService bookService, BookMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }


    @Override
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAllBooks()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(books);
    }

    @Override
    public ResponseEntity<Book> getBookById(Integer id) {
        return bookService.findBookById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Book>> getBooksearchText(String texto) {
        List<Book> results = bookService.searchBooks(texto)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequest Request) {
        BookEntity entity = mapper.toEntity(Request);
        BookEntity saved = bookService.createBook(entity);
        return new ResponseEntity<>(mapper.toDto(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Book> updateBook(Integer id, @Valid @RequestBody BookRequest bookRequest) {
        try {
            BookEntity entity = mapper.toEntity(bookRequest);
            BookEntity updated = bookService.updateBook(id, entity);
            return ResponseEntity.ok(mapper.toDto(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteBook(Integer id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}