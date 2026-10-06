package com.banking.customer.dto;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : CustomerResponseDTO
//
//  Description         : Stores the customer details returned as a
//                        response from the customer-related APIs.
//
//                        It contains customer information such as
//                        ID, name, email, and phone number.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public class CustomerResponseDTO
{
    // Stores the unique ID of the customer
    private Long id;

    // Stores the customer's name
    private String name;

    // Stores the customer's email address
    private String email;

    // Stores the customer's phone number
    private String phone;

    public CustomerResponseDTO()
    {

    }

    public CustomerResponseDTO(Long id, String name, String email, String phone)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
