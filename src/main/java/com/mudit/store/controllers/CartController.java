package com.mudit.store.controllers;

import com.mudit.store.dtos.AddItemToCartRequest;
import com.mudit.store.dtos.CartDto;
import com.mudit.store.dtos.CartItemDto;
import com.mudit.store.dtos.UpdateCartItemRequest;
import com.mudit.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder) {
        CartDto cartDto = cartService.createCart();
        URI uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId, @RequestBody AddItemToCartRequest request) {
        CartItemDto cartItemDto = cartService.addToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        CartDto cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }


    @PutMapping("/{cartId}/items/{productId}")
    ResponseEntity<CartItemDto> updateItem(@PathVariable UUID cartId, @PathVariable Long productId, @Valid @RequestBody UpdateCartItemRequest request) {
        CartItemDto cartItemDto = cartService.updateItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    ResponseEntity<Void> removeItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        cartService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Cart not found"));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Product not found"));
    }
}
