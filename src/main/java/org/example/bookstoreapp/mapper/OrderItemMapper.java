package org.example.bookstoreapp.mapper;

import org.example.bookstoreapp.config.MapperConfig;
import org.example.bookstoreapp.dto.order.OrderItemDto;
import org.example.bookstoreapp.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}

