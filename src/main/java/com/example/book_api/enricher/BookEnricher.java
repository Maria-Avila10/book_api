package com.example.book_api.enricher;

import com.example.book_api.model.BookEntity;

public interface BookEnricher {
    void enrich(BookEntity book);
}
