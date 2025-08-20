package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.order.OrderPlaceRequestDto;
import org.example.bookstoreapp.dto.order.OrderResponseDto;

public interface OrderService {

    OrderResponseDto submitOrder(OrderPlaceRequestDto requestDto);
}
