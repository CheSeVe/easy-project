package ru.cheseve.easyproject.service;

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
import ru.cheseve.easyproject.exception.CustomerNotFoundException;
import ru.cheseve.easyproject.exception.OrderNotFoundException;
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
    OrderMapper orderMapper;
    CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public OrderWithCustomerResponseDTO getOrderWithCustomer(Long id) {
        Order order = getOrderWithCustomerOrThrowException(id);

        return orderMapper.toResponseWithCustomer(order);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<OrderWithCustomerIdResponseDTO> getAllOrders(
            OrderFilterDTO filter,
            Pageable pageable) {
        Page<OrderWithCustomerIdResponseDTO> orderPage = orderRepository
                .findAll(OrderSpecifications.withFilter(filter), pageable)
                .map(orderMapper::toResponseWithCustomerId);

        return PageResponseDTO.from(orderPage);
    }

    @Transactional
    public OrderWithCustomerIdResponseDTO addOrder(OrderRequestDTO requestDTO) {
        Customer customer = getCustomerOrThrowException(requestDTO.customerId());

        Order order = orderMapper.toEntity(requestDTO);
        order.setCustomer(customer);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseWithCustomerId(savedOrder);
    }

    @Transactional
    public OrderResponseDTO changeOrderStatus(Long id, OrderStatusRequestDTO requestDTO) {
        Order order = getOrderOrThrowException(id);
        order.setStatus(requestDTO.status());

        return orderMapper.toResponse(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderOrThrowException(id);
        orderRepository.delete(order);
    }

    private Order getOrderWithCustomerOrThrowException(Long id) {
        return orderRepository.findWithCustomerById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format("Order with id %d does not exist", id
                        )));
    }

    private Order getOrderOrThrowException(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format("Order with id %d does not exist", id)));
    }

    private Customer getCustomerOrThrowException(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer with id %d does not exist", id)));
    }
}
