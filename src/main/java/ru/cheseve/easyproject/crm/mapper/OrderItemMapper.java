package ru.cheseve.easyproject.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cheseve.easyproject.crm.dto.orderItem.OrderItemResponse;
import ru.cheseve.easyproject.crm.dto.orderItem.OrderItemsResponse;
import ru.cheseve.easyproject.crm.entity.Order;
import ru.cheseve.easyproject.crm.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "name")
    OrderItemResponse toResponse(OrderItem item);

    @Mapping(source = "id", target = "orderId")
    OrderItemsResponse toOrderItemsResponse(Order order);
}
