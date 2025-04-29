package com.example.book_api.batch.config;

import com.example.book_api.model.BookEntity;
import org.springframework.batch.item.ItemProcessor;

public class BookEntityProcessor implements ItemProcessor<BookEntity, BookEntity> {
    @Override
    public BookEntity process(BookEntity book) throws Exception {
        // Procesa el libro si es necesario (por ejemplo, valida o transforma datos)
        return book;
    }
}
