//////////////////////////////////////////////////////////////////
//
//  Class Name          : CustomerService
//
//  Description         : Provides business logic for customer
//                        management in the Digital Banking Portal.
//
//                        It handles customer creation, retrieval,
//                        updating, and deletion.
//
//                        It also verifies customer access when an
//                        authenticated customer accesses, updates,
//                        or deletes their own profile.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.customer.service;

import com.banking.common.exception.CustomerNotFoundException;
import com.banking.common.exception.CustomerAccessDeniedException;
import com.banking.customer.dto.CustomerProfileUpdateDTO;
import com.banking.customer.dto.CustomerRequestDTO;
import com.banking.customer.dto.CustomerResponseDTO;
import com.banking.customer.entity.Customer;
import com.banking.customer.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CustomerService
{
    // Stores the repository used to perform customer database operations
    private final CustomerRepository customerRepository;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : CustomerService
    //
    //  Description         : Initializes the CustomerService with the
    //                        CustomerRepository dependency.
    //
    //                        Constructor-based dependency injection
    //                        is used to provide the repository object.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerService(CustomerRepository customerRepository)
    {
        // Store the customer repository dependency
        this.customerRepository = customerRepository;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : createCustomer
    //
    //  Description         : Creates a new customer in the system.
    //
    //                        It creates a Customer entity, copies the
    //                        request details into the entity, saves it
    //                        in the database, and returns the saved
    //                        customer as a response DTO.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO)
    {
        // Create a new customer entity
        Customer customer = new Customer();

        // Set the customer's name from the request
        customer.setName(customerRequestDTO.getName());

        // Set the customer's email from the request
        customer.setEmail(customerRequestDTO.getEmail());

        // Set the customer's phone number from the request
        customer.setPhone(customerRequestDTO.getPhone());

        // Save the customer in the database
        Customer savedCustomer = customerRepository.save(customer);

        // Convert the saved customer into a response DTO
        return new CustomerResponseDTO(savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerById
    //
    //  Description         : Retrieves customer details using the
    //                        customer ID.
    //
    //                        If the customer does not exist, a
    //                        CustomerNotFoundException is thrown.
    //
    //                        The customer details are converted into
    //                        a response DTO and returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerResponseDTO getCustomerById(Long id)
    {
        // Find the customer using the given ID
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with id : " + id));

        // Convert customer details into a response DTO
        return new CustomerResponseDTO(customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getAllCustomers
    //
    //  Description         : Retrieves all customers from the system.
    //
    //                        It fetches all customer records from the
    //                        database and converts each customer into
    //                        a response DTO.
    //
    //                        The response DTOs are returned as a list.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<CustomerResponseDTO> getAllCustomers()
    {
        // Find all customers from the database
        return customerRepository.findAll().stream()
                // Convert each customer entity into a response DTO
                .map(customer -> new CustomerResponseDTO(customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone()))
                // Convert the stream into a list
                .toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : updateCustomer
    //
    //  Description         : Updates the details of an existing
    //                        customer.
    //
    //                        It finds the customer using the given ID,
    //                        updates the name and phone number, saves
    //                        the changes, and returns the updated
    //                        customer as a response DTO.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerResponseDTO updateCustomer(Long id, @Valid CustomerRequestDTO customerRequestDTO)
    {
        // Find the existing customer using the given ID
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with id : " + id));

        // Update the customer's name
        existingCustomer.setName(customerRequestDTO.getName());

        // Update the customer's phone number
        existingCustomer.setPhone(customerRequestDTO.getPhone());

        // Save the updated customer in the database
        Customer updatedCustomer = customerRepository.save(existingCustomer);

        // Convert the updated customer into a response DTO
        return new CustomerResponseDTO(updatedCustomer.getId(),
                updatedCustomer.getName(),
                updatedCustomer.getEmail(),
                updatedCustomer.getPhone());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : deleteCustomer
    //
    //  Description         : Deletes an existing customer from the
    //                        system.
    //
    //                        It first verifies that the customer exists
    //                        and then deletes the customer from the
    //                        database.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public void deleteCustomer(Long id)
    {
        // Find the existing customer using the given ID
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with id " + id));

        // Delete the customer from the database
        customerRepository.delete(existingCustomer);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerByEmail
    //
    //  Description         : Retrieves customer details using the
    //                        customer's email address.
    //
    //                        It searches for the customer in the
    //                        database using the given email.
    //
    //                        If the customer is not found, a
    //                        CustomerNotFoundException is thrown.
    //
    //                        The customer details are converted into
    //                        a response DTO and returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerResponseDTO getCustomerByEmail(String email)
    {
        // Find the customer using the provided email
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found"));

        // Convert customer details into a response DTO
        return new CustomerResponseDTO(customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerById
    //
    //  Description         : Retrieves customer details using the
    //                        customer ID.
    //
    //                        It finds the customer from the database
    //                        using the given ID.
    //
    //                        It checks whether the requested customer
    //                        is the same as the logged-in customer.
    //
    //                        If the customer is not authorized to access
    //                        the profile, an exception is thrown.
    //
    //                        The customer details are converted into a
    //                        response DTO and returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerResponseDTO getCustomerById(Long id, Long loggedInCustomerId)
    {
        // Find the customer using the given customer ID
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with id :  " + id));

        // Check whether the requested customer is the logged-in customer
        if(!customer.getId().equals(loggedInCustomerId))
        {
            // Reject access if the customer IDs do not match
            throw new CustomerAccessDeniedException("You are not authorized to access this customer");
        }

        // Convert customer details into a response DTO
        return new CustomerResponseDTO(customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : updateCustomer
    //
    //  Description         : Updates the profile details of an existing
    //                        customer.
    //
    //                        It finds the customer using the given ID
    //                        and verifies that the customer belongs to
    //                        the logged-in user.
    //
    //                        It updates the customer's name, email,
    //                        and phone number with the provided details.
    //
    //                        The updated customer is saved in the database
    //                        and returned as a response DTO.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerResponseDTO updateCustomer(Long id,
                                              Long loggedInCustomerId,
                                              CustomerProfileUpdateDTO customerProfileUpdateDTO)
    {
        // Find the existing customer using the given customer ID
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with id : " + id));

        // Check whether the requested customer is the logged-in customer
        if(!existingCustomer.getId().equals(loggedInCustomerId))
        {
            // Reject the update if the customer IDs do not match
            throw new CustomerAccessDeniedException("You are not authorized to update this customer");
        }

        // Update the customer's name
        existingCustomer.setName(customerProfileUpdateDTO.getName());

        // Update the customer's email
        existingCustomer.setEmail(customerProfileUpdateDTO.getEmail());

        // Update the customer's phone number
        existingCustomer.setPhone(customerProfileUpdateDTO.getPhone());

        // Save the updated customer in the database
        Customer updatedCustomer = customerRepository.save(existingCustomer);

        // Convert the updated customer into a response DTO
        return new CustomerResponseDTO(updatedCustomer.getId(),
                updatedCustomer.getName(),
                updatedCustomer.getEmail(),
                updatedCustomer.getPhone());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : deleteCustomer
    //
    //  Description         : Deletes an existing customer from the
    //                        system.
    //
    //                        It finds the customer using the given ID
    //                        and verifies that the customer is the same
    //                        as the logged-in customer.
    //
    //                        If the customer is not authorized to delete
    //                        the profile, an exception is thrown.
    //
    //                        The customer is deleted from the database
    //                        after successful authorization.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public void deleteCustomer(Long id, Long loggedInCustomerId)
    {
        // Find the existing customer using the given customer ID
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with id : " + id));

        // Check whether the requested customer is the logged-in customer
        if(!existingCustomer.getId().equals(loggedInCustomerId))
        {
            // Reject the deletion if the customer IDs do not match
            throw new CustomerAccessDeniedException("You are not authorized to delete this customer");
        }

        // Delete the customer from the database
        customerRepository.delete(existingCustomer);
    }
}