package org.example.bookstoreapp.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookstoreapp.model.Status;

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
