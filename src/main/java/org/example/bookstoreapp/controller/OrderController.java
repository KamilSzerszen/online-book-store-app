package org.example.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.order.OrderItemDto;
import org.example.bookstoreapp.dto.order.OrderPlaceRequestDto;
import org.example.bookstoreapp.dto.order.OrderResponseDto;
import org.example.bookstoreapp.dto.order.OrderUpdateRequestDto;
import org.example.bookstoreapp.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders controller", description = "Endpoint for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Submit order", description = "Submit user order")
    public OrderResponseDto submitOrder(@RequestBody OrderPlaceRequestDto requestDto) {
        return orderService.submitOrder(requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get all user orders")
    public Page<OrderResponseDto> getOrders(Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update status", description = "Update order status")
    public OrderResponseDto updateStatus(
            @RequestBody @Valid OrderUpdateRequestDto requestDto,
            @PathVariable Long id
    ) {
        return orderService.updateStatus(requestDto, id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/items")
    @Operation(summary = "Get orders items", description = "Get all items from orders")
    public List<OrderItemDto> getOrderItems(@PathVariable Long id) {
        return orderService.getOrderItems(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get orders item", description = "Get single item from order")
    public OrderItemDto getOrderSingleItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderSingleItem(orderId, itemId);
    }
}
