package com.example.book_api.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "googlebooks", url = "https://www.googleapis.com")
public interface GoogleBooksClient {

    @GetMapping("/books/v1/volumes")
    Map<String, Object> buscarPorIsbn(@RequestParam("q") String isbnQuery);
}