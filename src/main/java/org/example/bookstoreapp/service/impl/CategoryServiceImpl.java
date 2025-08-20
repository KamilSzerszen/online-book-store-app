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

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(long id) {
        return categoryRepository.findById(id)
                .map(category -> categoryMapper.toDto(category))
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category model = categoryMapper.toModel(categoryDto);
        Category save = categoryRepository.save(model);
        return categoryMapper.toDto(save);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found with id " + id));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category save = categoryRepository.save(category);

        return categoryMapper.toDto(save);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
