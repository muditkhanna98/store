package com.mudit.store.mappers;

import com.mudit.store.dtos.ProductDto;
import com.mudit.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toProductDto(Product product);
}
