package com.banking.auth.controller;

import com.banking.auth.dto.LoginRequestDTO;
import com.banking.auth.dto.LoginResponseDTO;
import com.banking.auth.dto.RegisterRequestDTO;
import com.banking.auth.dto.RegisterResponseDTO;
import com.banking.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : AuthController
//
//  Description         : Provides REST APIs for customer
//                        registration and authentication.
//
//                        It accepts registration and login requests
//                        and delegates the authentication operations
//                        to AuthService.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentication",
        description = "APIs for customer registration and login"
)
public class AuthController
{
    private final AuthService authService;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : AuthController
    //
    //  Description         : Initializes the AuthController with
    //                        AuthService dependency.
    //
    //                        AuthService is used to perform customer
    //                        registration and login operations.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AuthController(AuthService authService)
    {
        // Initialize AuthService dependency
        this.authService = authService;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : register
    //
    //  Description         : Registers a new customer in the banking
    //                        portal.
    //
    //                        It accepts and validates the registration
    //                        request and passes it to AuthService.
    //
    //                        The newly registered customer response is
    //                        returned with HTTP CREATED status.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Register a new customer",
            description = "Create a new customer account in the banking portal"
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDTO register(@Valid @RequestBody RegisterRequestDTO request)
    {
        // Pass the registration request to AuthService
        return authService.register(request);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : login
    //
    //  Description         : Authenticates a customer using the
    //                        provided login credentials.
    //
    //                        It validates the login request and passes
    //                        it to AuthService.
    //
    //                        After successful authentication, a JWT
    //                        token is returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Login customer",
            description = "Authenticates a customer and returns a JWT token"
    )
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request)
    {
        // Pass the login request to AuthService
        return authService.login(request);
    }
}