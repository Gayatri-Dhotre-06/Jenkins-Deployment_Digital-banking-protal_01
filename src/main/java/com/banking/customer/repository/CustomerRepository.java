//////////////////////////////////////////////////////////////////
//
//  Interface Name      : CustomerRepository
//
//  Description         : Provides database operations for the
//                        Customer entity.
//
//                        It extends JpaRepository to use the standard
//                        CRUD operations provided by Spring Data JPA.
//
//                        It also provides a custom method to find a
//                        customer using the customer's email address.
//
//  Author               : Shubham Somanath Gadhe
//
//  Date                 : 20/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.customer.repository;

import com.banking.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByEmail
    //
    //  Description         : Finds a customer using the customer's
    //                        email address.
    //
    //                        Spring Data JPA automatically generates
    //                        the database query based on the method
    //                        name.
    //
    //                        Optional is used because a customer with
    //                        the given email may or may not exist.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    Optional<Customer> findByEmail(String email);
}