package org.example.bookstoreapp.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.cartitem.CartItemDto;
import org.example.bookstoreapp.dto.cartitem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartitem.CartItemUpdateRequestDto;
import org.example.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.CartItemMapper;
import org.example.bookstoreapp.mapper.ShoppingCartMapper;
import org.example.bookstoreapp.model.Book;
import org.example.bookstoreapp.model.CartItem;
import org.example.bookstoreapp.model.ShoppingCart;
import org.example.bookstoreapp.model.User;
import org.example.bookstoreapp.repository.book.BookRepository;
import org.example.bookstoreapp.repository.cartitem.CartItemRepository;
import org.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import org.example.bookstoreapp.service.ShoppingCartService;
import org.example.bookstoreapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCartByUserId() {
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for user: " + currentUser.getEmail() + "not found"));

        shoppingCart.getCartItems().forEach(cartItem -> {
            cartItem.getBook().getId();
            cartItem.getBook().getTitle();
        });

        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto addItemToCart(CartItemRequestDto requestDto) {
        User currentUser = userService.getCurrentUser();
        Optional<ShoppingCart> byUser = shoppingCartRepository.findByUserId(currentUser.getId());
        ShoppingCart shoppingCart = byUser.orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(currentUser);
            return shoppingCartRepository.save(newCart);
        });

        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Book with id " + requestDto.bookId() + " not found"
                        ));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(i -> i.getBook().getId().equals(book.getId()))
                .findFirst().orElseGet(() -> {
                    CartItem newCart = new CartItem();
                    newCart.setShoppingCart(shoppingCart);
                    newCart.setBook(book);
                    return newCart;
                });

        cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        CartItem saved = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CartItemDto updateItemQuantity(Long id, CartItemUpdateRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item with id " + id + " not found"
                ));

        cartItem.setQuantity(requestDto.quantity());
        CartItem saved = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteItemFromCart(Long id) {
        cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cart item with id " + id + " not found")
        );
        cartItemRepository.deleteById(id);
    }
}
