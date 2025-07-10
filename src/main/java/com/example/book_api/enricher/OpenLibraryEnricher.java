package com.example.book_api.enricher;

import com.example.book_api.client.OpenLibraryClient;
import com.example.book_api.model.BookEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OpenLibraryEnricher implements BookEnricher {

    private final OpenLibraryClient openLibraryClient;

    public OpenLibraryEnricher(OpenLibraryClient openLibraryClient) {
        this.openLibraryClient = openLibraryClient;
    }

    @Override
    public void enrich(BookEntity book) {
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            book.setUrl("ISBN no proporcionado");
            return;
        }

        try {
            String key = "ISBN:" + book.getIsbn();
            Map<String, Object> response = openLibraryClient.buscarLibro(key, "json", "data");


            if (response != null && !response.isEmpty()) {
                Map<String, Object> bookData = (Map<String, Object>) response.get(key);
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
    }
}
