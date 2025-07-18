package org.example.bookstoreapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Title cannot be null")
    @Column(nullable = false)
    private String title;
    @NotNull(message = "Author cannot be null")
    @Column(nullable = false)
    private String author;
    @NotNull(message = "Isbn cannot be null")
    @Column(nullable = false)
    private String isbn;
    @NotNull(message = "Price cannot be null")
    @Column(nullable = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
