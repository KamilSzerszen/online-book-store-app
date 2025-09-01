package org.example.bookstoreapp.dto.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
