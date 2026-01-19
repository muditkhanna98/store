package com.mudit.store.mappers;

import com.mudit.store.dtos.CartDto;
import com.mudit.store.dtos.CartItemDto;
import com.mudit.store.entities.Cart;
import com.mudit.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toCartItemDto(CartItem cartItem);
}
