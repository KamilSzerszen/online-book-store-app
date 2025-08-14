package org.example.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.user.UserRegistrationRequestDto;
import org.example.bookstoreapp.dto.user.UserResponseDto;
import org.example.bookstoreapp.exception.RegistrationException;
import org.example.bookstoreapp.mapper.UserMapper;
import org.example.bookstoreapp.model.User;
import org.example.bookstoreapp.repository.user.UserRepository;
import org.example.bookstoreapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Email already exists");
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User registered = userRepository.save(user);
        return userMapper.toDto(registered);
    }
}
