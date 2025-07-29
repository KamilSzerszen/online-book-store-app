package org.example.bookstoreapp.controller;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.BookDto;
import org.example.bookstoreapp.dto.BookSearchParametersDto;
import org.example.bookstoreapp.dto.CreateBookRequestDto;
import org.example.bookstoreapp.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.createBook(createBookRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    public BookDto updateBookById(
            @RequestBody CreateBookRequestDto createBookRequestDto,
            @PathVariable Long id) {
        return bookService.updateBookById(createBookRequestDto, id);
    }

    @GetMapping("/search")
    public List<BookDto> search(@RequestParam(required = false) String[] title,
                                @RequestParam(required = false) String[] author,
                                @RequestParam(required = false) String[] isbn,
                                @RequestParam(required = false) BigDecimal minPrice,
                                @RequestParam(required = false) BigDecimal maxPrice) {

        BookSearchParametersDto params = new BookSearchParametersDto(
                title, author, isbn, minPrice, maxPrice);

        return bookService.search(params);
    }
}
