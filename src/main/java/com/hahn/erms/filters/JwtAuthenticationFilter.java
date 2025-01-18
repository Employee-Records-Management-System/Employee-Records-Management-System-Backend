package com.hahn.erms.filters;

import com.hahn.erms.services.JwtService;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            logger.error("JWT processing error: {}", e.getMessage());
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        } catch (UsernameNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            setErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    private void setErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType("application/json");
        try {
            String jsonResponse = String.format("{\"error\": \"%s\"}", message);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            logger.error("Error writing the response: {}", e.getMessage());
        }
    }

}