package com.mudit.store.controllers;

import com.mudit.store.dtos.AddItemToCartRequest;
import com.mudit.store.dtos.CartDto;
import com.mudit.store.dtos.CartItemDto;
import com.mudit.store.entities.Cart;
import com.mudit.store.entities.CartItem;
import com.mudit.store.entities.Product;
import com.mudit.store.mappers.CartMapper;
import com.mudit.store.repositories.CartRepository;
import com.mudit.store.repositories.ProductRepository;
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
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        CartItem cartItem = cart.getCartItems().stream().filter(item -> item.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
        CartItemDto cartItemDto = cartMapper.toCartItemDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
}
