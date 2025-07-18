package org.example.bookstoreapp;

import java.math.BigDecimal;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreAppApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setAuthor("John Doe");
                book.setTitle("Book Title");
                book.setDescription("Book Description");
                book.setIsbn("123456789");
                book.setPrice(BigDecimal.TEN);
                book.setCoverImage("Book Cover Image");

                bookService.save(book);
                bookService.findAll().forEach(System.out::println);
            }
        };
    }

}

