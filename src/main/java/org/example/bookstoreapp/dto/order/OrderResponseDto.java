package org.example.bookstoreapp.dto.order;

import org.example.bookstoreapp.model.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Status status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Set<OrderItemDto> orderItems;
}
