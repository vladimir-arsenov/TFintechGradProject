package org.example.tfintechgradproject.security.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to load user by email: {}", email);

        return new UserPrincipal(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> {
                            log.warn("User with email '{}' not found", email);
                            return new UsernameNotFoundException("User with email '%s' not found".formatted(email));
                        })
        );
    }
}
