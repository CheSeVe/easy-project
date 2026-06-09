package ru.cheseve.easyproject.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cheseve.easyproject.crm.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProduct_Id(Long productId);
}
