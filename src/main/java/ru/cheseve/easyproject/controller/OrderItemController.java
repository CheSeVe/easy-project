package ru.cheseve.easyproject.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.cheseve.easyproject.dto.orderItem.OrderItemsRequestDTO;
import ru.cheseve.easyproject.dto.orderItem.OrderItemsResponseDTO;
import ru.cheseve.easyproject.service.OrderItemService;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/orders/{orderId}/items")
public class OrderItemController {

    OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<OrderItemsResponseDTO> getAllItems(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.getItems(orderId));
    }

    @PutMapping
    public ResponseEntity<OrderItemsResponseDTO> putItems(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderItemsRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(orderItemService.putItems(orderId, requestDTO));
    }

    @PatchMapping
    public ResponseEntity<OrderItemsResponseDTO> patchItems(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderItemsRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(orderItemService.patchItems(orderId, requestDTO));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long orderId,
            @PathVariable Long productId
    ) {
        orderItemService.deleteItem(orderId, productId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
