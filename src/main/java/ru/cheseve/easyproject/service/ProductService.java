package ru.cheseve.easyproject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cheseve.easyproject.dto.PageResponseDTO;
import ru.cheseve.easyproject.dto.product.ProductFilterDTO;
import ru.cheseve.easyproject.dto.product.ProductPatchRequestDTO;
import ru.cheseve.easyproject.dto.product.ProductRequestDTO;
import ru.cheseve.easyproject.dto.product.ProductResponseDTO;
import ru.cheseve.easyproject.entity.Product;
import ru.cheseve.easyproject.exception.NameAlreadyExistsException;
import ru.cheseve.easyproject.exception.ProductNotFoundException;
import ru.cheseve.easyproject.mapper.ProductMapper;
import ru.cheseve.easyproject.repository.OrderItemRepository;
import ru.cheseve.easyproject.repository.ProductRepository;
import ru.cheseve.easyproject.specification.ProductSpecifications;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper mapper;
    OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public ProductResponseDTO getProduct(Long id) {
        Product product = getProductOrThrowException(id);

        return mapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<ProductResponseDTO> getAllProducts(
            ProductFilterDTO filter,
            Pageable pageable
    ) {
        Page<ProductResponseDTO> productPage = productRepository
                .findAll(ProductSpecifications.withFilter(filter), pageable)
                .map(mapper::toResponse);

        return PageResponseDTO.from(productPage);
    }

    @Transactional
    public ProductResponseDTO addProduct(ProductRequestDTO requestDTO) {
        validateNameUniqueness(requestDTO.name());

        Product product = productRepository.save(mapper.toEntity(requestDTO));

        return mapper.toResponse(product);
    }

    @Transactional
    public ProductResponseDTO putProduct(Long id,  ProductRequestDTO requestDTO) {
        Product existingProduct = getProductOrThrowException(id);

        validateNameUniqueness(requestDTO.name(), existingProduct);

        mapper.updateProductFromRequest(requestDTO, existingProduct);

        return mapper.toResponse(existingProduct);
    }

    @Transactional
    public ProductResponseDTO patchProduct(Long id, ProductPatchRequestDTO requestDTO) {
        Product existingProduct = getProductOrThrowException(id);

        if (requestDTO.name() != null) {
            validateNameUniqueness(requestDTO.name(), existingProduct);
            existingProduct.setName(requestDTO.name());
        }

        if (requestDTO.description() != null) {
            existingProduct.setDescription(requestDTO.description());
        }

        if (requestDTO.price() != null) {
            existingProduct.setPrice(requestDTO.price());
        }

        return mapper.toResponse(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductOrThrowException(id);

        if (orderItemRepository.existsByProduct_Id(id)) {
            throw new IllegalArgumentException(String.format(
                    "Product with id %d already used in orders", id));
        }

        productRepository.delete(product);
    }
    private Product getProductOrThrowException(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(
                    "Product with id %d does not exist", id
                )));
    }

    private void validateNameUniqueness(String name) {
        if (productRepository.existsByName(name)) {
            throw new NameAlreadyExistsException(String.format(
                    "Product with name %s already exists", name));
        }
    }

    private void validateNameUniqueness(String name, Product existingProduct) {
        if (name != null
                && !name.equals(existingProduct.getName())
                && productRepository.existsByName(name)) {
            throw new NameAlreadyExistsException(String.format(
                    "Product with name %s already exists", name));
        }
    }
}