package org.example.bookstoreapp.security;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.dto.user.UserLoginRequestDto;
import org.example.bookstoreapp.dto.user.UserLoginResponseDto;
import org.example.bookstoreapp.repository.user.UserRepository;
import org.example.bookstoreapp.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));

        List<String> roles = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtUtil.generateToken(authenticate.getName(), roles);
        return new UserLoginResponseDto(token);
    }
}
