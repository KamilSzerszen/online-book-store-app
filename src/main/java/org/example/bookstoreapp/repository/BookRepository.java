package org.example.bookstoreapp.repository;

import java.util.List;
import org.example.bookstoreapp.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
