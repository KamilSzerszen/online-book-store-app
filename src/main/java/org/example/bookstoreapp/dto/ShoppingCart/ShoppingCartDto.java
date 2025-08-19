package org.example.bookstoreapp.dto.ShoppingCart;

import org.example.bookstoreapp.dto.cartItem.CartItemDto;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems = new HashSet<>();
}
