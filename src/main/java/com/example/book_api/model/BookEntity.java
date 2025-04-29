package com.example.book_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String author;
    private String isbn;

    @Column(name = "published_year") // Mapeo correcto a la BD
    private Integer publishedYear;

    @Column(name = "url")
    private String url;

}