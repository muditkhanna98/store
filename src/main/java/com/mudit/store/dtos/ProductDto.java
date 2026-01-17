package com.mudit.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String categoryId;
}
