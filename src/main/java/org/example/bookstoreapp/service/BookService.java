package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.book.BookDto;
import org.example.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstoreapp.dto.book.BookSearchParametersDto;
import org.example.bookstoreapp.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto createBook(CreateBookRequestDto createBookRequestDto);

    void deleteById(Long id);

    BookDto updateBookById(CreateBookRequestDto createBookRequestDto, Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}
