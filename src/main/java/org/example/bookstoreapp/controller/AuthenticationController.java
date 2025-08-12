package org.example.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookstoreapp.dto.user.UserRegistrationRequestDto;
import org.example.bookstoreapp.dto.user.UserResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "User authentication and authorization operations")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    public UserResponseDto register(UserRegistrationRequestDto request) {
        return userService.register(request);
    }
}
