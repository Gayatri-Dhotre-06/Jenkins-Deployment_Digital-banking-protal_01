//////////////////////////////////////////////////////////////////
//
//  Class Name          : SecurityConfig
//
//  Description         : Configures Spring Security for the Digital
//                        Banking Portal application.
//
//                        It defines password encoding, public and
//                        protected API endpoints, role-based access,
//                        stateless session management, and JWT
//                        authentication.
//
//                        It also registers the JWT authentication
//                        filter before the default username and
//                        password authentication filter.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.config;

import com.banking.auth.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig
{
    // Stores the JWT authentication filter used to validate JWT tokens
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : SecurityConfig
    //
    //  Description         : Initializes the SecurityConfig class
    //                        with the JWT authentication filter.
    //
    //                        Constructor injection is used to provide
    //                        the JWT authentication filter dependency.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter)
    {
        // Store the JWT authentication filter
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : passwordEncoder
    //
    //  Description         : Creates a BCrypt password encoder bean.
    //
    //                        BCrypt is used to securely hash user
    //                        passwords before storing them in the
    //                        database.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        // Create and return the BCrypt password encoder
        return new BCryptPasswordEncoder();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : securityFilterChain
    //
    //  Description         : Configures the security rules for the
    //                        application's REST APIs.
    //
    //                        It disables CSRF, defines public and
    //                        protected endpoints, configures role-based
    //                        authorization, enables stateless sessions,
    //                        and registers the JWT authentication filter.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        // Disable CSRF because the application uses JWT-based REST APIs
        http.csrf(csrf -> csrf.disable())

                // Configure authorization rules for different endpoints
                .authorizeHttpRequests(auth ->

                        // Allow authentication, Swagger, and API documentation
                        // endpoints without requiring authentication
                        auth.requestMatchers("/api/auth/register",
                                        "/api/auth/login",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**")
                                .permitAll()

                                // Allow authenticated customers to access
                                // their own customer information
                                .requestMatchers("/api/customers/me").authenticated()

                                // Allow authenticated users to access
                                // customer profile endpoints
                                .requestMatchers("/api/customers/profile/**").authenticated()

                                // Allow only users with ADMIN role to access
                                // all customers
                                .requestMatchers("/api/customers/all").hasRole("ADMIN")

                                // Allow only users with ADMIN role to access
                                // customer-specific endpoints
                                .requestMatchers("/api/customers/*").hasRole("ADMIN")

                                // Allow only users with ADMIN role to access
                                // admin endpoints
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                                // Allow only users with ADMIN role to change
                                // account status
                                .requestMatchers("/api/accounts/*/status").hasRole("ADMIN")

                                // Require authentication for all remaining endpoints
                                .anyRequest().authenticated())

                // Configure the application to use stateless sessions
                // because authentication is handled using JWT tokens
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add the JWT filter before Spring Security's default
                // username and password authentication filter
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        // Build and return the configured security filter chain
        return http.build();
    }
}