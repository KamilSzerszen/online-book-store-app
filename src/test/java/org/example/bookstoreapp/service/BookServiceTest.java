package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.book.BookDto;
import org.example.bookstoreapp.dto.book.BookSearchParametersDto;
import org.example.bookstoreapp.dto.book.CreateBookRequestDto;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.BookMapper;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.SpecificationBuilder;
import org.example.bookstoreapp.repository.book.BookRepository;
import org.example.bookstoreapp.repository.book.BookSpecificationBuilder;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private SpecificationBuilder<Book> bookSpecificationBuilder;

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

    @Test
    @DisplayName("""
            Returns book for existing id
            """)
    public void findById_validId_returnsBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setIsbn("ISBN One");
        book.setPrice(BigDecimal.TEN);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());

        Long validId = 1L;

        Mockito.when(bookRepository.findById(validId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findById(validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookDto.getId(), result.getId());
        Assertions.assertEquals(bookDto.getTitle(), result.getTitle());
        Assertions.assertEquals(bookDto.getAuthor(), result.getAuthor());
        Assertions.assertEquals(bookDto.getIsbn(), result.getIsbn());
        Assertions.assertEquals(bookDto.getPrice(), result.getPrice());
    }

    @Test
    @DisplayName("""
            Throwing an exception when no book exists for the given id
            """)
    public void findById_bookDoesNotExist_throwsException() {
        Long validId = 1L;

        Mockito.when(bookRepository.findById(validId)).thenThrow(new EntityNotFoundException(
                "Cannot find Book with id: " + validId
        ));

        EntityNotFoundException result = Assertions.assertThrows(
                EntityNotFoundException.class, () -> bookService.findById(validId)
        );

        Assertions.assertEquals(
                "Cannot find Book with id: " + validId,
                result.getMessage()
        );
    }

    @Test
    @DisplayName("""
            Should create a new book and return its DTO
            """)
    public void createBook_validRequest_returnsBookDto() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setIsbn("ISBN One");
        book.setPrice(BigDecimal.TEN);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle(book.getTitle());
        createBookRequestDto.setAuthor(book.getAuthor());
        createBookRequestDto.setIsbn(book.getIsbn());
        createBookRequestDto.setPrice(book.getPrice());

        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.createBook(createBookRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), book.getId());
        Assertions.assertEquals(book.getTitle(), result.getTitle());
        Assertions.assertEquals(book.getAuthor(), result.getAuthor());
        Assertions.assertEquals(book.getIsbn(), result.getIsbn());
        Assertions.assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    @DisplayName("""
            Should delete exist books with valid id
            """)
    public void deleteBookById_bookExistIdIsValid_verifyMockito() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setIsbn("ISBN One");
        book.setPrice(BigDecimal.TEN);

        Long validId = 1L;

        Mockito.when(bookRepository.findById(validId)).thenReturn(Optional.of(book));

        bookService.deleteById(validId);

        Mockito.verify(bookRepository).findById(validId);
        Mockito.verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("""
            Should throw exception when book doses not existing
            """)
    public void deleteBookById_bookDoesNotExist_throwsException() {
        Long validId = 1L;

        Mockito.when(bookRepository.findById(validId)).thenThrow(new EntityNotFoundException(
                "Cannot find Book with id: " + validId
        ));

        EntityNotFoundException result = Assertions.assertThrows(
                EntityNotFoundException.class, () -> bookService.findById(validId)
        );

        Assertions.assertEquals(
                "Cannot find Book with id: " + validId,
                result.getMessage()
        );
    }

    @Test
    @DisplayName("""
            Should update exisiting book
            """)
    public void updateBookById_bookExistIdIsValid_returnsBookDto() {
        Book bookBeforeUpdate = new Book();
        bookBeforeUpdate.setId(1L);
        bookBeforeUpdate.setTitle("Book One");
        bookBeforeUpdate.setAuthor("Author One");
        bookBeforeUpdate.setIsbn("ISBN One");
        bookBeforeUpdate.setPrice(BigDecimal.TEN);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book One Update");
        requestDto.setAuthor("Author One Update");
        requestDto.setIsbn(bookBeforeUpdate.getIsbn());
        requestDto.setPrice(bookBeforeUpdate.getPrice());

        Book bookAfterUpdate = new Book();
        bookAfterUpdate.setId(bookBeforeUpdate.getId());
        bookAfterUpdate.setTitle(requestDto.getTitle());
        bookAfterUpdate.setAuthor(requestDto.getAuthor());
        bookAfterUpdate.setIsbn(requestDto.getIsbn());
        bookAfterUpdate.setPrice(requestDto.getPrice());

        BookDto bookDto = new BookDto();
        bookDto.setId(bookAfterUpdate.getId());
        bookDto.setTitle(requestDto.getTitle());
        bookDto.setAuthor(requestDto.getAuthor());
        bookDto.setIsbn(requestDto.getIsbn());
        bookDto.setPrice(requestDto.getPrice());

        Long validId = 1L;

        Mockito.when(bookRepository.findById(validId)).thenReturn(Optional.of(bookBeforeUpdate));
        Mockito.when(bookRepository.save(bookBeforeUpdate)).thenReturn(bookAfterUpdate);
        Mockito.when(bookMapper.toDto(bookAfterUpdate)).thenReturn(bookDto);

        BookDto result = bookService.updateBookById(requestDto, validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), bookDto.getId());
        Assertions.assertEquals(bookDto.getTitle(), result.getTitle());
        Assertions.assertEquals(bookDto.getAuthor(), result.getAuthor());
        Assertions.assertEquals(bookDto.getIsbn(), result.getIsbn());
        Assertions.assertEquals(bookDto.getPrice(), result.getPrice());
    }

    @Test
    @DisplayName("""
            Should return book with search params
            """)
    public void search_bookWithSearchParamsExist_returnsPageBookDto() {
        BookSearchParametersDto searchParams = new BookSearchParametersDto(
                new String[]{"Book"},
                new String[]{"Author"},
                new String[]{"Isbn"},
                BigDecimal.ZERO,
                BigDecimal.TEN);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book");
        book.setAuthor("Author");
        book.setIsbn("Isbn");
        book.setPrice(BigDecimal.valueOf(5.0));

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());

        Pageable pageable = PageRequest.of(0, 10);

        Specification<Book> spec =
                (root, query, cb) -> cb.conjunction();

        Page<Book> bookPage = new PageImpl<>(List.of(book));

        Mockito.when(bookSpecificationBuilder.build(searchParams)).thenReturn(spec);
        Mockito.when(bookRepository.findAll(spec, pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.search(searchParams, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(bookDto.getId(), result.getContent().get(0).getId());

    }

}
