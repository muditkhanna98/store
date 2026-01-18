package com.mudit.store.controllers;

import com.mudit.store.dtos.RegisterUserRequest;
import com.mudit.store.dtos.UpdateUserRequest;
import com.mudit.store.dtos.UserDto;
import com.mudit.store.entities.User;
import com.mudit.store.mappers.UserMapper;
import com.mudit.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false, name = "sort") String sort) {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterUserRequest userRequest,
                                              UriComponentsBuilder uriComponentsBuilder) {
        User user = userMapper.toEntity(userRequest);
        userRepository.save(user);

        UserDto userDto = userMapper.toDto(user);
        //Good practise to do this. It's a REST Convention to set status to 201
        URI uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id,
                                              @RequestBody UpdateUserRequest userRequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            userMapper.update(userRequest, user);
            userRepository.save(user);

            return ResponseEntity.ok(userMapper.toDto(user));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }
    }
}
