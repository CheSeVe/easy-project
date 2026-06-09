package ru.cheseve.easyproject.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cheseve.easyproject.crm.dto.customer.CustomerRequest;
import ru.cheseve.easyproject.crm.dto.customer.CustomerResponse;
import ru.cheseve.easyproject.crm.dto.customer.CustomerWithOrdersResponse;
import ru.cheseve.easyproject.crm.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequest requestDTO);
    CustomerWithOrdersResponse toResponseWithOrders(Customer customer);
    CustomerResponse toResponse(Customer customer);
    void updateCustomerFromRequest(CustomerRequest requestDTO, @MappingTarget Customer customer);
}
