//////////////////////////////////////////////////////////////////
//
//  Interface Name       : AccountRepository
//
//  Description          : Provides database operations for the
//                        Account entity.
//
//                        It extends JpaRepository to perform common
//                        CRUD operations on account records.
//
//                        It also provides methods to find accounts
//                        by account number and customer ID.
//
//  Author               : Shubham Somanath Gadhe
//
//  Date                 : 20/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.account.repository;

import com.banking.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long>
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByAccountNumber
    //
    //  Description         : Finds an account using its account
    //                        number.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    Optional<Account> findByAccountNumber(String accountNumber);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : existsByAccountNumber
    //
    //  Description         : Checks whether an account with the
    //                        given account number already exists.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    boolean existsByAccountNumber(String accountNumber);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByCustomerId
    //
    //  Description         : Retrieves all accounts belonging to
    //                        the specified customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    List<Account> findByCustomerId(Long customerId);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByAccountNumberAndCustomerId
    //
    //  Description         : Finds an account using both the account
    //                        number and customer ID.
    //
    //                        This helps verify that the account
    //                        belongs to the specified customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    Optional<Account> findByAccountNumberAndCustomerId(String accountNumber, Long customerId);
}