package com.example.book_api.controller;

import com.example.book_api.mapper.BookMapper;
import com.example.book_api.model.Book;
import com.example.book_api.model.BookEntity;
import com.example.book_api.model.BookRequest;
import com.example.book_api.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BooksControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BooksController booksController;

    private BookRequest bookRequest;
    private BookEntity bookEntity;
    private Book bookResponse;
    private List<BookEntity> bookEntityList;
    private List<Book> bookList;


    @BeforeEach
    void setUp() {
        bookRequest = new BookRequest();
        bookRequest.setTitleRq("El Hobbit");
        bookRequest.setAuthorRq("J.R.R. Tolkien");
        bookRequest.setIsbnRq("9780261102217");
        bookRequest.setPublishedYearRq(1937);

        bookEntity = new BookEntity();
        bookEntity.setId(1);
        bookEntity.setTitle("El Hobbit");
        bookEntity.setAuthor("J.R.R. Tolkien");
        bookEntity.setIsbn("9780261102217");
        bookEntity.setPublishedYear(1937);
        bookEntity.setUrl("https://openlibrary.org");
        bookEntity.setCategory("Fiction");

        bookResponse = new Book();
        bookResponse.setId(1);
        bookResponse.setTitle("El Hobbit");
        bookResponse.setAuthor("J.R.R. Tolkien");
        bookResponse.setIsbn("9780261102217");
        bookResponse.setPublishedYear(1937);
        bookResponse.setUrl("https://openlibrary.org");
        bookResponse.setCategory("Fiction");
    }

    @Test
    void createBook() {
        //Arrange
        when(mapper.toEntity(any(BookRequest.class))).thenReturn(bookEntity);
        when(bookService.createBook(any(BookEntity.class))).thenReturn(bookEntity);
        when(mapper.toDto(any(BookEntity.class))).thenReturn(bookResponse);

        //act
        ResponseEntity<Book> response = booksController.createBook(bookRequest);

        // assert (Verificar)
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        Book createdBook = response.getBody();
        assertEquals(1, createdBook.getId());
        assertEquals("El Hobbit", createdBook.getTitle());
        assertEquals("J.R.R. Tolkien", createdBook.getAuthor());
        assertEquals("9780261102217", createdBook.getIsbn());
        assertEquals(1937, createdBook.getPublishedYear());
        assertEquals("https://openlibrary.org", createdBook.getUrl());
        assertEquals("Fiction", createdBook.getCategory());
    }

    @Test
    void getAllBooks() {
        when(bookService.findAllBooks()).thenReturn(List.of(bookEntity));
        when(mapper.toDto(bookEntity)).thenReturn(bookResponse);

        ResponseEntity<List<Book>> response = booksController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("El Hobbit", response.getBody().get(0).getTitle());
    }
    @Test
    void getBookById_found() {
        when(bookService.findBookById(1)).thenReturn(java.util.Optional.of(bookEntity));
        when(mapper.toDto(bookEntity)).thenReturn(bookResponse);

        ResponseEntity<Book> response = booksController.getBookById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El Hobbit", response.getBody().getTitle());
    }

    @Test
    void getBookById_notFound() {
        when(bookService.findBookById(99)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Book> response = booksController.getBookById(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void searchBooksByText() {
        when(bookService.searchBooks("hobbit")).thenReturn(List.of(bookEntity));
        when(mapper.toDto(bookEntity)).thenReturn(bookResponse);

        ResponseEntity<List<Book>> response = booksController.getBooksearchText("hobbit");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("El Hobbit", response.getBody().get(0).getTitle());
    }
    @Test
    void updateBook_success() {
        when(mapper.toEntity(bookRequest)).thenReturn(bookEntity);
        when(bookService.updateBook(1, bookEntity)).thenReturn(bookEntity);
        when(mapper.toDto(bookEntity)).thenReturn(bookResponse);

        ResponseEntity<Book> response = booksController.updateBook(1, bookRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("El Hobbit", response.getBody().getTitle());
    }

    @Test
    void updateBook_notFound() {
        when(mapper.toEntity(bookRequest)).thenReturn(bookEntity);
        when(bookService.updateBook(99, bookEntity)).thenThrow(new RuntimeException("Libro no encontrado"));

        ResponseEntity<Book> response = booksController.updateBook(99, bookRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void deleteBook_success() {
        when(bookService.deleteBook(1)).thenReturn(true);

        ResponseEntity<Void> response = booksController.deleteBook(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteBook_notFound() {
        when(bookService.deleteBook(99)).thenReturn(false);

        ResponseEntity<Void> response = booksController.deleteBook(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void getBooksGroupedByCategory() {
        when(bookService.findAllBooks()).thenReturn(List.of(bookEntity));
        when(mapper.toDto(bookEntity)).thenReturn(bookResponse);

        ResponseEntity<Map<String, List<Book>>> response = booksController.getBooksByCategory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("Fiction"));
        assertEquals(1, response.getBody().get("Fiction").size());
    }

}