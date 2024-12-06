package org.example.tfintechgradproject.security.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.security.dto.AuthenticationRequestDto;
import org.example.tfintechgradproject.security.dto.AuthenticationResponseDto;
import org.example.tfintechgradproject.security.dto.RegisterRequestDto;
import org.example.tfintechgradproject.security.jwt.JwtService;
import org.example.tfintechgradproject.security.jwt.JwtToken;
import org.example.tfintechgradproject.model.Role;
import org.example.tfintechgradproject.model.User;
import org.example.tfintechgradproject.repository.UserRepository;
import org.example.tfintechgradproject.security.jwt.JwtTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterRequestDto request) {
        log.info("Registering new user with email: {}", request.getEmail());

        var savedUser = userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .nickname(request.getNickname())
                        .rating(new BigDecimal("0.0"))
                        .build()
        );
        log.debug("User {} saved successfully", savedUser.getEmail());

        var jwtToken = jwtService.generateToken(savedUser.getEmail(), request.isRememberMe());
        saveUserToken(savedUser, jwtToken);
        log.info("Generated JWT token for user: {}", savedUser.getEmail());

        return new AuthenticationResponseDto(jwtToken);
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        log.info("Authenticating user with email: {}", request.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        log.debug("Authentication successful for email: {}", request.getEmail());

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User with email " + request.getEmail() + " not found"));
        var jwtToken = jwtService.generateToken(user.getEmail(), request.isRememberMe());
        saveUserToken(user, jwtToken);
        log.info("JWT token generated for user: {}", user.getEmail());

        return new AuthenticationResponseDto(jwtToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        log.debug("Saving JWT token for user: {}", user.getEmail());

        var token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .build();
        jwtTokenRepository.save(token);

        log.debug("JWT token saved successfully for user: {}", user.getEmail());
    }
}