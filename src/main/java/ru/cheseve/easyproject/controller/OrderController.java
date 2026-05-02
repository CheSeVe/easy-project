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
import ru.cheseve.easyproject.dto.order.*;
import ru.cheseve.easyproject.enums.OrderSort;
import ru.cheseve.easyproject.service.OrderService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/orders")
public class OrderController {

    OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderWithCustomerResponseDTO> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderWithCustomer(orderId));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<OrderWithCustomerIdResponseDTO>> getAllOrders(
            @ModelAttribute OrderFilterDTO filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "CREATED_ASC") OrderSort sort
            ) {
        Pageable pageable = PageRequest.of(page, size, sort.getSort());

        return ResponseEntity.ok(orderService.getAllOrders(filter, pageable));
    }

    @PostMapping
    public ResponseEntity<OrderWithCustomerIdResponseDTO> addOrder(@RequestBody @Valid OrderRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.addOrder(requestDTO));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> changeOrderStatus(
            @PathVariable Long oderId,
            @RequestBody @Valid OrderStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(orderService.changeOrderStatus(oderId, requestDTO));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long oderId) {
        orderService.deleteOrder(oderId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
