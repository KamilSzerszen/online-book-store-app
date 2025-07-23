package org.example.bookstoreapp.service;

import java.util.List;
import org.example.bookstoreapp.dto.BookDto;
import org.example.bookstoreapp.dto.CreateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto createBook(CreateBookRequestDto createBookRequestDto);
}
