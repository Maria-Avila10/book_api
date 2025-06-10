package com.example.book_api.mapper;

import com.example.book_api.model.Book;
import com.example.book_api.model.BookEntity;
import com.example.book_api.model.BookRequest;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toDto(BookEntity entity);


    BookEntity toEntity(BookRequest dto);
}


