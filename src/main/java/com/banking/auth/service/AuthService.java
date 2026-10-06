package com.banking.auth.service;

import com.banking.auth.security.JwtService;
import com.banking.auth.dto.LoginRequestDTO;
import com.banking.auth.dto.LoginResponseDTO;
import com.banking.auth.dto.RegisterRequestDTO;
import com.banking.auth.dto.RegisterResponseDTO;
import com.banking.auth.entity.Role;
import com.banking.auth.entity.User;
import com.banking.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.banking.customer.entity.Customer;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : AuthService
//
//  Description         : Provides business logic for customer
//                        registration and authentication.
//
//                        It handles customer registration, password
//                        encryption, user login, JWT token generation,
//                        and password hash generation.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

@Service
public class AuthService
{
    // Provides database operations for User entity
    private final UserRepository userRepository;

    // Encrypts and verifies user passwords using BCrypt
    private final BCryptPasswordEncoder passwordEncoder;

    // Generates and manages JWT authentication tokens
    private final JwtService jwtService;

    // Provides database operations for Customer entity
    private final CustomerRepository customerRepository;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : AuthService
    //
    //  Description         : Initializes the AuthService with all
    //                        required dependencies.
    //
    //                        The dependencies are used for user
    //                        management, password encryption, JWT
    //                        generation, and customer management.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       BCryptPasswordEncoder passwordEncoder,
                       CustomerRepository customerRepository)
    {
        // Initialize UserRepository dependency
        this.userRepository = userRepository;

        // Initialize JwtService dependency
        this.jwtService = jwtService;

        // Initialize BCryptPasswordEncoder dependency
        this.passwordEncoder = passwordEncoder;

        // Initialize CustomerRepository dependency
        this.customerRepository = customerRepository;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : register
    //
    //  Description         : Registers a new customer in the banking
    //                        portal.
    //
    //                        It checks whether the email is already
    //                        registered, creates a new user, encrypts
    //                        the password, assigns the CUSTOMER role,
    //                        and saves the user.
    //
    //                        It then creates the corresponding
    //                        customer record and returns the
    //                        registration response.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request)
    {
        // Check whether the email is already registered
        if(userRepository.existsByEmail(request.getEmail()))
        {
            // Stop registration when the email already exists
            throw new RuntimeException("Email already registered");
        }

        // Create a new User object
        User user = new User();

        // Set the customer's name
        user.setName(request.getName());

        // Set the customer's email
        user.setEmail(request.getEmail());

        // Encrypt the customer's password using BCrypt
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Store the encrypted password
        user.setPassword(encodedPassword);

        // Assign the CUSTOMER role to the new user
        user.setRole(Role.CUSTOMER);

        // Save the user in the database
        User savedUser = userRepository.save(user);

        // Create a corresponding Customer object
        Customer customer = new Customer();

        // Set the customer's name from the saved user
        customer.setName(savedUser.getName());

        // Set the customer's email from the saved user
        customer.setEmail(savedUser.getEmail());

        // Set the customer's phone number
        customer.setPhone(request.getPhone());

        // Save the customer in the database
        customerRepository.save(customer);

        // Return the registered customer details
        return new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : login
    //
    //  Description         : Authenticates a customer using the
    //                        provided email and password.
    //
    //                        It finds the user by email, verifies the
    //                        password using BCrypt, generates a JWT
    //                        token after successful authentication,
    //                        and returns the login response.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public LoginResponseDTO login(LoginRequestDTO request)
    {
        // Find the user using the provided email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        // Verify the provided password against the stored encrypted password
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            // Reject the login when the password does not match
            throw new RuntimeException("Invalid email or password");
        }

        // Generate a JWT token using the user's email and role
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // Return the successful login response with JWT token
        return new LoginResponseDTO(
                "Login successful",
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : generatePasswordHash
    //
    //  Description         : Generates a BCrypt hash for the provided
    //                        password.
    //
    //                        This method is used when a password needs
    //                        to be converted into a secure BCrypt hash.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public String generatePasswordHash(String password)
    {
        // Generate and return the BCrypt password hash
        return passwordEncoder.encode(password);
    }
}