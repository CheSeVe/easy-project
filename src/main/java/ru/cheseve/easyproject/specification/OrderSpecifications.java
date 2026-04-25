package ru.cheseve.easyproject.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.cheseve.easyproject.dto.order.OrderFilterDTO;
import ru.cheseve.easyproject.entity.Order;
import ru.cheseve.easyproject.enums.Status;

import java.time.Instant;

public final class OrderSpecifications {
    private OrderSpecifications() {}

    public static Specification<Order> hasStatus(Status status) {
        return status == null
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Order> createdFrom(Instant from) {
        return from == null
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<Order> createdTo(Instant to) {
        return to == null
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<Order> withFilter(OrderFilterDTO filter) {
        if (filter == null) {
            return Specification.unrestricted();
        }

        return Specification
                .where(hasStatus(filter.status()))
                .and(createdFrom(filter.createdFrom()))
                .and(createdTo(filter.createdTo()));
    }
}
