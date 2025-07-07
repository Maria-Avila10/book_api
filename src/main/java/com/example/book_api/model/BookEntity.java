
package com.example.book_api.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;



    @Column(nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "isbn")
    private String isbn;


    @Column(name = "publishedYear")
    private Integer publishedYear;

    @Column(name = "url")
    private String url;

    @Column(name = "category")
    private String category;
}