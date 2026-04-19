package ru.cheseve.easyproject.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cheseve.easyproject.dto.EmployeePatchRequestDTO;
import ru.cheseve.easyproject.dto.EmployeeResponseDTO;
import ru.cheseve.easyproject.dto.EmployeeRequestDTO;
import ru.cheseve.easyproject.enums.EmployeeSort;
import ru.cheseve.easyproject.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api")
public class EmployeeController {

    EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
                @RequestParam(defaultValue = "ID_ASC") EmployeeSort sort
    ) {
        Pageable pageable = PageRequest.of(page, size, sort.getSortValue());
        return ResponseEntity
                .ok(employeeService.getAllEmployees(pageable));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable Long id) {
        return ResponseEntity
                .ok(employeeService.getEmployee(id));
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@Valid @RequestBody EmployeeRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.addEmployee(requestDTO));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDTO> putEmployee(@PathVariable Long id,
                                                           @Valid @RequestBody EmployeeRequestDTO requestDTO) {
        return ResponseEntity.ok(employeeService.putEmployee(id, requestDTO));
    }

    @PatchMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDTO> patchEmployee(@PathVariable Long id,
                                                             @Valid @RequestBody EmployeePatchRequestDTO requestDTO) {
        return ResponseEntity.ok(employeeService.patchEmployee(id, requestDTO));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
