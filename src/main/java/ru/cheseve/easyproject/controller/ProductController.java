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
import ru.cheseve.easyproject.dto.product.ProductFilterDTO;
import ru.cheseve.easyproject.dto.product.ProductPatchRequestDTO;
import ru.cheseve.easyproject.dto.product.ProductRequestDTO;
import ru.cheseve.easyproject.dto.product.ProductResponseDTO;
import ru.cheseve.easyproject.enums.ProductSort;
import ru.cheseve.easyproject.service.ProductService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/products")
public class ProductController {
    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<ProductResponseDTO>> getAllProducts(
            @ModelAttribute ProductFilterDTO filter,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "NAME_ASC") ProductSort sort
            ) {
        Pageable pageable = PageRequest.of(page, size, sort.getSort());
        return ResponseEntity.ok(productService.getAllProducts(filter, pageable));
    }

    @PostMapping
    ResponseEntity<ProductResponseDTO> addProduct(@RequestBody @Valid ProductRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(requestDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductResponseDTO> putProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequestDTO requestDTO) {
        return ResponseEntity.ok(productService.putProduct(id, requestDTO));
    }

    @PatchMapping("/{id}")
    ResponseEntity<ProductResponseDTO> patchProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductPatchRequestDTO requestDTO) {
        return ResponseEntity.ok(productService.patchProduct(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
