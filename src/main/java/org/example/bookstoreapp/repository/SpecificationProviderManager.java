package org.example.bookstoreapp.repository;

public interface SpecificationProviderManager<T> {

    SpecificationProvider<T, ?> getSpecificationProvider(String key);
}
