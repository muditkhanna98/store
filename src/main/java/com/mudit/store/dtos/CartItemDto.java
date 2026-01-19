package com.mudit.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CartItemDto {
    private CartProductDto product;
    private int quantity;
    private BigDecimal totalPrice;
}