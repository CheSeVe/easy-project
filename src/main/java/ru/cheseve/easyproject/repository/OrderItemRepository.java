package ru.cheseve.easyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cheseve.easyproject.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProduct_Id(Long productId);
}
