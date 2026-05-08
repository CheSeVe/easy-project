package ru.cheseve.easyproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cheseve.easyproject.dto.order.OrderRequestDTO;
import ru.cheseve.easyproject.dto.order.OrderResponseDTO;
import ru.cheseve.easyproject.dto.order.OrderWithCustomerIdResponseDTO;
import ru.cheseve.easyproject.dto.order.OrderWithCustomerResponseDTO;
import ru.cheseve.easyproject.dto.orderItem.OrderItemsResponseDTO;
import ru.cheseve.easyproject.entity.Order;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public interface OrderMapper {

    @Mapping(target = "customer", ignore = true)
    Order toEntity(OrderRequestDTO requestDTO);

    @Mapping(source = "customer.id", target = "customerId")
    OrderWithCustomerIdResponseDTO toResponseWithCustomerId(Order order);

    OrderWithCustomerResponseDTO toResponseWithCustomer(Order order);

    OrderResponseDTO toResponse(Order order);
}
