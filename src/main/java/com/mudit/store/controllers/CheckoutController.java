package com.mudit.store.controllers;

import com.mudit.store.dtos.CheckoutRequestDto;
import com.mudit.store.dtos.CheckoutResponseDto;
import com.mudit.store.entities.*;
import com.mudit.store.repositories.CartRepository;
import com.mudit.store.repositories.OrderRepository;
import com.mudit.store.repositories.UserRepository;
import com.mudit.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutController {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequestDto request) {
        Cart cart = cartRepository.getCartWithCartItems(request.getCartId()).orElse(null);

        if (cart == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart not found"));
        }

        if (cart.getCartItems().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        User user = userRepository.findById(userId).orElse(null);

        Order newOrder = new Order();
        newOrder.setTotalPrice(cart.getTotalPrice());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setCustomer(user);

        cart.getCartItems().forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProduct(item.getProduct());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            newOrder.getItems().add(orderItem);
        });

        orderRepository.save(newOrder);

        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponseDto(newOrder.getId()));
    }
}

