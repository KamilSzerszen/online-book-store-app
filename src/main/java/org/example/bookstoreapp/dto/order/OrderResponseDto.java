package org.example.bookstoreapp.dto.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookstoreapp.model.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Status status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private List<OrderItemDto> orderItems;
}
