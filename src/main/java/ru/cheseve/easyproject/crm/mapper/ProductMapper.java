package ru.cheseve.easyproject.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cheseve.easyproject.crm.dto.product.ProductRequest;
import ru.cheseve.easyproject.crm.dto.product.ProductResponse;
import ru.cheseve.easyproject.crm.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest request);
    ProductResponse toResponse(Product product);
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}
