package com.banking.common.exception;

import java.util.Map;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : ValidationErrorResponse
//
//  Description         : Represents the response returned when
//                        validation errors occur in the application.
//
//                        It stores the HTTP status, error message,
//                        and field-level validation errors.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 21/09/2026
//
//////////////////////////////////////////////////////////////////

public class ValidationErrorResponse
{
    // Stores the HTTP status code
    private int status;

    // Stores the general validation error message
    private String message;

    // Stores validation errors for individual fields
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, Map<String, String> errors)
    {
        this.status = status;
        this.message = message;
        this.errors = errors;
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

    public Map<String, String> getErrors()
    {
        return errors;
    }
    public void setErrors(Map<String, String> errors)
    {
        this.errors = errors;
    }
}
