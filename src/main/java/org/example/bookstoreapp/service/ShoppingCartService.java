package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.ShoppingCart.ShoppingCartDto;
import org.example.bookstoreapp.dto.cartItem.CartItemDto;
import org.example.bookstoreapp.dto.cartItem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartItem.CartItemUpdateRequestDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartByUserId();

    CartItemDto addItemToCart(CartItemRequestDto requestDto);

    CartItemDto updateItemQuantity(Long id, CartItemUpdateRequestDto requestDto);

    void deleteItemFromCart(Long id);
}
