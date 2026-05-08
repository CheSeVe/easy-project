package ru.cheseve.easyproject.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.cheseve.easyproject.dto.product.ProductFilterDTO;
import ru.cheseve.easyproject.entity.Product;

import java.math.BigDecimal;
import java.util.Locale;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> hasName(String name) {
        return name == null || name.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Product> priceFrom(BigDecimal from) {
        return from == null
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("price"), from);
    }

    public static Specification<Product> priceTo(BigDecimal to) {
        return to == null
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("price"), to);
    }

    public static Specification<Product> withFilter(ProductFilterDTO filter) {
        if (filter == null) {
            return Specification.unrestricted();
        }

        return Specification
                .where(hasName(filter.name()))
                .and(priceFrom(filter.priceFrom()))
                .and(priceTo(filter.priceTo()));
    }
}
