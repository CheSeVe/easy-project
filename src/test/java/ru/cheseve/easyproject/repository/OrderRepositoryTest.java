package ru.cheseve.easyproject.repository;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.TestContainersConfig;
import ru.cheseve.easyproject.crm.entity.Order;
import ru.cheseve.easyproject.crm.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ImportTestcontainers(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = "/sql/order-repository-test-data.sql",
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void detachCustomerFromOrders_Detaches() {
        //when
        orderRepository.detachCustomerFromOrders(2L);
        Order updated = orderRepository.findById(2L).orElseThrow();
        //then
        assertNull(updated.getCustomer());
    }

    @Test
    void findWithCustomerById_ReturnsEagerCustomer() {
        //when
        Order result = orderRepository.findWithCustomerById(1L).orElseThrow();
        //then
        assertEquals("Иван", result.getCustomer().getName());
        assertFalse(Hibernate.isInitialized(result.getItems()));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void findWithItemsById_ReturnsEagerItems() {
        //when
        var result = orderRepository.findWithItemsById(1L).orElseThrow();
        //then
        assertEquals(1, result.getItems().get(0).getQuantity());
        assertFalse(Hibernate.isInitialized(result.getCustomer()));
        assertFalse(Hibernate.isInitialized(result.getItems().get(0).getProduct()));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void findWithItemsAndProductsById_ReturnsEagerItems() {
        //when
        var result = orderRepository.findWithItemsAndProductsById(1L).orElseThrow();
        //then
        assertEquals(1, result.getItems().get(0).getQuantity());
        assertEquals("Ноутбук", result.getItems().get(0).getProduct().getName());
        assertFalse(Hibernate.isInitialized(result.getCustomer()));
    }
}
