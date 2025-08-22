package org.example.bookstoreapp.dto.ShoppingCart;

import lombok.Data;
import org.example.bookstoreapp.dto.cartItem.CartItemDto;
import java.util.HashSet;
import java.util.Set;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems = new HashSet<>();
}
