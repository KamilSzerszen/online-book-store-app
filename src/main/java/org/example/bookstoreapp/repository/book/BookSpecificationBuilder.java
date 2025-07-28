package org.example.bookstoreapp.repository.book;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.BookSearchParametersDto;
import org.example.bookstoreapp.dto.PriceParams;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.SpecificationBuilder;
import org.example.bookstoreapp.repository.SpecificationProvider;
import org.example.bookstoreapp.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {

        Specification<Book> spec = (root, query, cb) -> cb.conjunction();

        if (searchParameters.author() != null && searchParameters.author().length > 0) {

            @SuppressWarnings("unchecked")
            SpecificationProvider<Book, String[]> authorProvider =
                    (SpecificationProvider<Book, String[]>) bookSpecificationProviderManager
                            .getSpecificationProvider("author");

            spec = spec.and(authorProvider.getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {

            @SuppressWarnings("unchecked")
            SpecificationProvider<Book, String[]> isbnProvider =
                    (SpecificationProvider<Book, String[]>) bookSpecificationProviderManager
                            .getSpecificationProvider("isbn");

            spec = spec.and(isbnProvider.getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.priceParams().minPrice() != null
                && searchParameters.priceParams().maxPrice() != null) {

            @SuppressWarnings("unchecked")
            SpecificationProvider<Book, PriceParams> priceProvider =
                    (SpecificationProvider<Book, PriceParams>) bookSpecificationProviderManager
                            .getSpecificationProvider("price");

            spec = spec.and(priceProvider.getSpecification(searchParameters.priceParams()));
        }
        if (searchParameters.title() != null && searchParameters.title().length > 0) {

            @SuppressWarnings("unchecked")
            SpecificationProvider<Book, String[]> titleProvider =
                    (SpecificationProvider<Book, String[]>) bookSpecificationProviderManager
                            .getSpecificationProvider("title");

            spec = spec.and(titleProvider.getSpecification(searchParameters.title()));
        }

        return spec;
    }
}
