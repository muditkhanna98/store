package com.mudit.store.mappers;

import com.mudit.store.dtos.RegisterUserRequest;
import com.mudit.store.dtos.UserDto;
import com.mudit.store.entities.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);
}
