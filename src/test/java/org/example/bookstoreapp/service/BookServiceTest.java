package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.book.BookDto;
import org.example.bookstoreapp.mapper.BookMapper;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.book.BookRepository;
import org.example.bookstoreapp.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
            Verifying that the method retrieves all books from the repository
            """)
    public void findAll_repositoryContainsTwoBooks_returnsTwoBooks() {
        Book bookOne = new Book();
        bookOne.setId(1L);
        bookOne.setTitle("Book One");
        bookOne.setAuthor("Author One");
        bookOne.setIsbn("ISBN One");
        bookOne.setPrice(BigDecimal.TEN);

        Book bookTwo = new Book();
        bookTwo.setId(2L);
        bookTwo.setTitle("Book Two");
        bookTwo.setAuthor("Author Two");
        bookTwo.setIsbn("ISBN Two");
        bookTwo.setPrice(BigDecimal.TEN);

        BookDto bookDtoOne = new BookDto();
        bookDtoOne.setId(bookOne.getId());
        bookDtoOne.setTitle(bookOne.getTitle());
        bookDtoOne.setAuthor(bookOne.getAuthor());
        bookDtoOne.setIsbn(bookOne.getIsbn());
        bookDtoOne.setPrice(bookOne.getPrice());

        BookDto bookDtoTwo = new BookDto();
        bookDtoTwo.setId(bookTwo.getId());
        bookDtoTwo.setTitle(bookTwo.getTitle());
        bookDtoTwo.setAuthor(bookTwo.getAuthor());
        bookDtoTwo.setIsbn(bookTwo.getIsbn());
        bookDtoTwo.setPrice(bookTwo.getPrice());

        List<Book> books = List.of(bookOne, bookTwo);
        Page<Book> booksPage = new PageImpl<>(books);

        Mockito.when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(booksPage);

        Mockito.when(bookMapper.toDto(bookOne)).thenReturn(bookDtoOne);
        Mockito.when(bookMapper.toDto(bookTwo)).thenReturn(bookDtoTwo);

        Pageable pageable = Pageable.unpaged();
        Page<BookDto> result = bookService.findAll(pageable);

        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(bookDtoOne.getTitle(), result.getContent().get(0).getTitle());
        Assertions.assertEquals(bookDtoTwo.getTitle(), result.getContent().get(1).getTitle());

    }


}
