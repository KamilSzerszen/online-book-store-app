package org.example.bookstoreapp.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.exception.SpecificationProviderNotFoundException;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.repository.SpecificationProvider;
import org.example.bookstoreapp.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManagerImpl<T>
        implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book, ?>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book, ?> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new SpecificationProviderNotFoundException(
                                "Specification provider " + key + " not found"));
    }
}
