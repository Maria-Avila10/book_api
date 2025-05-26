package com.example.book_api.controller;

import com.example.book_api.api.BooksApi;
import com.example.book_api.model.Book;
import com.example.book_api.model.BookEntity;
import com.example.book_api.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BooksController implements BooksApi {

    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    private Book toDto(BookEntity entity) {
        Book dto = new Book();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setIsbn(entity.getIsbn());
        return dto;
    }

    private BookEntity toEntity(Book dto) {
        BookEntity entity = new BookEntity();
        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setIsbn(dto.getIsbn());
        return entity;
    }

    @Override
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAllBooks()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(books);
    }

    public ResponseEntity<Book> getBookById(Integer id) {
        return bookService.findBookById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Override
    public ResponseEntity<List<Book>> getBooksearchText(String texto) {
        List<Book> results = bookService.searchBooks(texto)
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(results);
    }


    @Override
    public ResponseEntity<Book> createBook(Book bookDto) {
        BookEntity entity = toEntity(bookDto);
        BookEntity saved = bookService.createBook(entity);
        return ResponseEntity.ok(toDto(saved));
    }


    @Override
    public ResponseEntity<Book> updateBook(Integer id, Book bookDto) {
        BookEntity entity = toEntity(bookDto);
        try {
            BookEntity updated = bookService.updateBook(id, entity);
            return ResponseEntity.ok(toDto(updated));
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
