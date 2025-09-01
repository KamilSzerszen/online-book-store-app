package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.category.CategoryDto;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.CategoryMapper;
import org.example.bookstoreapp.model.Category;
import org.example.bookstoreapp.repository.category.CategoryRepository;
import org.example.bookstoreapp.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
            Should find all category and returns Pade<CategoryDto>
            """)
    public void findAll_repositoryContainsTwoCategories_returnsTwoCategories() {
        Category categoryOne = new Category();
        categoryOne.setId(1L);
        categoryOne.setName("Category One");

        Category categoryTwo = new Category();
        categoryTwo.setId(2L);
        categoryTwo.setName("Category Two");

        CategoryDto categoryOneDto = new CategoryDto();
        categoryOneDto.setName(categoryOne.getName());

        CategoryDto categoryTwoDto = new CategoryDto();
        categoryTwoDto.setName(categoryTwo.getName());

        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> categoryPage = new PageImpl<>(List.of(categoryOne, categoryTwo));

        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        Mockito.when(categoryMapper.toDto(categoryOne)).thenReturn(categoryOneDto);
        Mockito.when(categoryMapper.toDto(categoryTwo)).thenReturn(categoryTwoDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(categoryOneDto, result.getContent().get(0));
        Assertions.assertEquals(categoryTwoDto, result.getContent().get(1));
    }

    @Test
    @DisplayName("""
            Should return CategoryDto when category exists by id
            """)
    public void getById_categoryExists_returnsCategoryDto() {
        Long validId = 1L;
        Category category = new Category();
        category.setId(validId);
        category.setName("Category One");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());

        Mockito.when(categoryRepository.findById(validId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(categoryDto.getName(), result.getName());

    }

    @Test
    @DisplayName("""
            Should throw EntityNotFoundException when category not found by id
            """)
    public void getById_categoryNotFound_throwsException() {
        Long validId = 1L;

        Mockito.when(categoryRepository.findById(validId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () ->
                categoryService.getById(validId)
        );

        Assertions.assertEquals("Category not found with id " + validId, exception.getMessage());
    }

    @Test
    @DisplayName("""
            Should save category and return CategoryDto
            """)
    public void save_categoryDto_returnsSavedCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("New Category");

        Category category = new Category();
        category.setName("New Category");

        Category savedCategory = new Category();
        savedCategory.setName("New Category");

        CategoryDto savedCategoryDto = new CategoryDto();
        savedCategoryDto.setName("New Category");

        Mockito.when(categoryMapper.toModel(categoryDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(savedCategory);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);

        CategoryDto result = categoryService.save(categoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedCategoryDto.getName(), result.getName());

    }

    @Test
    @DisplayName("""
            Should update existing category and return updated CategoryDto
            """)
    public void update_categoryExists_returnsUpdatedCategoryDto() {
        Long validId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Updated Category");
        categoryDto.setDescription("Updated Description");

        Category existingCategory = new Category();
        existingCategory.setId(validId);
        existingCategory.setName("Old Category");
        existingCategory.setDescription("Old Description");

        Category savedCategory = new Category();
        savedCategory.setId(validId);
        savedCategory.setName("Updated Category");
        savedCategory.setDescription("Updated Description");

        CategoryDto savedCategoryDto = new CategoryDto();
        savedCategoryDto.setName("Updated Category");
        savedCategoryDto.setDescription("Updated Description");

        Mockito.when(categoryRepository.findById(validId)).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepository.save(existingCategory)).thenReturn(savedCategory);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);

        CategoryDto result = categoryService.update(validId, categoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Category", result.getName());
        Assertions.assertEquals("Updated Description", result.getDescription());

    }

    @Test
    @DisplayName("""
            Should throw EntityNotFoundException when updating non-existing category
            """)
    public void update_categoryNotFound_throwsException() {
        Long validId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Updated Category");

        Mockito.when(categoryRepository.findById(validId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () ->
                categoryService.update(validId, categoryDto)
        );

        Assertions.assertEquals("Category not found with id " + validId, exception.getMessage());

    }

    @Test
    @DisplayName("""
            Should delete existing category
            """)
    public void delete_categoryExists_deletesCategory() {
        Long validId = 1L;
        Category category = new Category();
        category.setId(validId);
        category.setName("Category One");

        Mockito.when(categoryRepository.findById(validId)).thenReturn(Optional.of(category));

        categoryService.delete(validId);

        Mockito.verify(categoryRepository).findById(validId);
        Mockito.verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("""
            Should throw EntityNotFoundException when deleting non-existing category
            """)
    public void delete_categoryNotFound_throwsException() {
        Long validId = 1L;

        Mockito.when(categoryRepository.findById(validId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () ->
                categoryService.delete(validId)
        );

        Assertions.assertEquals("Category not found with id " + validId, exception.getMessage());

        Mockito.verify(categoryRepository).findById(validId);
        Mockito.verify(categoryRepository, Mockito.never()).delete(Mockito.any());
    }

}
