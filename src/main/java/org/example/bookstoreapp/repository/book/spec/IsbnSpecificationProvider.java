package org.example.bookstoreapp.repository.book.spec;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book, String[]> {

    @Override
    public String getKey() {
        return "isbn";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb)
                -> cb.or(
                        Arrays.stream(params)
                                .map(param -> cb.like(cb.lower(root.get("isbn")),
                                        "%" + param + "%"))
                                .toArray(Predicate[]::new)
        );
    }
}
