package org.example.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.order.OrderPlaceRequestDto;
import org.example.bookstoreapp.dto.order.OrderResponseDto;
import org.example.bookstoreapp.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders controller", description = "Endpoint for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Submit order", description = "Submit user order")
    public OrderResponseDto submitOrder(OrderPlaceRequestDto requestDto) {
        return orderService.submitOrder(requestDto);
    }
}
