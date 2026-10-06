package com.banking.common.exception;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : ErrorResponse
//
//  Description         : Represents the standard error response
//                        returned to the client when an exception
//                        occurs in the banking application.
//
//                        It contains the HTTP status code and
//                        corresponding error message.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 21/09/2026
//
//////////////////////////////////////////////////////////////////

public class ErrorResponse
{
    // Stores the HTTP status code of the error response
    private int status;

    // Stores the error message returned to the client
    private String message;

    public ErrorResponse(int status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
}
