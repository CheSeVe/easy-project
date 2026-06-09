package ru.cheseve.easyproject.crm.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.cheseve.easyproject.crm.entity.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Modifying
    @Query("""
            update Order o
            set o.customer = null
            where o.customer.id = :customerId
            """)
    void detachCustomerFromOrders(@Param("customerId") Long customerId);

    @EntityGraph(attributePaths = "customer")
    Optional<Order> findWithCustomerById(Long id);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findWithItemsAndProductsById(Long id);

    @EntityGraph(attributePaths = "items")
    Optional<Order> findWithItemsById(Long id);
}
