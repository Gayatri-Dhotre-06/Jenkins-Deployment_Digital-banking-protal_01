package com.banking.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : CustomerRequestDTO
//
//  Description         : Stores the customer details received when
//                        creating or updating a customer.
//
//                        It contains validation rules to ensure that
//                        the name, email, and phone fields are provided
//                        with valid values.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public class CustomerRequestDTO
{
    // Stores the customer's name
    @NotBlank(message = "Name is required")
    private String name;

    // Stores the customer's email address
    @NotBlank(message = "Email is required")
    @Email(message = "Enter the valid email address")
    private String email;

    // Stores the customer's phone number
    @NotBlank(message = "Phone is required")
    private String phone;


    public CustomerRequestDTO()
    {

    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
