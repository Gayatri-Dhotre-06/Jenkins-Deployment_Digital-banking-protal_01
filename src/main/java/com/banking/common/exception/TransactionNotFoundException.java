package com.banking.common.exception;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : TransactionNotFoundException
//
//  Description         : Custom exception used when a requested
//                        transaction cannot be found.
//
//                        This exception extends RuntimeException
//                        and is handled by the global exception
//                        handler.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class TransactionNotFoundException extends RuntimeException
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : TransactionNotFoundException
    //
    //  Description         : Creates a transaction not found
    //                        exception with the given error message.
    //
    //                        The message is passed to the parent
    //                        RuntimeException class.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public TransactionNotFoundException(String message)
    {
        // Pass the error message to the parent exception class
        super(message);
    }
}