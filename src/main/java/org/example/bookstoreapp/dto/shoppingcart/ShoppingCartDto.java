package org.example.bookstoreapp.dto.shoppingcart;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.example.bookstoreapp.dto.cartitem.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems = new HashSet<>();
}
