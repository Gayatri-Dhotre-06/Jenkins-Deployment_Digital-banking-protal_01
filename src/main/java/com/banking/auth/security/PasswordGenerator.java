package com.banking.auth.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : PasswordGenerator
//
//  Description         : Generates a BCrypt encrypted password.
//
//                        This utility is used to convert a plain text
//                        password into a BCrypt hash that can be
//                        stored securely in the database.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public class PasswordGenerator
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : main
    //
    //  Description         : Generates a BCrypt hash for the provided
    //                        password and displays the generated hash.
    //
    //                        It creates a BCryptPasswordEncoder,
    //                        encrypts the plain text password, and
    //                        prints the resulting hash.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public static void main(String A[])
    {
        // Create a BCrypt password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Store the plain text password
        String password = "dummy_password_123";

        // Generate a BCrypt hash from the password
        String hash = encoder.encode(password);

        // Display the generated password hash
        System.out.println(hash);
    }
}