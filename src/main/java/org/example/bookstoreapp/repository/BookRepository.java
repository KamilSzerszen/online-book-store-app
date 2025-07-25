package org.example.bookstoreapp.repository;

import java.util.List;
import java.util.Optional;
import org.example.bookstoreapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
