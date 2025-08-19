package org.example.bookstoreapp.dto.ShoppingCart;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<Long> cartItemIds = new HashSet<>();
}
