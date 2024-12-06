package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.dto.request.ChangePasswordDto;
import org.example.tfintechgradproject.model.User;
import org.example.tfintechgradproject.repository.UserRepository;
import org.example.tfintechgradproject.security.auth.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(ChangePasswordDto request, UserPrincipal authenticatedUser) {
        log.info("Attempting to change password for user: {}", authenticatedUser.getUsername());

        var user = findByEmail(authenticatedUser.getUsername());
        if (!passwordEncoder.matches(request.getCurrentPassword(), authenticatedUser.getPassword())) {
            log.warn("Current password does not match for user: {}", authenticatedUser.getUsername());

            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            log.warn("New password and confirmation password do not match for user: {}", authenticatedUser.getUsername());

            throw new IllegalStateException("Passwords do not match");
        }

        var encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    public User findByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User with email {} not found", email);
                    return new EntityNotFoundException("User with email %s not found".formatted(email));
                });
    }
}
