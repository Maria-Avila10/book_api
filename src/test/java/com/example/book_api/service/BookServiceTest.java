package com.example.book_api.service;

import com.example.book_api.enricher.BookEnricher;
import com.example.book_api.model.BookEntity;
import com.example.book_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookEnricher enricher;

    @InjectMocks
    private BookService bookService;

    private BookEntity book;

    @BeforeEach
    void setUp() {
        book = new BookEntity();
        book.setId(1);
        book.setTitle("El Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setIsbn("9780261102217");
        book.setPublishedYear(1937);
        book.setUrl("https://openlibrary.org");
        book.setCategory("Fiction");

        // Reinicializo el service con lista de enrichers
        bookService = new BookService(bookRepository, List.of(enricher));
    }

    @Test
    void createBook_shouldEnrichAndSave() {
        when(bookRepository.save(book)).thenReturn(book);

        BookEntity result = bookService.createBook(book);

        verify(enricher, times(1)).enrich(book);
        verify(bookRepository).save(book);
        assertEquals("El Hobbit", result.getTitle());
    }

    @Test
    void findBookById_found() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Optional<BookEntity> result = bookService.findBookById(1);

        assertTrue(result.isPresent());
        assertEquals("El Hobbit", result.get().getTitle());
    }

    @Test
    void findBookById_notFound() {
        when(bookRepository.findById(99)).thenReturn(Optional.empty());

        Optional<BookEntity> result = bookService.findBookById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void updateBook_success() {
        BookEntity updated = new BookEntity();
        updated.setTitle("The Hobbit");
        updated.setAuthor("Tolkien");
        updated.setIsbn("9780261102217");
        updated.setPublishedYear(1937);
        updated.setUrl("https://openlibrary.org/book");
        updated.setCategory("Fantasy");

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(BookEntity.class))).thenReturn(updated);

        BookEntity result = bookService.updateBook(1, updated);

        assertEquals("The Hobbit", result.getTitle());
        assertEquals("Fantasy", result.getCategory());
    }

    @Test
    void updateBook_notFound_shouldThrow() {
        when(bookRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.updateBook(99, book);
        });

        assertEquals("Libro no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    void deleteBook_exists_shouldDelete() {
        when(bookRepository.existsById(1)).thenReturn(true);

        boolean result = bookService.deleteBook(1);

        verify(bookRepository).deleteById(1);
        assertTrue(result);
    }

    @Test
    void deleteBook_notExists_shouldReturnFalse() {
        when(bookRepository.existsById(99)).thenReturn(false);

        boolean result = bookService.deleteBook(99);

        assertFalse(result);
        verify(bookRepository, never()).deleteById(99);
    }

    @Test
    void findAllBooks_shouldReturnList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookEntity> result = bookService.findAllBooks();

        assertEquals(1, result.size());
    }

    @Test
    void searchBooks_shouldReturnMatchingResults() {
        when(bookRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase("hobbit", "hobbit"))
                .thenReturn(List.of(book));

        List<BookEntity> result = bookService.searchBooks("hobbit");

        assertEquals(1, result.size());
        assertEquals("El Hobbit", result.get(0).getTitle());
    }
}
