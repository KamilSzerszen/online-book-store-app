package org.example.bookstoreapp.dto.cartItem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequestDto {

    @NotNull
    private Long bookId;

    @NotNull
    private int quantity;
}
