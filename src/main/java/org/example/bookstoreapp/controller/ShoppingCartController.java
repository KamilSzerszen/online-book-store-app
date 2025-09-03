package org.example.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.cartitem.CartItemDto;
import org.example.bookstoreapp.dto.cartitem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartitem.CartItemUpdateRequestDto;
import org.example.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstoreapp.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart controller", description = "Endpoint for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get shopping cart", description = "Get all products from shopping cart")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCartByUserId();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add book", description = "Add book to shopping cart")
    public CartItemDto addBooksToShoppingCart(@RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addItemToCart(requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update", description = "Update book quantity")
    public CartItemDto updateCartItem(
            @PathVariable Long id,
            @RequestBody CartItemUpdateRequestDto updateRequestDto
    ) {
        return shoppingCartService.updateItemQuantity(id, updateRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Delete", description = "Delete books from shopping cart")
    public void deleteBooksFromShoppingCart(@PathVariable Long id) {
        shoppingCartService.deleteItemFromCart(id);
    }
}
