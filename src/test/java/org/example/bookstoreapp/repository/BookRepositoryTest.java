package org.example.bookstoreapp.repository;

import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Should find all books by category id
            """)
    @Sql(
            scripts = "classpath:database/add-books-categories-relation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/remove-books-categories-relation.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByCategoryId_bookAndCategoryExist_returnsBook() {
        Book expected = new Book();
        expected.setTitle("Title");
        expected.setAuthor("Author");
        expected.setIsbn("Isbn");
        expected.setPrice(BigDecimal.valueOf(10.00));

        Long categoryId = 1L;

        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> result = bookRepository.findAllByCategoryId(categoryId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.getTitle(), result.getContent().get(0).getTitle());
        Assertions.assertEquals(expected.getAuthor(), result.getContent().get(0).getAuthor());
        Assertions.assertEquals(expected.getIsbn(), result.getContent().get(0).getIsbn());
        Assertions.assertTrue(expected.getPrice().compareTo(result.getContent().get(0).getPrice()) == 0);
    }
}
