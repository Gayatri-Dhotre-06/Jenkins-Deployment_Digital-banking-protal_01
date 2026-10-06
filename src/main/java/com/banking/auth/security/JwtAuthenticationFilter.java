package com.banking.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : JwtAuthenticationFilter
//
//  Description         : Filters incoming HTTP requests and checks
//                        for a valid JWT token.
//
//                        It extracts the JWT token from the
//                        Authorization header and validates it.
//
//                        After successful validation, it extracts
//                        the user's email and role and stores the
//                        authentication details in the Spring Security
//                        context.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    // Provides JWT token validation and data extraction operations
    private final JwtService jwtService;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : JwtAuthenticationFilter
    //
    //  Description         : Initializes the JWT authentication
    //                        filter with JwtService dependency.
    //
    //                        JwtService is used to validate the token
    //                        and extract user information from it.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public JwtAuthenticationFilter(JwtService jwtService)
    {
        // Initialize JwtService dependency
        this.jwtService = jwtService;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : doFilterInternal
    //
    //  Description         : Processes each incoming HTTP request
    //                        to authenticate the user using a JWT.
    //
    //                        It checks the Authorization header,
    //                        extracts and validates the JWT token,
    //                        extracts the user's email and role,
    //                        and stores the authentication details
    //                        in the Spring Security context.
    //
    //                        The request is then passed to the next
    //                        filter in the security filter chain.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // Get the Authorization header from the request
        String authorizationHeader = request.getHeader("Authorization");

        // Check whether the Authorization header contains a Bearer token
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
        {
            // Continue the request without JWT authentication
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Validate the JWT token
        if(jwtService.isTokenValid(token))
        {
            // Extract the user's email from the JWT token
            String email = jwtService.extractEmail(token);

            // Extract the user's role from the JWT token
            String role = jwtService.extractRole(token);

            // Create an authority using the user's role
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            // Create authentication details for the authenticated user
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(authority)
                    );

            // Store the authentication details in the Spring Security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue processing the request through the security filter chain
        filterChain.doFilter(request, response);
    }
}