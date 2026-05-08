package ru.cheseve.easyproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cheseve.easyproject.dto.customer.CustomerRequestDTO;
import ru.cheseve.easyproject.dto.customer.CustomerResponseDTO;
import ru.cheseve.easyproject.dto.customer.CustomerWithOrdersResponseDTO;
import ru.cheseve.easyproject.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDTO requestDTO);
    CustomerWithOrdersResponseDTO toResponseWithOrders(Customer customer);
    CustomerResponseDTO toResponse(Customer customer);
    void updateCustomerFromRequest(CustomerRequestDTO requestDTO, @MappingTarget Customer customer);
}
