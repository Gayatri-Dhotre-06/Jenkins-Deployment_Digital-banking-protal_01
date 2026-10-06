package com.banking.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : LoginRequestDTO
//
//  Description         : Stores the login credentials provided by
//                        the customer.
//
//                        It contains the customer's email and
//                        password with validation rules to ensure
//                        that valid login data is provided.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public class LoginRequestDTO
{
    // Set the customer's email address
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    // Set the customer's password
    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequestDTO()
    {

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
}
