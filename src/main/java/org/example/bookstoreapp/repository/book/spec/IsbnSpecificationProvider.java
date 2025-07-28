package org.example.bookstoreapp.repository.book.spec;

import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book, String[]> {

    @Override
    public String getKey() {
        return "isbn";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get("isbn").in(Arrays.stream(params).toArray());
    }
}
