package org.example.bookstoreapp.repository;

import java.util.List;
import java.util.Optional;
import org.example.bookstoreapp.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
