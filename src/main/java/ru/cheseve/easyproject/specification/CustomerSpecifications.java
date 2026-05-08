package ru.cheseve.easyproject.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.cheseve.easyproject.dto.customer.CustomerFilterDTO;
import ru.cheseve.easyproject.entity.Customer;

import java.util.Locale;

public final class CustomerSpecifications {
    private CustomerSpecifications() {
    }

    public static Specification<Customer> hasName(String name) {
        return name == null || name.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Customer> hasSurname(String surname) {
        return surname == null || surname.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Customer> hasEmail(String email) {
        return email == null || email.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Customer> hasPhoneNumber(String phoneNumber) {
        return phoneNumber == null || phoneNumber.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("phoneNumber")), "%" + phoneNumber.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Customer> withFilter(CustomerFilterDTO filter) {
        if (filter == null) {
            return Specification.unrestricted();
        }

        return Specification
                .where(CustomerSpecifications.hasName(filter.name()))
                .and(CustomerSpecifications.hasSurname(filter.surname()))
                .and(CustomerSpecifications.hasEmail(filter.email()))
                .and(CustomerSpecifications.hasPhoneNumber(filter.phoneNumber()));
    }
}
