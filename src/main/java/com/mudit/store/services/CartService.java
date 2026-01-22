package com.mudit.store.services;

import com.mudit.store.dtos.CartDto;
import com.mudit.store.dtos.CartItemDto;
import com.mudit.store.entities.Cart;
import com.mudit.store.entities.CartItem;
import com.mudit.store.entities.Product;
import com.mudit.store.exceptions.CartNotFoundException;
import com.mudit.store.exceptions.ProductNotFoundException;
import com.mudit.store.mappers.CartMapper;
import com.mudit.store.repositories.CartRepository;
import com.mudit.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDto createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        Cart cart = cartRepository.getCartWithCartItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        CartItem cartItem = cart.addItem(product);

        cartRepository.save(cart);
        return cartMapper.toCartItemDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        Cart cart = cartRepository.getCartWithCartItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        } else {
            return cartMapper.toCartDto(cart);
        }
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.getCartWithCartItems(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        } else {
            CartItem cartItem = cart.getItem(productId);

            if (cartItem != null) {
                cartItem.setQuantity(quantity);
                cartRepository.save(cart);
                return cartMapper.toCartItemDto(cartItem);
            } else {
                throw new ProductNotFoundException();
            }
        }
    }

    public void removeItem(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        } else {
            cart.removeItem(productId);
            cartRepository.save(cart);
        }
    }

    public void clearCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);
    }
}

