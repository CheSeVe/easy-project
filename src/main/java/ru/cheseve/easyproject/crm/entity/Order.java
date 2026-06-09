package ru.cheseve.easyproject.crm.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.cheseve.easyproject.crm.dto.orderItem.OrderItemReplacement;
import ru.cheseve.easyproject.crm.enums.Status;
import ru.cheseve.easyproject.crm.exception.ItemNotPresentException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<OrderItem> items = new ArrayList<>();

    @Column(name = "items_count", nullable = false)
    private int itemsCount = 0;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public void setStatus(Status status) {
        validateStatus(status);
        this.status = status;
    }

    public void upsertItem(Product product, int quantity) {
        validateProduct(product);
        validateQuantity(quantity);

        findItemByProductId(product.getId())
                .ifPresentOrElse(
                        item -> item.setQuantity(quantity),
                        () -> {
                            items.add(new OrderItem(this, product, quantity, product.getPrice()));
                            itemsCount++;
                        }
                );
    }

    public void refreshItemsPriceFromProductPrice() {
        for (OrderItem item : items) {
            BigDecimal price = item.getProduct().getPrice();
            item.setUnitPrice(price);
        }
    }

    public void replaceItems(Map<Long, OrderItemReplacement> replacementByProductIds) {
        Set<Long> replacementProductIds = replacementByProductIds.keySet();

        Map<Long, OrderItem> orderItemsByProductIds = items.stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        Function.identity()
                ));

        orderItemsByProductIds.keySet().stream()
                .filter(productId -> !replacementProductIds.contains(productId))
                .forEach(this::removeItem);

        for (Long productId : replacementProductIds) {

            OrderItemReplacement replacement = replacementByProductIds.get(productId);
            upsertItem(replacement.product(), replacement.quantity());
        }

        refreshItemsPriceFromProductPrice();
    }

    public void removeItem(Long productId) {
        validateProductId(productId);

        OrderItem item = findItemByProductIdOrThrowException(productId);

        items.remove(item);
        item.detachFromOrder();
        itemsCount--;
    }

    private static void validateStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
    }

    private void validateProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("product must not be null and must have id");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
    }

    private Optional<OrderItem> findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }

    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId must not be null");
        }
    }

    private OrderItem findItemByProductIdOrThrowException(Long productId) {
        return findItemByProductId(productId)
                .orElseThrow(() -> new ItemNotPresentException(String.format(
                        "Product with id %d is not present in order", productId)
                ));
    }
}