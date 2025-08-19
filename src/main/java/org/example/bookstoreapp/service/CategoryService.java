package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.category.CategoryRequestDto;
import org.example.bookstoreapp.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(long id);

    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);

    void delete(Long id);
}
