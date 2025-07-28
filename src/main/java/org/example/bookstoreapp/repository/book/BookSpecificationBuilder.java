package org.example.bookstoreapp.repository.book;

import org.example.bookstoreapp.dto.BookSearchParametersDto;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.SpecificationBuilder;
import org.example.bookstoreapp.repository.book.spec.AuthorSpecificationProvider;
import org.example.bookstoreapp.repository.book.spec.IsbnSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = null;
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(new AuthorSpecificationProvider().getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            spec = spec.and(new IsbnSpecificationProvider().getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.price())
        return spec;
    }
}
