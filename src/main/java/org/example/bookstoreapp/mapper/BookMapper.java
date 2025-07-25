package org.example.bookstoreapp.mapper;

import org.example.bookstoreapp.config.MapperConfig;
import org.example.bookstoreapp.dto.BookDto;
import org.example.bookstoreapp.dto.CreateBookRequestDto;
import org.example.bookstoreapp.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);
}
