package com.mudit.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddItemToCartRequest {
    private Long productId;
}
