package com.example.book_api.model;

import jakarta.persistence.*; // Importa anotaciones JPA (para mapear clases a tablas)
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Table(name = "book") // Asocia esta entidad a la tabla llamada "book" en la base de datos
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente
    private Integer id;

    private String title;
    private String author;

    private String isbn;

    @Column(name = "published_year") // Mapea la propiedad al nombre de columna "published_year"
    private Integer publishedYear;

    @Column(name = "url")
    private String url;

}