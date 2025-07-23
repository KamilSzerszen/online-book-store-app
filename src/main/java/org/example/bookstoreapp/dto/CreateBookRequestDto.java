package org.example.bookstoreapp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateBookRequestDto {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
