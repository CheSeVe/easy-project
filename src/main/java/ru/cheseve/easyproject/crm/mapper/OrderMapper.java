package ru.cheseve.easyproject.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cheseve.easyproject.crm.dto.order.*;
import ru.cheseve.easyproject.crm.entity.Order;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public interface OrderMapper {

    @Mapping(target = "customer", ignore = true)
    Order toEntity(OrderRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    OrderWithCustomerIdResponse toResponseWithCustomerId(Order order);

    OrderWithCustomerResponse toResponseWithCustomer(Order order);

    OrderResponse toResponse(Order order);
}
