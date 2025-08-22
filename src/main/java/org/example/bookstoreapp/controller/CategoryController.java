package org.example.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstoreapp.dto.category.CategoryDto;
import org.example.bookstoreapp.service.BookService;
import org.example.bookstoreapp.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Categories controller", description = "Endpoint for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create", description = "Create new category")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get All", description = "Get all categories")
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get by id", description = "Get category by unique id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update", description = "Update category by unique id")
    public CategoryDto updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryDto requestDto
    ) {
        return categoryService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete", description = "Delete category by unique id")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    @Operation(
            summary = "Get books by category",
            description = "Get books by category with unique id"
    )
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return bookService.getBooksByCategoryId(id, pageable);
    }

}
