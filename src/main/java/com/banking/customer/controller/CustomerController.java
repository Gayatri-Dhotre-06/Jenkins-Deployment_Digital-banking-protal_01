//////////////////////////////////////////////////////////////////
//
//  Class Name          : CustomerController
//
//  Description         : Handles REST API requests related to
//                        customer profile management.
//
//                        It provides endpoints for creating,
//                        retrieving, updating, and deleting
//                        customer profiles.
//
//                        It also handles authenticated customer
//                        profile operations using JWT tokens.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.customer.controller;

import com.banking.auth.security.JwtService;
import com.banking.customer.dto.CustomerProfileUpdateDTO;
import com.banking.customer.dto.CustomerRequestDTO;
import com.banking.customer.dto.CustomerResponseDTO;
import com.banking.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(
        name = "Customer",
        description = "APIs for customer profile management"
)
public class CustomerController
{
    // Stores the customer service used to perform customer operations
    private final CustomerService customerService;

    // Stores the JWT service used to extract customer information from JWT tokens
    private final JwtService jwtService;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : CustomerController
    //
    //  Description         : Initializes the CustomerController with
    //                        the required service dependencies.
    //
    //                        Constructor-based dependency injection
    //                        is used to provide the CustomerService
    //                        and JwtService objects.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerController(CustomerService customerService, JwtService jwtService)
    {
        // Store the customer service dependency
        this.customerService = customerService;

        // Store the JWT service dependency
        this.jwtService = jwtService;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : createCustomer
    //
    //  Description         : Creates a new customer profile in the
    //                        banking portal.
    //
    //                        The customer request is validated before
    //                        being passed to the customer service.
    //
    //                        HTTP CREATED status is returned after
    //                        successful customer creation.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Create customer",
            description = "Create a new customer profile in the banking portal"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO)
    {
        // Create the customer using the customer service
        return customerService.createCustomer(customerRequestDTO);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerById
    //
    //  Description         : Retrieves customer details using the
    //                        customer ID.
    //
    //                        The customer ID is passed to the service
    //                        layer to retrieve the requested customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get customer by ID",
            description = "Returns customer details using the customer ID"
    )
    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable Long id)
    {
        // Retrieve the customer using the provided ID
        return customerService.getCustomerById(id);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : updateCustomer
    //
    //  Description         : Updates customer details using the
    //                        customer ID.
    //
    //                        The request data is validated before
    //                        being passed to the service layer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Update customer",
            description = "Updates customer details using the customer ID"
    )
    @PutMapping("/{id}")
    public CustomerResponseDTO updateCustomer(@PathVariable Long id,
                                              @Valid @RequestBody CustomerRequestDTO customerRequestDTO)
    {
        // Update the customer using the provided ID and request data
        return customerService.updateCustomer(id, customerRequestDTO);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : deleteCustomer
    //
    //  Description         : Deletes a customer using the customer ID.
    //
    //                        HTTP NO_CONTENT status is returned after
    //                        successful deletion.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Delete customer",
            description = "Deletes a customer using the customer ID"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id)
    {
        // Delete the customer using the provided ID
        customerService.deleteCustomer(id);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getMyProfile
    //
    //  Description         : Retrieves the profile details of the
    //                        currently logged-in customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token.
    //
    //                        It uses the email to find the customer
    //                        details from the database.
    //
    //                        The customer profile is returned as a
    //                        response DTO.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get my profile",
            description = "Returns the profile details of the currently authenticated customer"
    )
    @GetMapping("/me")
    public CustomerResponseDTO getMyProfile(@RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Retrieve and return the customer profile using the email
        return customerService.getCustomerByEmail(email);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getAllCustomers
    //
    //  Description         : Retrieves the details of all customers
    //                        from the system.
    //
    //                        It calls the customer service to fetch
    //                        all customer records.
    //
    //                        The customer details are returned as a
    //                        list of response DTOs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get all customers",
            description = "Returns all customers in the banking portal"
    )
    @GetMapping("/all")
    public List<CustomerResponseDTO> getAllCustomers()
    {
        // Retrieve all customers from the customer service
        return customerService.getAllCustomers();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerById
    //
    //  Description         : Retrieves the profile details of a
    //                        customer using the customer ID.
    //
    //                        It extracts the logged-in customer's email
    //                        from the JWT token.
    //
    //                        It identifies the logged-in customer and
    //                        uses the customer ID for authorization.
    //
    //                        The requested customer profile is returned
    //                        after validating the customer's access.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get customer profile",
            description = "Returns the profile details of a customer by customer ID"
    )
    @GetMapping("/profile/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable Long id,
                                               @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the logged-in customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the logged-in customer's profile using the email
        CustomerResponseDTO customer = customerService.getCustomerByEmail(email);

        // Retrieve the requested customer after validating customer access
        return customerService.getCustomerById(id, customer.getId());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : updateMyProfile
    //
    //  Description         : Updates the profile details of the
    //                        currently logged-in customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It verifies the logged-in customer's
    //                        identity before updating the profile.
    //
    //                        The updated customer details are returned
    //                        as a response DTO.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Update customer profile",
            description = "Updates the profile details of the currently authenticated customer"
    )
    @PutMapping("/profile/{id}")
    public CustomerResponseDTO updateMyProfile(@PathVariable Long id,
                                               @Valid @RequestBody CustomerProfileUpdateDTO customerProfileUpdateDTO,
                                               @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the logged-in customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the logged-in customer's profile using the email
        CustomerResponseDTO customer = customerService.getCustomerByEmail(email);

        // Update the requested profile after validating customer access
        return customerService.updateCustomer(id, customer.getId(), customerProfileUpdateDTO);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : deleteMyProfile
    //
    //  Description         : Deletes the profile of the currently
    //                        authenticated customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the logged-in
    //                        customer.
    //
    //                        It verifies the requested customer ID
    //                        belongs to the authenticated customer.
    //
    //                        The customer profile is deleted after
    //                        successful authorization.
    //
    //                        HTTP NO_CONTENT status is returned after
    //                        successful deletion.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Delete customer profile",
            description = "Deletes the profile of the currently authenticated customer"
    )
    @DeleteMapping("/profile/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyProfile(@PathVariable Long id,
                                @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the logged-in customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the logged-in customer's profile using the email
        CustomerResponseDTO customer = customerService.getCustomerByEmail(email);

        // Delete the profile after validating customer access
        customerService.deleteCustomer(id, customer.getId());
    }
}