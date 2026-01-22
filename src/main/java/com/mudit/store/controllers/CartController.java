package com.mudit.store.controllers;

import com.mudit.store.dtos.AddItemToCartRequest;
import com.mudit.store.dtos.CartDto;
import com.mudit.store.dtos.CartItemDto;
import com.mudit.store.dtos.UpdateCartItemRequest;
import com.mudit.store.entities.Cart;
import com.mudit.store.entities.CartItem;
import com.mudit.store.entities.Product;
import com.mudit.store.mappers.CartMapper;
import com.mudit.store.repositories.CartRepository;
import com.mudit.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder) {
        Cart cart = new Cart();
        cartRepository.save(cart);

        CartDto cartDto = cartMapper.toCartDto(cart);
        URI uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId, @RequestBody AddItemToCartRequest request) {
        Cart cart = cartRepository.getCartWithCartItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        CartItem cartItem = cart.addItem(product);

        cartRepository.save(cart);
        CartItemDto cartItemDto = cartMapper.toCartItemDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        Cart cart = cartRepository.getCartWithCartItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        } else {
            CartDto cartDto = cartMapper.toCartDto(cart);
            return ResponseEntity.ok(cartDto);
        }
    }


    @PutMapping("/{cartId}/items/{productId}")
    ResponseEntity<CartItemDto> updateItem(@PathVariable UUID cartId,
                                           @PathVariable Long productId,
                                           @Valid @RequestBody UpdateCartItemRequest request
    ) {
        Cart cart = cartRepository.getCartWithCartItems(cartId).orElse(null);

        if (cart == null) {
            return ResponseEntity.notFound().build();
        } else {
            CartItem cartItem = cart.getItem(productId);

            if (cartItem != null) {
                cartItem.setQuantity(request.getQuantity());
                cartRepository.save(cart);
                CartItemDto cartItemDto = cartMapper.toCartItemDto(cartItem);
                return ResponseEntity.ok(cartItemDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    ResponseEntity<Void> removeItem(@PathVariable UUID cartId,
                                    @PathVariable Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        } else {
            cart.removeItem(productId);
            cartRepository.save(cart);
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        cart.clear();
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }
}
