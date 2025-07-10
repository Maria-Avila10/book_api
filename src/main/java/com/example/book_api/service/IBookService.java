package com.example.book_api.service;

import com.example.book_api.model.BookEntity;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    BookEntity createBook(BookEntity book);
    Optional<BookEntity> findBookById(Integer id);
    BookEntity updateBook(Integer id, BookEntity updatedBook);
    boolean deleteBook(Integer id);
    List<BookEntity> findAllBooks();
    List<BookEntity> searchBooks(String text);
}
