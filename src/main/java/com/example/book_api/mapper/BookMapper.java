package com.example.book_api.mapper;

import com.example.book_api.model.Book;
import com.example.book_api.model.BookEntity;
import com.example.book_api.model.BookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "titleRq", target = "title")
    @Mapping(source = "authorRq", target = "author")
    @Mapping(source = "isbnRq", target = "isbn")
    @Mapping(source = "publishedYearRq", target = "publishedYear")
    BookEntity toEntity(BookRequest dto);

    Book toDto(BookEntity entity);
}


