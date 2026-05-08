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
import ru.cheseve.easyproject.dto.customer.*;
import ru.cheseve.easyproject.enums.CustomerSort;
import ru.cheseve.easyproject.service.CustomerService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/customers")
public class CustomerController {
    CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerWithOrdersResponseDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerWithOrders(id));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<CustomerResponseDTO>> getAllCustomers(
            @ModelAttribute CustomerFilterDTO filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "NAME") CustomerSort sort
            ) {
        Pageable pageable = PageRequest.of(page, size, sort.getSort());
        return ResponseEntity.ok(customerService.getAllCustomers(filter ,pageable));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> addCustomer(@RequestBody @Valid CustomerRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.addCustomer(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> putCustomer(@PathVariable Long id,
                                                           @RequestBody @Valid CustomerRequestDTO requestDTO) {
        return ResponseEntity.ok(customerService.putCustomer(id, requestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> patchCustomer(@PathVariable Long id,
                                                             @RequestBody @Valid CustomerPatchRequestDTO requestDTO) {
        return ResponseEntity.ok(customerService.patchCustomer(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
