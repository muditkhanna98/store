package com.mudit.store.controllers;

import com.mudit.store.dtos.JWTResponse;
import com.mudit.store.dtos.UserDto;
import com.mudit.store.dtos.UserLoginRequest;
import com.mudit.store.entities.User;
import com.mudit.store.repositories.UserRepository;
import com.mudit.store.services.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;


    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);


        //Cookie for refresh tokens
        Cookie jwtCookie = new Cookie("refreshToken", refreshToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/auth/refresh");
        jwtCookie.setMaxAge(604800); //7d
        jwtCookie.setSecure(true);

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(new JWTResponse(accessToken));
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Validate called");
        String token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    @GetMapping("/me")

    public ResponseEntity<UserDto> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
            return ResponseEntity.ok(userDto);
        }

    }

    @ExceptionHandler
    public ResponseEntity<Void> handleBadLogins(BadCredentialsException badCredentialsException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
