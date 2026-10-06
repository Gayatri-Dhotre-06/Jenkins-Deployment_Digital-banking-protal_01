package com.banking.common.exception;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : CustomerNotFoundException
//
//  Description         : Represents a custom exception used when
//                        a customer cannot be found in the banking
//                        system.
//
//                        This exception extends RuntimeException
//                        and is used to handle customer-related
//                        not found situations.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class CustomerNotFoundException extends RuntimeException
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : CustomerNotFoundException
    //
    //  Description         : Creates a CustomerNotFoundException
    //                        with the provided error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerNotFoundException(String message)
    {
        // Pass the error message to the parent RuntimeException class
        super(message);
    }
}