package org.example.bookstoreapp.repository.book.spec;

import org.example.bookstoreapp.dto.PriceParams;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book, PriceParams> {

    @Override
    public String getKey() {
        return "price";
    }

    public Specification<Book> getSpecification(PriceParams params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.minPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), params.minPrice()));
            }

            if (params.maxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), params.maxPrice()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
