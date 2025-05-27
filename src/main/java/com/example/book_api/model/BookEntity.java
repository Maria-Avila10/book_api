package com.example.book_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
// Importa anotaciones JPA (para mapear clases a tablas)
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book") // Asocia esta entidad a la tabla llamada "book" en la base de datos
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente
    private Integer id;

    private String title;
    private String author;

    private String isbn;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(name = "url")
    private String url;

}