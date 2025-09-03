package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstoreapp.dto.cartitem.CartItemDto;
import org.example.bookstoreapp.dto.cartitem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartitem.CartItemUpdateRequestDto;
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
import org.example.bookstoreapp.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("""
            Should return shopping cart for current user
            """)
    public void getShoppingCartByUserId_userExists_returnsShoppingCart() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@user.com");

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        ShoppingCartDto cartDto = new ShoppingCartDto();

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        Mockito.when(shoppingCartMapper.toShoppingCartDto(cart)).thenReturn(cartDto);

        ShoppingCartDto result = shoppingCartService.getShoppingCartByUserId();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cartDto, result);
        Mockito.verify(shoppingCartRepository).findByUserId(user.getId());
    }

    @Test
    @DisplayName("""
            Should throw exception when shopping cart not found for current user
            """)
    public void getShoppingCartByUserId_cartNotFound_throwsException() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@user.com");

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCartByUserId()
        );

        Assertions.assertEquals(
                "Shopping cart for user: " + user.getEmail() + "not found",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("""
            Should add new item to shopping cart
            """)
    public void addItemToCart_validRequest_returnsCartItemDto() {
        User user = new User();
        user.setId(1L);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        Book book = new Book();
        book.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(book);
        cartItem.setShoppingCart(cart);
        cartItem.setQuantity(2);

        CartItemRequestDto requestDto = new CartItemRequestDto(1L, 2);
        CartItemDto cartItemDto = new CartItemDto(cartItem.getId(),book.getId(), book.getTitle(), cartItem.getQuantity());

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(cartItemRepository.save(Mockito.any(CartItem.class))).thenReturn(cartItem);
        Mockito.when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);

        CartItemDto result = shoppingCartService.addItemToCart(requestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cartItemDto.id(), result.id());
        Assertions.assertEquals(cartItemDto.quantity(), result.quantity());
    }

    @Test
    @DisplayName("""
            Should throw exception when adding item with invalid book id
            """)
    public void addItemToCart_invalidBookId_throwsException() {
        User user = new User();
        user.setId(1L);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        CartItemRequestDto requestDto = new CartItemRequestDto(999L, 2);

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        Mockito.when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.addItemToCart(requestDto)
        );

        Assertions.assertEquals(
                "Book with id " + requestDto.bookId() + " not found",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("""
            Should update cart item quantity
            """)
    public void updateItemQuantity_itemExists_returnsCartItemDto() {
        Book book = new Book();
        book.setId(10L);
        book.setTitle("Sample Book");

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        CartItemUpdateRequestDto requestDto = new CartItemUpdateRequestDto(5);

        CartItemDto cartItemDto = new CartItemDto(
                cartItem.getId(),
                book.getId(),
                book.getTitle(),
                requestDto.quantity()
        );

        Mockito.when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);

        CartItemDto result = shoppingCartService.updateItemQuantity(cartItem.getId(), requestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cartItemDto.id(), result.id());
        Assertions.assertEquals(cartItemDto.bookId(), result.bookId());
        Assertions.assertEquals(cartItemDto.bookTitle(), result.bookTitle());
        Assertions.assertEquals(cartItemDto.quantity(), result.quantity());
    }

    @Test
    @DisplayName("""
            Should throw exception when updating non-existing cart item
            """)
    public void updateItemQuantity_itemDoesNotExist_throwsException() {
        Long invalidId = 999L;
        CartItemUpdateRequestDto requestDto = new CartItemUpdateRequestDto(5);

        Mockito.when(cartItemRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.updateItemQuantity(invalidId, requestDto)
        );

        Assertions.assertEquals(
                "Cart item with id " + invalidId + " not found",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("""
            Should delete cart item by id
            """)
    public void deleteItemFromCart_itemExists_deletesItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        Mockito.when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));

        shoppingCartService.deleteItemFromCart(cartItem.getId());

        Mockito.verify(cartItemRepository).deleteById(cartItem.getId());
    }

    @Test
    @DisplayName("""
            Should throw exception when deleting non-existing cart item
            """)
    public void deleteItemFromCart_itemDoesNotExist_throwsException() {
        Long invalidId = 999L;

        Mockito.when(cartItemRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.deleteItemFromCart(invalidId)
        );

        Assertions.assertEquals(
                "Cart item with id " + invalidId + " not found",
                exception.getMessage()
        );
    }
}
