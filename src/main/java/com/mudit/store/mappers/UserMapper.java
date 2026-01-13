package com.mudit.store.mappers;

import com.mudit.store.dtos.UserDto;
import com.mudit.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
