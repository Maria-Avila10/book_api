package com.example.book_api.repository;

import com.example.book_api.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category);


}