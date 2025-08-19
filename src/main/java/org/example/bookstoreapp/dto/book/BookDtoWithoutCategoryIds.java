package org.example.bookstoreapp.dto.book;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookDtoWithoutCategoryIds {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
