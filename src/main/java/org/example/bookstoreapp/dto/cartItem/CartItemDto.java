package org.example.bookstoreapp.dto.cartItem;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity) {

}
