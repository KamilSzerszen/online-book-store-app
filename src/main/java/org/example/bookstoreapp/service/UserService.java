package org.example.bookstoreapp.service;

import org.example.bookstoreapp.dto.user.UserRegistrationRequestDto;
import org.example.bookstoreapp.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto request);
}
