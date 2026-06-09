package ru.cheseve.easyproject.crm.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.cheseve.easyproject.crm.dto.employee.EmployeeFilterDTO;
import ru.cheseve.easyproject.crm.entity.Employee;
import ru.cheseve.easyproject.crm.enums.Role;

import java.util.Locale;

public final class EmployeeSpecifications {
    private EmployeeSpecifications() {
    }

    public static Specification<Employee> hasName(String name) {
        return name == null || name.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Employee> hasSurname(String surname) {
        return surname == null || surname.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Employee> hasEmail(String email) {
        return email == null || email.isBlank()
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Employee> hasRole(Role role) {
        return role == null
                ? Specification.unrestricted()
                : (root, query, cb) ->
                cb.equal(root.get("role"), role);
    }

    public static Specification<Employee> withFilter(EmployeeFilterDTO filter) {
        if (filter == null) {
            return Specification.unrestricted();
        }

        return Specification
                .where(hasName(filter.name()))
                .and(hasSurname(filter.surname()))
                .and(hasEmail(filter.email()))
                .and(hasRole(filter.role()));
    }
}
