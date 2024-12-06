package org.example.tfintechgradproject.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tfintechgradproject.security.auth.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JWT security filter invoked for request: {}", request.getRequestURI());

        var authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.debug("No Bearer authorization header found");

            filterChain.doFilter(request, response);
            return;
        }
        var jwtToken = authHeader.substring(BEARER_PREFIX.length());
        log.debug("Extracted JWT token: {}", jwtToken);

        var username = jwtService.extractUsername(jwtToken);
        var userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(jwtToken, userDetails)) {
            log.info("JWT token is valid for user: {}", username);

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("JWT token is invalid or expired for user: {}", username);
        }

        filterChain.doFilter(request, response);
    }
}
