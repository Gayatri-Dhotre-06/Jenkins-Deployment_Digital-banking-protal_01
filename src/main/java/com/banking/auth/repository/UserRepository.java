package com.banking.auth.repository;

import com.banking.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//////////////////////////////////////////////////////////////////
//
//  Interface Name       : UserRepository
//
//  Description          : Provides database operations for the
//                         User entity.
//
//                         It extends JpaRepository to provide
//                         built-in CRUD operations and defines
//                         custom methods for searching users
//                         by email.
//
//  Author               : Shubham Somanath Gadhe
//
//  Date                 : 20/09/2026
//
//////////////////////////////////////////////////////////////////

public interface UserRepository extends JpaRepository<User, Long>
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByEmail
    //
    //  Description         : Finds a user using the user's email
    //                        address.
    //
    //                        It returns an Optional containing the
    //                        user if a matching email is found.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    Optional<User> findByEmail(String email);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : existsByEmail
    //
    //  Description         : Checks whether a user already exists
    //                        with the given email address.
    //
    //                        It returns true when the email exists
    //                        and false when it does not exist.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    boolean existsByEmail(String email);
}