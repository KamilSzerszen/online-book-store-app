package org.example.bookstoreapp.service;

import java.util.List;
import org.example.bookstoreapp.model.Book;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
