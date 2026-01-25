package com.mudit.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CheckoutRequestDto {
    @NotNull(message = "Cart ID is required")
    private UUID cartId;
}
