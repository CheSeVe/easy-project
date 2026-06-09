package ru.cheseve.easyproject.crm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.crm.dto.order.*;
import ru.cheseve.easyproject.dto.PageResponse;
import ru.cheseve.easyproject.crm.entity.Customer;
import ru.cheseve.easyproject.crm.entity.Order;
import ru.cheseve.easyproject.crm.exception.CustomerNotFoundException;
import ru.cheseve.easyproject.crm.exception.OrderNotFoundException;
import ru.cheseve.easyproject.crm.mapper.OrderMapper;
import ru.cheseve.easyproject.crm.repository.CustomerRepository;
import ru.cheseve.easyproject.crm.repository.OrderRepository;
import ru.cheseve.easyproject.crm.specification.OrderSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public OrderWithCustomerResponse getOrderWithCustomer(Long id) {
        log.debug("getOrderWithCustomer id={}", id);
        Order order = getOrderWithCustomerOrThrowException(id);

        return orderMapper.toResponseWithCustomer(order);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderWithCustomerIdResponse> getAllOrders(
            OrderFilterDTO filter,
            Pageable pageable) {
        log.debug("getAllOrders page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<OrderWithCustomerIdResponse> orderPage = orderRepository
                .findAll(OrderSpecifications.withFilter(filter), pageable)
                .map(orderMapper::toResponseWithCustomerId);

        return PageResponse.from(orderPage);
    }

    @Transactional
    public OrderWithCustomerIdResponse addOrder(OrderRequest request) {
        log.debug("addOrder started");
        Customer customer = getCustomerOrThrowException(request.customerId());

        Order order = orderMapper.toEntity(request);
        order.setCustomer(customer);

        Order savedOrder = orderRepository.save(order);
        log.debug("addOrder - created id={}", savedOrder.getId());
        return orderMapper.toResponseWithCustomerId(savedOrder);
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long id, OrderStatusRequest request) {
        log.debug("changeOrderStatus id={}, status={}", id, request.status());
        Order order = getOrderOrThrowException(id);
        order.setStatus(request.status());

        return orderMapper.toResponse(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        log.debug("deleteOrder id={}", id);
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
