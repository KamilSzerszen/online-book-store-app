package org.example.bookstoreapp.service;

import java.util.List;
import org.example.bookstoreapp.dto.BookDto;
import org.example.bookstoreapp.dto.BookSearchParametersDto;
import org.example.bookstoreapp.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto createBook(CreateBookRequestDto createBookRequestDto);

    void deleteById(Long id);

    BookDto updateBookById(CreateBookRequestDto createBookRequestDto, Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
