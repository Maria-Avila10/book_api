package com.example.book_api.repository;

import com.example.book_api.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indica que esta interfaz es un componente de acceso a datos (DAO) gestionado por Spring
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);


}
