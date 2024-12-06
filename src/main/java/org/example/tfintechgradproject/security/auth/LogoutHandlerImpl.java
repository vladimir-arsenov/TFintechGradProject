package org.example.tfintechgradproject.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.security.jwt.JwtTokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final JwtTokenRepository jwtTokenRepository;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Processing logout request");

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("No valid Authorization header found during logout");

            return;
        }
        String jwtToken = authHeader.substring(7);
        log.debug("Extracted JWT token during logout: {}", jwtToken);

        var storedToken = jwtTokenRepository.findByToken(jwtToken)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            jwtTokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
            log.info("Successfully invalidated JWT token and cleared security context");
        } else {
            log.warn("JWT token not found in the repository during logout");
        }
    }
}