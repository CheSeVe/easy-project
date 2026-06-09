package ru.cheseve.easyproject.crm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.crm.dto.employee.*;
import ru.cheseve.easyproject.dto.PageResponse;
import ru.cheseve.easyproject.crm.entity.Employee;
import ru.cheseve.easyproject.crm.exception.EmailAlreadyExistsException;
import ru.cheseve.easyproject.crm.exception.EmployeeNotFoundException;
import ru.cheseve.easyproject.crm.mapper.EmployeeMapper;
import ru.cheseve.easyproject.crm.repository.EmployeeRepository;
import ru.cheseve.easyproject.crm.specification.EmployeeSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {
    EmployeeRepository repository;
    EmployeeMapper mapper;

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployee(Long id) {
        log.debug("getEmployee id={}", id);

        Employee employee = getEmployeeOrThrowException(id);
        return mapper.toResponse(employee);
    }

    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> getAllEmployees(EmployeeFilterDTO filter, Pageable pageable) {
        log.debug("getAllEmployees page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<EmployeeResponse> employeePage = repository
                .findAll(EmployeeSpecifications.withFilter(filter),pageable)
                .map(mapper::toResponse);

        return PageResponse.from(employeePage);
    }

    @Transactional
    public EmployeeResponse addEmployee(EmployeeRequest request) {
        log.debug("addEmployee started");
        validateEmailUniqueness(request.email());

        Employee employee = repository.save(mapper.toEntity(request));
        log.debug("addEmployee - created id={}", employee.getId());
        return mapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse putEmployee(Long id, EmployeeRequest request) {
        log.debug("putEmployee id={}", id);
        Employee existingEmployee = getEmployeeOrThrowException(id);
        validateEmailUniqueness(request.email(), existingEmployee);

        mapper.updateEmployeeFromRequest(request, existingEmployee);

        return mapper.toResponse(existingEmployee);
    }

    @Transactional
    public EmployeeResponse patchEmployee(Long id, EmployeePatchRequest request) {
        log.debug("patchEmployee id={}", id);
        Employee existingEmployee = getEmployeeOrThrowException(id);

        if (request.name() != null) {
            existingEmployee.setName(request.name());
        }
        if (request.surname() != null) {
            existingEmployee.setSurname(request.surname());
        }
        if (request.email() != null) {
            validateEmailUniqueness(request.email(), existingEmployee);
            existingEmployee.setEmail(request.email());
        }
        if (request.password() != null) {
            existingEmployee.setPassword(request.password());
        }
        if (request.role() != null) {
            existingEmployee.setRole(request.role());
        }
        return mapper.toResponse(existingEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        log.debug("deleteEmployee id={}", id);
        Employee employee = getEmployeeOrThrowException(id);
        repository.delete(employee);
    }

    private Employee getEmployeeOrThrowException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        String.format("Employee with id %d does not exist", id)));
    }

    private void validateEmailUniqueness(String email) {
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format(
                    "Employee with email %s already exists", email));
        }
    }

    private void validateEmailUniqueness(String email, Employee existingEmployee) {
        if (email != null
                && !email.equals(existingEmployee.getEmail())
                && repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format(
                    "Employee with email %s already exists", email));
        }
    }
}