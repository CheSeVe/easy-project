package ru.cheseve.easyproject.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cheseve.easyproject.dto.PageResponseDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeFilterDTO;
import ru.cheseve.easyproject.dto.employee.EmployeePatchRequestDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeResponseDTO;
import ru.cheseve.easyproject.dto.employee.EmployeeRequestDTO;
import ru.cheseve.easyproject.enums.EmployeeSort;
import ru.cheseve.easyproject.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/employees")
public class EmployeeController {

    EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<EmployeeResponseDTO>> getAllEmployees(
            @ModelAttribute EmployeeFilterDTO filter,
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "ID_ASC") EmployeeSort sort
    ) {
        Pageable pageable = PageRequest.of(page, size, sort.getSort());
        return ResponseEntity.ok(employeeService.getAllEmployees(filter, pageable));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody @Valid EmployeeRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.addEmployee(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> putEmployee(@PathVariable Long id,
                                                           @RequestBody @Valid EmployeeRequestDTO requestDTO) {
        return ResponseEntity.ok(employeeService.putEmployee(id, requestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> patchEmployee(@PathVariable Long id,
                                                             @RequestBody @Valid EmployeePatchRequestDTO requestDTO) {
        return ResponseEntity.ok(employeeService.patchEmployee(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
