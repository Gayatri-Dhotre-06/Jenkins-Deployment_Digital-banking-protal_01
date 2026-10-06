package com.banking.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


//////////////////////////////////////////////////////////////////
//
//  Class Name          : RegisterRequestDTO
//
//  Description         : Stores the information provided by a
//                        customer during registration.
//
//                        It contains the customer's name, email,
//                        password, and phone number.
//
//                        Validation annotations are used to ensure
//                        that required and valid registration data
//                        is provided.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public class RegisterRequestDTO
{
    // Stores the customer's name
    @NotBlank(message = "Name is required")
    private String name;

    // Stores the customer's email address
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    // Stores the customer's password
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    // Stores the customer's phone number
    private String phone;

    public RegisterRequestDTO()
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

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
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
