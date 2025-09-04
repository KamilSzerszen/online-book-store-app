package org.example.bookstoreapp.mapper;

import java.util.stream.Collectors;
import org.example.bookstoreapp.config.MapperConfig;
import org.example.bookstoreapp.dto.book.BookDto;
import org.example.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstoreapp.dto.book.CreateBookRequestDto;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryId(BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryId(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }
}
