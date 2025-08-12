package org.example.bookstoreapp.repository;

import org.example.bookstoreapp.dto.book.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {

    Specification<T> build(BookSearchParametersDto searchParameters);
}
