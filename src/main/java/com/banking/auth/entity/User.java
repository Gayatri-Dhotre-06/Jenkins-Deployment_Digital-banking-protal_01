package com.banking.auth.entity;

import jakarta.persistence.*;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : User
//
//  Description         : Represents a user in the authentication
//                        system.
//
//                        It stores user information such as name,
//                        email, password, and role.
//
//                        The class is mapped to the "users" table
//                        in the database.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

@Entity
@Table(name = "users")
public class User
{
    // Stores the unique ID of the user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the name of the user
    private String name;

    // Stores the unique email address of the user
    @Column(unique = true, nullable = false)
    private String email;

    // Stores the encrypted password of the user
    @Column(nullable = false)
    private String password;

    // Stores the role assigned to the user
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User()
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

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public Role getRole()
    {
        return role;
    }
    public void setRole(Role role)
    {
        this.role = role;
    }
}
