package org.example.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.category.CategoryDto;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.CategoryMapper;
import org.example.bookstoreapp.model.Category;
import org.example.bookstoreapp.repository.category.CategoryRepository;
import org.example.bookstoreapp.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(long id) {
        return categoryRepository.findById(id)
                .map(category -> categoryMapper.toDto(category))
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toModel(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found with id " + id));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found with id " + id)
        );

        categoryRepository.delete(category);
    }
}
