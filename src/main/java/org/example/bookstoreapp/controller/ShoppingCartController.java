package org.example.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.ShoppingCart.ShoppingCartDto;
import org.example.bookstoreapp.dto.cartItem.CartItemDto;
import org.example.bookstoreapp.dto.cartItem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartItem.CartItemUpdateRequestDto;
import org.example.bookstoreapp.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return shoppingCartService.getShoppingCartByUser();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add book", description = "Add book to shopping cart")
    public CartItemDto addBooksToShoppingCart(@RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBooksToShoppingCart(requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update", description = "Update book quantity")
    public CartItemDto updateCartItem(
            @PathVariable Long id,
            @RequestBody CartItemUpdateRequestDto updateRequestDto
    ) {
        return shoppingCartService.update(id, updateRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Delete", description = "Delete books from shopping cart")
    public void deleteBooksFromShoppingCart(@PathVariable Long id) {
        shoppingCartService.delete(id);
    }
}
