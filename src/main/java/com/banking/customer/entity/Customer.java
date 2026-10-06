package com.banking.customer.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : Customer
//
//  Description         : Represents the Customer entity used to
//                        store customer information in the database.
//
//                        The entity is mapped to the "customers"
//                        database table.
//
//                        It stores customer details such as ID,
//                        name, email, and phone number.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

@Entity
@Table(name = "customers")
@JsonPropertyOrder({"id", "name", "email", "phone"})
public class Customer
{
    // Stores the unique ID of the customer
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the customer's name
    private String name;

    // Stores the customer's unique email address
    @Column(unique = true, nullable = false)
    private String email;

    // Stores the customer's phone number
    private String phone;

    public Customer()
    {

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