package com.banking.auth.dto;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : LoginResponseDTO
//
//  Description         : Stores the response returned after a
//                        successful customer login.
//
//                        It contains the login message, customer
//                        email, customer role, and JWT token.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public class LoginResponseDTO
{
    // Set the login response message
    private String message;

    // Set the customer's email
    private String email;

    // Set the customer's role
    private String role;

    // Set the generated JWT token
    private String token;

    public LoginResponseDTO()
    {

    }

    public LoginResponseDTO(String message, String email, String role, String token)
    {
        this.message = message;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getRole()
    {
        return role;
    }
    public void setRole(String role)
    {
        this.role = role;
    }

    public String getToken()
    {
        return token;
    }
    public void setToken(String token)
    {
        this.token = token;
    }
}
