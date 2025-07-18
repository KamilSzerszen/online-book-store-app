package org.example.bookstoreapp.repository.impl;

import java.util.List;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.BookRepository;

public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return List.of();
    }
}
