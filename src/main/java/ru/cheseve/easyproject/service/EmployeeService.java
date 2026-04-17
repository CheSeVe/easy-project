package ru.cheseve.easyproject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.cheseve.easyproject.dto.EmployeePatchRequestDTO;
import ru.cheseve.easyproject.dto.EmployeeResponseDTO;
import ru.cheseve.easyproject.dto.EmployeeRequestDTO;
import ru.cheseve.easyproject.entity.Employee;
import ru.cheseve.easyproject.exception.EmailAlreadyExistsException;
import ru.cheseve.easyproject.exception.EntityNotFoundException;
import ru.cheseve.easyproject.exception.RepositoryException;
import ru.cheseve.easyproject.mapper.EmployeeMapper;
import ru.cheseve.easyproject.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {
    EmployeeRepository repository;

    public List<EmployeeResponseDTO> getAllEmployees() {
        return repository.findAll().stream()
                .map(EmployeeMapper::mapEntityToResponse)
                .toList();
    }

    public EmployeeResponseDTO getEmployee(Long id) {
        Employee employee = getEmployeeOrThrowException(id);

        return EmployeeMapper.mapEntityToResponse(employee);
    }

    private Employee getEmployeeOrThrowException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Employee with id %d does not exist", id)));
    }

    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO requestDTO) {
        validateEmailUniqueness(requestDTO.email());

        Employee employee = repository.save(EmployeeMapper.mapRequestToEntity(requestDTO));
        return EmployeeMapper.mapEntityToResponse(employee);
    }

    private void validateEmailUniqueness(String email) {
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format(
                    "Employee with email %s already exists", email));
        }
    }

    public EmployeeResponseDTO putEmployee(Long id, EmployeeRequestDTO requestDTO) {

        Employee existingEmployee = getEmployeeOrThrowException(id);

        validateEmailUniqueness(requestDTO.email(), existingEmployee);

        Employee newEmployee = EmployeeMapper.mapRequestToEntity(requestDTO);
        newEmployee.setId(existingEmployee.getId());
        return EmployeeMapper.mapEntityToResponse(repository.save(newEmployee));
    }

    private void validateEmailUniqueness(String email, Employee existingEmployee) {
        if (email != null
                && !email.equals(existingEmployee.getEmail())
                && repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format(
                    "Employee with email %s already exists", email));
        }
    }

    public EmployeeResponseDTO patchEmployee(Long id, EmployeePatchRequestDTO requestDTO) {
        Employee existingEmployee = getEmployeeOrThrowException(id);

        if (requestDTO.name() != null && !requestDTO.name().isBlank()) {
            existingEmployee.setName(requestDTO.name());
        }
        if (requestDTO.surname() != null && !requestDTO.surname().isBlank()) {
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
        return EmployeeMapper.mapEntityToResponse(repository.save(existingEmployee));
    }

    public void deleteEmployee(Long id) {
        try {
            repository.deleteById(id);
        } catch (RepositoryException e) {
            throw new EntityNotFoundException(e);
        }
    }
}