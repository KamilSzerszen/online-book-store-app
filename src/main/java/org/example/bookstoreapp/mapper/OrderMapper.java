package org.example.bookstoreapp.mapper;

import org.example.bookstoreapp.config.MapperConfig;
import org.example.bookstoreapp.dto.order.OrderResponseDto;
import org.example.bookstoreapp.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderMapper.class})
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponseDto toDto(Order order);
}
