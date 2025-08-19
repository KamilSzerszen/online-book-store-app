package org.example.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.category.CategoryRequestDto;
import org.example.bookstoreapp.dto.category.CategoryResponseDto;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.CategoryMapper;
import org.example.bookstoreapp.model.Category;
import org.example.bookstoreapp.repository.category.CategoryRepository;
import org.example.bookstoreapp.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getById(long id) {
        return categoryRepository.findById(id)
                .map(category -> categoryMapper.toDto(category))
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {
        Category model = categoryMapper.toModel(categoryRequestDto);
        Category save = categoryRepository.save(model);
        return categoryMapper.toDto(save);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found with id " + id));

        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());
        Category save = categoryRepository.save(category);

        return categoryMapper.toDto(save);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
