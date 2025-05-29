
package com.example.book_api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(name = "url")
    private String url;
}