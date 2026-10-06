package com.banking.common.exception;

import com.banking.beneficiary.dto.BeneficiaryResponseDTO;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : BeneficiaryNotFoundException
//
//  Description         : Represents a custom exception used when
//                        a beneficiary cannot be found in the
//                        banking system.
//
//                        This exception extends RuntimeException
//                        and is used to handle beneficiary-related
//                        not found situations.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class BeneficiaryNotFoundException extends RuntimeException
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : BeneficiaryNotFoundException
    //
    //  Description         : Creates a BeneficiaryNotFoundException
    //                        with the provided error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public BeneficiaryNotFoundException(String message)
    {
        // Pass the error message to the parent RuntimeException class
        super(message);
    }
}