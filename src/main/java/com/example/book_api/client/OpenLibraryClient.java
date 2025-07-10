package com.example.book_api.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "openlibrary", url = "https://openlibrary.org")
public interface OpenLibraryClient {

    @GetMapping("/api/books")
    Map<String, Object> buscarLibro(
            @RequestParam("bibkeys") String bibkeys,
            @RequestParam("format") String format,
            @RequestParam("jscmd") String jscmd
    );
}