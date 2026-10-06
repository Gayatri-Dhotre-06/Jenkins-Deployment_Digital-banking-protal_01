package com.banking.beneficiary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banking.beneficiary.entity.Beneficiary;

import java.util.List;
import java.util.Optional;

//////////////////////////////////////////////////////////////////
//
//  Interface Name       : BeneficiaryRepository
//
//  Description          : Provides database operations for the
//                        Beneficiary entity.
//
//                        It extends JpaRepository to provide
//                        standard CRUD operations and defines
//                        custom methods for finding beneficiaries
//                        using customer ID and account number.
//
//  Author               : Shubham Somanath Gadhe
//
//  Date                 : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByCustomerId
    //
    //  Description         : Returns all beneficiaries associated
    //                        with the specified customer ID.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    List<Beneficiary> findByCustomerId(Long customerId);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : existsByCustomerIdAndAccountNumber
    //
    //  Description         : Checks whether a beneficiary with the
    //                        specified customer ID and account number
    //                        already exists.
    //
    //                        It helps prevent the same beneficiary
    //                        account from being added multiple times
    //                        for the same customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    boolean existsByCustomerIdAndAccountNumber(Long customerId, String accountNumber);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByIdAndCustomerId
    //
    //  Description         : Finds a beneficiary using both the
    //                        beneficiary ID and customer ID.
    //
    //                        It ensures that the beneficiary belongs
    //                        to the specified customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    Optional<Beneficiary> findByIdAndCustomerId(Long id, Long customerId);
}