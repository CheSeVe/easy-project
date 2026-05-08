package ru.cheseve.easyproject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.dto.PageResponseDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeFilterDTO;
import ru.cheseve.easyproject.dto.employee.EmployeePatchRequestDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeResponseDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeRequestDTO;
import ru.cheseve.easyproject.entity.Employee;
import ru.cheseve.easyproject.exception.EmailAlreadyExistsException;
import ru.cheseve.easyproject.exception.EmployeeNotFoundException;
import ru.cheseve.easyproject.mapper.EmployeeMapper;
import ru.cheseve.easyproject.repository.EmployeeRepository;
import ru.cheseve.easyproject.specification.EmployeeSpecifications;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {
    EmployeeRepository repository;
    EmployeeMapper mapper;

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployee(Long id) {
        Employee employee = getEmployeeOrThrowException(id);

        return mapper.toResponseDTO(employee);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<EmployeeResponseDTO> getAllEmployees(EmployeeFilterDTO filter, Pageable pageable) {
        Page<EmployeeResponseDTO> employeePage = repository
                .findAll(EmployeeSpecifications.withFilter(filter),pageable)
                .map(mapper::toResponseDTO);

        return PageResponseDTO.from(employeePage);
    }

    @Transactional
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO requestDTO) {
        validateEmailUniqueness(requestDTO.email());

        Employee employee = repository.save(mapper.toEntity(requestDTO));
        return mapper.toResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO putEmployee(Long id, EmployeeRequestDTO requestDTO) {
        Employee existingEmployee = getEmployeeOrThrowException(id);
        validateEmailUniqueness(requestDTO.email(), existingEmployee);

        mapper.updateEmployeeFromDto(requestDTO, existingEmployee);

        return mapper.toResponseDTO(existingEmployee);
    }

    @Transactional
    public EmployeeResponseDTO patchEmployee(Long id, EmployeePatchRequestDTO requestDTO) {
        Employee existingEmployee = getEmployeeOrThrowException(id);

        if (requestDTO.name() != null) {
            existingEmployee.setName(requestDTO.name());
        }
        if (requestDTO.surname() != null) {
            existingEmployee.setSurname(requestDTO.surname());
        }
        if (requestDTO.email() != null) {
            validateEmailUniqueness(requestDTO.email(), existingEmployee);
            existingEmployee.setEmail(requestDTO.email());
        }
        if (requestDTO.password() != null) {
            existingEmployee.setPassword(requestDTO.password());
        }
        if (requestDTO.role() != null) {
            existingEmployee.setRole(requestDTO.role());
        }
        return mapper.toResponseDTO(existingEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
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