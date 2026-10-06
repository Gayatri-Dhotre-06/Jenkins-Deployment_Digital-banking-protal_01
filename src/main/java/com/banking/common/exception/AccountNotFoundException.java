package com.banking.common.exception;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : AccountNotFoundException
//
//  Description         : Represents a custom exception used when
//                        an account or related customer cannot be
//                        found in the banking system.
//
//                        This exception extends RuntimeException
//                        and is used to handle account-related
//                        not found situations.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class AccountNotFoundException extends RuntimeException
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : AccountNotFoundException
    //
    //  Description         : Creates an AccountNotFoundException
    //                        with the provided error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AccountNotFoundException(String message)
    {
        // Pass the error message to the parent RuntimeException class
        super(message);
    }
}