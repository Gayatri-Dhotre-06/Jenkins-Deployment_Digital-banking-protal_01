package com.banking.common.exception;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : CustomerAccessDeniedException
//
//  Description         : Represents a custom exception used when
//                        a customer tries to access or perform an
//                        operation that they are not authorized to
//                        perform.
//
//                        This exception extends RuntimeException
//                        and is used to handle customer access
//                        authorization failures.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class CustomerAccessDeniedException extends RuntimeException
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : CustomerAccessDeniedException
    //
    //  Description         : Creates a CustomerAccessDeniedException
    //                        with the provided error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public CustomerAccessDeniedException(String message)
    {
        // Pass the error message to the parent RuntimeException class
        super(message);
    }
}