package org.example.bookstoreapp.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.BookDto;
import org.example.bookstoreapp.dto.BookSearchParametersDto;
import org.example.bookstoreapp.dto.CreateBookRequestDto;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.BookMapper;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.book.BookRepository;
import org.example.bookstoreapp.repository.book.BookSpecificationBuilder;
import org.example.bookstoreapp.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find Book with id: " + id));

        return bookMapper.toDto(book);
    }

    @Override
    public BookDto createBook(CreateBookRequestDto createBookRequestDto) {
        Book book = bookMapper.toModel(createBookRequestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBookById(CreateBookRequestDto createBookRequestDto, Long id) {
        Book bookBeforeUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Book with id: " + id));
        bookBeforeUpdate.setTitle(createBookRequestDto.getTitle());
        bookBeforeUpdate.setAuthor(createBookRequestDto.getAuthor());
        bookBeforeUpdate.setDescription(createBookRequestDto.getDescription());
        bookBeforeUpdate.setPrice(createBookRequestDto.getPrice());
        bookBeforeUpdate.setIsbn(createBookRequestDto.getIsbn());
        bookBeforeUpdate.setCoverImage(createBookRequestDto.getCoverImage());
        Book savedBook = bookRepository.save(bookBeforeUpdate);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

}
