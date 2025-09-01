package org.example.bookstoreapp.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDto {

    @NotNull
    private String name;

    private String description;
}
