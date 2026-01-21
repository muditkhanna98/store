package com.mudit.store.repositories;

import com.mudit.store.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepositoryImplementation<Cart, UUID> {
    @EntityGraph(attributePaths = "cartItems.product")
    @Query("SELECT c FROM Cart c where c.id=:cartId")
    Optional<Cart> getCartWithCartItems(UUID cartId);
}