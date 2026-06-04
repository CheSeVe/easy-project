package ru.cheseve.easyproject.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import ru.cheseve.easyproject.dto.customer.CustomerResponseDTO;
import ru.cheseve.easyproject.dto.order.*;
import ru.cheseve.easyproject.entity.Customer;
import ru.cheseve.easyproject.entity.Order;
import ru.cheseve.easyproject.enums.Status;
import ru.cheseve.easyproject.exception.CustomerNotFoundException;
import ru.cheseve.easyproject.exception.OrderNotFoundException;
import ru.cheseve.easyproject.mapper.OrderMapper;
import ru.cheseve.easyproject.repository.CustomerRepository;
import ru.cheseve.easyproject.repository.OrderRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    OrderMapper orderMapper;
    @InjectMocks
    OrderService service;

    Customer customer;
    Order order;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(2L)
                .name("Иван")
                .surname("Иванов")
                .email("ivanovivan@test.ru")
                .phoneNumber("+79991234567")
                .build();

        order = Order.builder()
                .id(1L)
                .status(Status.NEW)
                .createdAt(Instant.parse("2026-01-01T00:00:00Z"))
                .customer(customer)
                .build();
    }

    @Test
    void getOrderWithCustomer_ExistingId_ReturnsOrderWithCustomerResponseDTO() {
        var expectedResponse = new OrderWithCustomerResponseDTO(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt(),
                new CustomerResponseDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getSurname(),
                        customer.getEmail(),
                        customer.getPhoneNumber()
                )
        );
        doReturn(Optional.of(order)).when(orderRepository).findWithCustomerById(1L);
        doReturn(expectedResponse).when(orderMapper).toResponseWithCustomer(order);

        var result = service.getOrderWithCustomer(1L);

        assertEquals(expectedResponse, result);
        verify(orderRepository).findWithCustomerById(1L);
        verify(orderMapper).toResponseWithCustomer(order);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    void getOrderWithCustomer_NotExistingId_ThrowsNotFound() {

        Long id = 99L;
        doReturn(Optional.empty()).when(orderRepository).findWithCustomerById(id);

        var exception = assertThrows(OrderNotFoundException.class, () -> service.getOrderWithCustomer(id));
        assertEquals("Order with id 99 does not exist", exception.getMessage());
        verify(orderRepository).findWithCustomerById(id);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void getAllOrders_ReturnsPageResponseDTO() {
        var filter = new OrderFilterDTO(null, null, null, null);
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(List.of(order), pageable, 1);
        var expectedResponse = new OrderWithCustomerIdResponseDTO(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt(),
                null);

        doReturn(page).when(orderRepository).findAll(any(Specification.class), eq(pageable));
        doReturn(expectedResponse).when(orderMapper).toResponseWithCustomerId(order);

        var result = service.getAllOrders(filter, pageable);

        assertEquals(1, result.content().size());
        assertEquals(expectedResponse, result.content().getFirst());
        verify(orderRepository).findAll(any(Specification.class), eq(pageable));
        verify(orderMapper).toResponseWithCustomerId(order);
        verifyNoMoreInteractions(orderMapper, orderRepository);
    }

    @Test
    void addOrder_ExistingCustomerId_ReturnsResponse() {
        var requestDTO = new OrderRequestDTO(Status.NEW, 2L);
        var expectedResponse = new OrderWithCustomerIdResponseDTO(
                1L,
                Status.NEW,
                order.getCreatedAt(),
                2L);
        doReturn(Optional.of(customer)).when(customerRepository).findById(2L);
        doReturn(order).when(orderMapper).toEntity(requestDTO);
        doReturn(order)
                .when(orderRepository)
                .save(argThat(o -> o.getCustomer().equals(customer)));
        doReturn(expectedResponse).when(orderMapper).toResponseWithCustomerId(order);

        var result = service.addOrder(requestDTO);

        assertEquals(expectedResponse, result);
        verify(customerRepository).findById(2L);
        verify(orderMapper).toEntity(requestDTO);
        verify(orderRepository).save(argThat(o -> o.getCustomer().equals(customer)));
        verify(orderMapper).toResponseWithCustomerId(order);
        verifyNoMoreInteractions(customerRepository, orderMapper, orderRepository);
    }

    @Test
    void addOrder_NotExistingCustomerId_ThrowsNotFound() {
        var requestDTO = new OrderRequestDTO(Status.NEW, 2L);
        doReturn(Optional.empty()).when(customerRepository).findById(2L);

        var exception = assertThrows(CustomerNotFoundException.class, () -> service.addOrder(requestDTO));
        assertEquals("Customer with id 2 does not exist", exception.getMessage());
        verify(customerRepository).findById(2L);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(orderMapper, orderRepository);
    }

    @Test
    void changeOrderStatus_ExistingId_ReturnsResponse() {
        var requestDTO = new OrderStatusRequestDTO(Status.CANCELED);
        var expectedResponse = new OrderResponseDTO(order.getId(), Status.CANCELED, order.getCreatedAt());
        doReturn(Optional.of(order)).when(orderRepository).findById(1L);
        doReturn(expectedResponse)
                .when(orderMapper).toResponse(argThat(o -> o.getStatus() == Status.CANCELED));

        var result = service.changeOrderStatus(1L, requestDTO);

        assertEquals(expectedResponse, result);
        verify(orderRepository).findById(1L);
        verify(orderMapper).toResponse(argThat(o -> o.getStatus() == Status.CANCELED));
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    void changeOrderStatus_NotExistingId_ThrowsNotFound() {
        var requestDTO = new OrderStatusRequestDTO(Status.CANCELED);
        doReturn(Optional.empty()).when(orderRepository).findById(1L);

        var exception = assertThrows(OrderNotFoundException.class,() -> service.changeOrderStatus(1L, requestDTO));
        assertEquals("Order with id 1 does not exist", exception.getMessage());
        verify(orderRepository).findById(1L);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(orderMapper);
    }

    @Test
    void deleteOrder_ExistingId_Deletes() {
        doReturn(Optional.of(order)).when(orderRepository).findById(1L);

        service.deleteOrder(1L);

        verify(orderRepository).findById(1L);
        verify(orderRepository).delete(order);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(customerRepository, orderMapper);
    }

    @Test
    void deleteOrder_NotExistingId_ThrowsNotFound() {
        doReturn(Optional.empty()).when(orderRepository).findById(1L);

        var exception = assertThrows(OrderNotFoundException.class, () -> service.deleteOrder(1L));
        assertEquals("Order with id 1 does not exist", exception.getMessage());
        verify(orderRepository).findById(1L);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(customerRepository, orderMapper);
    }
}
