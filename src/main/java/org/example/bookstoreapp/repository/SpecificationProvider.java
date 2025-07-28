package org.example.bookstoreapp.repository;

import org.example.bookstoreapp.dto.BookSearchParametersDto;
import org.example.bookstoreapp.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T, P> {

    Specification<T> getSpecification(P params);
}
