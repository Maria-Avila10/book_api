package com.example.book_api.service;

import com.example.book_api.model.BookEntity;
import com.example.book_api.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookEntity createBook(BookEntity book) {
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
                }
            } catch (Exception e) {
                book.setUrl("URL no disponible");
            }
        } else {
            book.setUrl("ISBN no proporcionado");
        }

        return bookRepository.save(book);
    }

    public Optional<BookEntity> findBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public BookEntity updateBook(Integer id, BookEntity updatedBook) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            BookEntity book = optionalBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setIsbn(updatedBook.getIsbn());
            book.setPublishedYear(updatedBook.getPublishedYear());
            book.setUrl(updatedBook.getUrl());
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Libro no encontrado con ID: " + id);
        }
    }

    public boolean deleteBook(Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }

    public List<BookEntity> searchBooks(String text) {
        return bookRepository.findByTitleContainingIgnoreCase (text);
    }
}