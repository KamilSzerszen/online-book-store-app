package org.example.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.order.OrderItemDto;
import org.example.bookstoreapp.dto.order.OrderPlaceRequestDto;
import org.example.bookstoreapp.dto.order.OrderResponseDto;
import org.example.bookstoreapp.dto.order.UpdateOrderStatusDto;
import org.example.bookstoreapp.exception.AccessDeniedException;
import org.example.bookstoreapp.exception.EntityNotFoundException;
import org.example.bookstoreapp.mapper.OrderItemMapper;
import org.example.bookstoreapp.mapper.OrderMapper;
import org.example.bookstoreapp.model.CartItem;
import org.example.bookstoreapp.model.Order;
import org.example.bookstoreapp.model.OrderItem;
import org.example.bookstoreapp.model.ShoppingCart;
import org.example.bookstoreapp.model.Status;
import org.example.bookstoreapp.model.User;
import org.example.bookstoreapp.repository.order.OrderRepository;
import org.example.bookstoreapp.repository.shoppingCart.ShoppingCartRepository;
import org.example.bookstoreapp.service.OrderService;
import org.example.bookstoreapp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto submitOrder(OrderPlaceRequestDto requestDto) {
        User currentUser = getCurrentUser();
        ShoppingCart shoppingCart = getShoppingCart();

        Order newOrder = new Order();
        newOrder.setUser(currentUser);
        newOrder.setStatus(Status.PENDING);
        newOrder.setTotal(getOrderAmount(shoppingCart));
        newOrder.setOrderDate(getOrderDate());
        newOrder.setShippingAddress(requestDto.getShippingAddress());
        newOrder.setOrderItems(getOrderItems(newOrder, shoppingCart));
        Order saved = orderRepository.save(newOrder);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrders(Pageable pageable) {
        Long userId = getCurrentUser().getId();

        return orderRepository.findAllByUser_Id(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto updateStatus(UpdateOrderStatusDto requestDto, Long id) {
        Order orderById = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order with id: " + id + " not found")
        );

        orderById.setStatus(requestDto.getStatus());
        Order saved = orderRepository.save(orderById);

        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> getOrderItems(Long orderId) {
        User currentUser = getCurrentUser();

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with orderId: " + orderId + " not found")
        );

        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDto getOrderSingleItem(Long orderId, Long itemId) {
        User currentUser = getCurrentUser();

        Order orderById = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with id: " + orderId + " not found")
        );

        if (!orderById.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }

        Optional<OrderItem> orderItemById = orderById.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        OrderItem orderItem = orderItemById.orElseThrow(
                () -> new EntityNotFoundException(
                        "Order item with id: " + itemId + " not found"
                )
        );

        return orderItemMapper.toDto(orderItem);
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

    private BigDecimal getOrderAmount(ShoppingCart shoppingCart) {
        BigDecimal orderAmount = BigDecimal.ZERO;
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem ci : cartItems) {
            BigDecimal price = ci.getBook().getPrice();
            orderAmount = orderAmount.add(price.multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        return orderAmount;
    }

    private List<OrderItem> getOrderItems(Order order, ShoppingCart shoppingCart) {
        List<OrderItem> orderItems = new ArrayList<>();
        shoppingCart.getCartItems().stream().forEach(ci -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(ci.getBook());
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setPrice(ci.getBook().getPrice().multiply(
                    BigDecimal.valueOf(ci.getQuantity())
            ));
            orderItems.add(orderItem);
        });

        order.setOrderItems(orderItems);
        return orderItems;
    }
}
