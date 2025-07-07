package com.example.book_api.service;

import com.example.book_api.client.GoogleBooksClient;
import com.example.book_api.client.OpenLibraryClient;
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
    private final OpenLibraryClient openLibraryClient;
    private final GoogleBooksClient googleBooksClient;

    public BookService(BookRepository bookRepository,
                       OpenLibraryClient openLibraryClient,
                       GoogleBooksClient googleBooksClient) {
        this.bookRepository = bookRepository;
        this.openLibraryClient = openLibraryClient;
        this.googleBooksClient = googleBooksClient;


    }

    public BookEntity createBook(BookEntity book) {
        if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
            String key = "ISBN:" + book.getIsbn();

            // Obtener URL desde OpenLibrary
            try {
                Map<String, Object> olResponse = openLibraryClient.buscarLibro(
                        key, "json", "data");

                if (olResponse != null && !olResponse.isEmpty()) {
                    Map<String, Object> bookData = (Map<String, Object>) olResponse.get(key);

                    if (bookData != null && bookData.get("url") != null) {
                        String url = "https://openlibrary.org" + bookData.get("url").toString();
                        book.setUrl(url);
                    } else {
                        book.setUrl("URL no disponible");
                    }
                }
            } catch (Exception e) {
                book.setUrl("URL no disponible");
            }

            // Obtener categoría desde Google Books
            try {
                Map<String, Object> gbResponse = googleBooksClient.buscarPorIsbn("isbn:" + book.getIsbn());

                if (gbResponse != null && gbResponse.containsKey("items")) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) gbResponse.get("items");
                    if (!items.isEmpty()) {
                        Map<String, Object> volumeInfo = (Map<String, Object>) items.get(0).get("volumeInfo");
                        if (volumeInfo != null && volumeInfo.containsKey("categories")) {
                            List<String> categories = (List<String>) volumeInfo.get("categories");
                            if (!categories.isEmpty()) {
                                book.setCategory(categories.get(0));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // no hacer nada
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
            book.setCategory(updatedBook.getCategory()); 
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
        return bookRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(text, text);
    }
}
