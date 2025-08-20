package org.example.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.order.OrderPlaceRequestDto;
import org.example.bookstoreapp.dto.order.OrderResponseDto;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.OrderMapper;
import org.example.bookstoreapp.model.CartItem;
import org.example.bookstoreapp.model.Order;
import org.example.bookstoreapp.model.OrderItem;
import org.example.bookstoreapp.model.ShoppingCart;
import org.example.bookstoreapp.model.Status;
import org.example.bookstoreapp.model.User;
import org.example.bookstoreapp.repository.order.OrderRepository;
import org.example.bookstoreapp.repository.orderItem.OrderItemRepository;
import org.example.bookstoreapp.repository.shoppingCart.ShoppingCartRepository;
import org.example.bookstoreapp.service.OrderService;
import org.example.bookstoreapp.service.UserService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto submitOrder(OrderPlaceRequestDto requestDto) {

        Order newOrder = new Order();
        newOrder.setUser(getCurrentUser());
        newOrder.setStatus(Status.PENDING);
        newOrder.setTotal(getOrderAmount());
        newOrder.setOrderDate(getOrderDate());
        newOrder.setShippingAddress(requestDto.shippingAddress());
        newOrder.setOrderItems(getOrderItem(newOrder));
        Order saved = orderRepository.save(newOrder);

        return orderMapper.toDto(saved);
    }

    private ShoppingCart getShoppingCart() {
        User currentUser = userService.getCurrentUser();
        return shoppingCartRepository.findById(currentUser.getId()).
                orElseThrow(() -> new EntityNotFoundException(
                        "User" + currentUser.getEmail() + "not found"
                ));
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private LocalDateTime getOrderDate() {
        return LocalDateTime.now();
    }

    private BigDecimal getOrderAmount() {
        BigDecimal orderAmount = BigDecimal.ZERO;
        Set<CartItem> cartItems = getShoppingCart().getCartItems();
        for (CartItem ci : cartItems) {
            BigDecimal price = ci.getBook().getPrice();
            orderAmount = orderAmount.add(price.multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        return orderAmount;
    }

    private Set<OrderItem> getOrderItem(Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        getShoppingCart().getCartItems().stream().forEach(ci -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(ci.getBook());
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setPrice(ci.getBook().getPrice().multiply(
                    BigDecimal.valueOf(ci.getQuantity())
            ));
            OrderItem saved = orderItemRepository.save(orderItem);
            orderItems.add(saved);
        });

        return orderItems;
    }
}
