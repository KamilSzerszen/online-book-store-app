package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.order.OrderItemDto;
import org.example.bookstoreapp.dto.order.OrderPlaceRequestDto;
import org.example.bookstoreapp.dto.order.OrderResponseDto;
import org.example.bookstoreapp.dto.order.UpdateOrderStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {

    OrderResponseDto submitOrder(OrderPlaceRequestDto requestDto);

    Page<OrderResponseDto> getOrders(Pageable pageable);

    OrderResponseDto updateStatus(UpdateOrderStatusDto requestDto, Long orderId);

    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderSingleItem(Long orderId, Long itemId);
}
