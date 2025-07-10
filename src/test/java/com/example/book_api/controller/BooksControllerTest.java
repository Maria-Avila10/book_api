package com.example.book_api.controller;

import com.example.book_api.mapper.BookMapper;
import com.example.book_api.model.Book;
import com.example.book_api.model.BookEntity;
import com.example.book_api.model.BookRequest;
import com.example.book_api.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}