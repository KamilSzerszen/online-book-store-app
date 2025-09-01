package org.example.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.order.OrderItemDto;
import org.example.bookstoreapp.dto.order.OrderPlaceRequestDto;
import org.example.bookstoreapp.dto.order.OrderResponseDto;
import org.example.bookstoreapp.dto.order.UpdateOrderStatusDto;
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
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update status", description = "Update order status")
    public OrderResponseDto updateStatus(
            @RequestBody @Valid UpdateOrderStatusDto requestDto,
            @PathVariable Long orderId
    ) {
        return orderService.updateStatus(requestDto, orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get orders items", description = "Get all items from orders")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get orders item", description = "Get single item from order")
    public OrderItemDto getOrderSingleItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderSingleItem(orderId, itemId);
    }
}
