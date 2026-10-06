package com.banking.auth.dto;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : RegisterResponseDTO
//
//  Description         : Stores the response returned after a
//                        successful customer registration.
//
//                        It contains the customer's ID, name, email,
//                        and assigned role.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public class RegisterResponseDTO
{
    // Stores the unique ID of the registered customer
    private Long id;

    // Stores the name of the registered customer
    private String name;

    // Stores the email address of the registered customer
    private String email;

    // Stores the role assigned to the registered customer
    private String role;

    public RegisterResponseDTO()
    {

    }

    public RegisterResponseDTO(Long id, String name, String email, String role)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
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

    public String getRole()
    {
        return role;
    }
    public void setRole(String role)
    {
        this.role = role;
    }
}
