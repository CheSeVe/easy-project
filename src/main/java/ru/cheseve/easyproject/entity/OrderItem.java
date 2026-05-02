package ru.cheseve.easyproject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    BigDecimal unitPrice;

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal unitPrice) {
        validateOrder(order);
        validateProduct(product);
        validateQuantity(quantity);
        validateUnitPrice(unitPrice);

        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public void setQuantity(Integer quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        validateUnitPrice(unitPrice);
        this.unitPrice = unitPrice;
    }

    protected void detachFromOrder() {
        this.order = null;
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
    }

    private void validateProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("product must not be null and must have id");
        }
    }

    private void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("unit price must be >= 0.00");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("quantity must be not null and > 0");
        }
    }
}
