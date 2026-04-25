package ru.cheseve.easyproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.dto.PageResponseDTO;
import ru.cheseve.easyproject.dto.order.*;
import ru.cheseve.easyproject.entity.Customer;
import ru.cheseve.easyproject.entity.Order;
import ru.cheseve.easyproject.mapper.OrderMapper;
import ru.cheseve.easyproject.repository.CustomerRepository;
import ru.cheseve.easyproject.repository.OrderRepository;
import ru.cheseve.easyproject.specification.OrderSpecifications;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper mapper;
    CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public OrderWithCustomerResponseDTO getOrderWithCustomer(Long id) {
        Order order = getOrderWithCustomerOrThrowException(id);

        return mapper.toResponseWithCustomer(order);
    }

    private Order getOrderWithCustomerOrThrowException(Long id) {
        return orderRepository.findWithCustomerById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with id %d does not exist", id)));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<OrderWithCustomerIdResponseDTO> getAllOrders(
            OrderFilterDTO filter,
            Pageable pageable) {
        Page<OrderWithCustomerIdResponseDTO> orderPage = orderRepository
                .findAll(OrderSpecifications.withFilter(filter), pageable)
                .map(mapper::toResponseWithCustomerId);

        return PageResponseDTO.from(orderPage);
    }

    private Order getOrderOrThrowException(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with id %d does not exist", id)));
    }

    @Transactional
    public OrderWithCustomerIdResponseDTO addOrder(OrderRequestDTO requestDTO) {
        Customer customer = getCustomerOrThrowException(requestDTO.customerId());

        Order order = mapper.toEntity(requestDTO);
        order.setCustomer(customer);
        order.setCreatedAt(Instant.now());

        Order savedOrder = orderRepository.save(order);

        return mapper.toResponseWithCustomerId(savedOrder);
    }

    private Customer getCustomerOrThrowException(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Customer with id %d does not exist", id)));
    }

    @Transactional
    public OrderResponseDTO changeOrderStatus(Long id, OrderStatusRequestDTO requestDTO) {
        Order order = getOrderOrThrowException(id);
        order.setStatus(requestDTO.status());

        return mapper.toResponse(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderOrThrowException(id);
        orderRepository.delete(order);
    }


}
