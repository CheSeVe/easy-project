package ru.cheseve.easyproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cheseve.easyproject.dto.product.ProductRequestDTO;
import ru.cheseve.easyproject.dto.product.ProductResponseDTO;
import ru.cheseve.easyproject.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDTO requestDTO);
    ProductResponseDTO toResponse(Product product);
    void updateProductFromRequest(ProductRequestDTO requestDTO, @MappingTarget Product product);
}
