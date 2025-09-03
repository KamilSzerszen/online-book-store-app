package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstoreapp.dto.cartitem.CartItemDto;
import org.example.bookstoreapp.dto.cartitem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartitem.CartItemUpdateRequestDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartByUserId();

    CartItemDto addItemToCart(CartItemRequestDto requestDto);

    CartItemDto updateItemQuantity(Long id, CartItemUpdateRequestDto requestDto);

    void deleteItemFromCart(Long id);
}
