package com.example.book_api.enricher;

import com.example.book_api.client.GoogleBooksClient;
import com.example.book_api.model.BookEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GoogleBooksEnricher implements BookEnricher {

    private final GoogleBooksClient googleBooksClient;

    public GoogleBooksEnricher(GoogleBooksClient googleBooksClient) {
        this.googleBooksClient = googleBooksClient;
    }

    @Override
    public void enrich(BookEntity book) {
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            return;
        }

        try {
            Map<String, Object> response = googleBooksClient.buscarPorIsbn("isbn:" + book.getIsbn());

            if (response != null && response.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
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

        }
    }
}
