package ru.cheseve.easyproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cheseve.easyproject.dto.orderItem.OrderItemResponseDTO;
import ru.cheseve.easyproject.dto.orderItem.OrderItemsResponseDTO;
import ru.cheseve.easyproject.entity.Order;
import ru.cheseve.easyproject.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "name")
    OrderItemResponseDTO toResponse(OrderItem item);

    @Mapping(source = "id", target = "orderId")
    OrderItemsResponseDTO toOrderItemsResponseDTO(Order order);
}
