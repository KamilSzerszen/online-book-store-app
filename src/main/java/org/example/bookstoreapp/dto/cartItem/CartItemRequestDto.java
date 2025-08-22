package org.example.bookstoreapp.dto.cartItem;

import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull Long bookId,
        @NotNull int quantity
) {}
