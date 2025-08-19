package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.ShoppingCart.ShoppingCartDto;
import org.example.bookstoreapp.dto.cartItem.CartItemDto;
import org.example.bookstoreapp.dto.cartItem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartItem.CartItemUpdateRequestDto;
import org.example.bookstoreapp.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartByUser();

    CartItemDto addBooksToShoppingCart(CartItemRequestDto requestDto);

    CartItemDto update(Long id, CartItemUpdateRequestDto requestDto);

    void delete(Long id);
}
