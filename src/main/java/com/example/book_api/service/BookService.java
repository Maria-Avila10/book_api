package com.example.book_api.service;

import com.example.book_api.enricher.BookEnricher;
import com.example.book_api.model.BookEntity;
import com.example.book_api.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final List<BookEnricher> enrichers;

    public BookService(BookRepository bookRepository, List<BookEnricher> enrichers) {
        this.bookRepository = bookRepository;
        this.enrichers = enrichers;
    }

    @Override
    public BookEntity createBook(BookEntity book) {
        for (BookEnricher enricher : enrichers) {
            enricher.enrich(book);
        }
        return bookRepository.save(book);
    }

    @Override
    public Optional<BookEntity> findBookById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public BookEntity updateBook(Integer id, BookEntity updatedBook) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            BookEntity book = optionalBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setIsbn(updatedBook.getIsbn());
            book.setPublishedYear(updatedBook.getPublishedYear());
            book.setUrl(updatedBook.getUrl());
            book.setCategory(updatedBook.getCategory());
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Libro no encontrado con ID: " + id);
        }
    }

    @Override
    public boolean deleteBook(Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<BookEntity> searchBooks(String text) {
        return bookRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(text, text);
    }
}
