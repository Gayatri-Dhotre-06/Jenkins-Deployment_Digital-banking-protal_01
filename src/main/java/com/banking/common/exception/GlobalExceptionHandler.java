package com.banking.common.exception;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : GlobalExceptionHandler
//
//  Description         : Handles exceptions generated throughout
//                        the Digital Banking Portal application.
//
//                        It provides centralized exception handling
//                        for validation errors, not found errors,
//                        invalid requests, authorization failures,
//                        optimistic locking failures, and unsupported
//                        HTTP methods.
//
//                        It returns appropriate HTTP status codes
//                        and error responses to the client.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 21/09/2026
//
//////////////////////////////////////////////////////////////////

@RestControllerAdvice
public class GlobalExceptionHandler
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleCustomerNotFound
    //
    //  Description         : Handles exceptions that occur when a
    //                        requested customer is not found.
    //
    //                        It creates an error response containing
    //                        the HTTP status code and exception message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCustomerNotFound(CustomerNotFoundException exception)
    {
        // Create a map to store the error response
        Map<String, Object> responce = new HashMap<>();

        // Add the HTTP status code to the response
        responce.put("status", 404);

        // Add the exception message to the response
        responce.put("message", exception.getMessage());

        // Return the error response
        return responce;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleValidationErrors
    //
    //  Description         : Handles validation errors that occur when
    //                        request data does not satisfy the required
    //                        validation rules.
    //
    //                        It collects the validation errors for each
    //                        field and stores the field name along with
    //                        its corresponding error message.
    //
    //                        The validation errors are returned with an
    //                        HTTP BAD_REQUEST response.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationErrors(MethodArgumentNotValidException exception)
    {
        // Create a map to store field validation errors
        Map<String, String> errors = new HashMap<>();

        // Collect each field name and its validation error message
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        // Return the validation error response
        return new ValidationErrorResponse(400, "Validation failed", errors);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleAccountNotFound
    //
    //  Description         : Handles exceptions that occur when a
    //                        requested account is not found.
    //
    //                        It returns an HTTP NOT_FOUND response
    //                        containing the status code and error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccountNotFound(AccountNotFoundException exception)
    {
        // Create and return the account not found response
        return new ErrorResponse(404, exception.getMessage());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleIllegalArgument
    //
    //  Description         : Handles exceptions caused by invalid
    //                        arguments or invalid input values.
    //
    //                        It returns an HTTP BAD_REQUEST response
    //                        containing the status code and error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException exception)
    {
        // Return a response for invalid requests or operations
        return new ErrorResponse(400,"Invalid request or operation");
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleBeneficiaryNotFound
    //
    //  Description         : Handles exceptions that occur when a
    //                        requested beneficiary is not found.
    //
    //                        It returns an HTTP NOT_FOUND response
    //                        containing the status code and error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(BeneficiaryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBeneficiaryNotFound(BeneficiaryNotFoundException exception)
    {
        // Create and return the beneficiary not found response
        return new ErrorResponse(404, exception.getMessage());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleTransactionNotFound
    //
    //  Description         : Handles exceptions that occur when a
    //                        requested transaction is not found.
    //
    //                        It returns an HTTP NOT_FOUND response
    //                        containing the status code and error message.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTransactionNotFound(TransactionNotFoundException exception)
    {
        // Create and return the transaction not found response
        return new ErrorResponse(404, exception.getMessage());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleOptimisticLock
    //
    //  Description         : Handles optimistic locking exceptions that
    //                        occur when an account is modified by another
    //                        transaction.
    //
    //                        It returns an HTTP CONFLICT response with
    //                        an error message asking the customer to
    //                        try the operation again.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOptimisticLock(OptimisticLockException exception)
    {
        // Return a conflict response for the optimistic locking failure
        return new ErrorResponse(409, "Account was modified by another transaction. Please try again.");
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleObjectOptimisticLocking
    //
    //  Description         : Handles optimistic locking failures when
    //                        an account is modified by another transaction.
    //
    //                        It returns an HTTP CONFLICT response with
    //                        an appropriate error message asking the
    //                        customer to try the operation again.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleObjectOptimisticLocking(ObjectOptimisticLockingFailureException exception)
    {
        // Return a conflict response for the optimistic locking failure
        return new ErrorResponse(409, "Account was modified by another transaction. Please try again.");
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleHttpMessageNotReadable
    //
    //  Description         : Handles errors caused by an invalid or
    //                        unreadable request body.
    //
    //                        It returns an HTTP BAD_REQUEST response
    //                        with an appropriate error message when
    //                        invalid input or values are provided.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception)
    {
        // Return a response for invalid or unreadable request data
        return new ErrorResponse(400, "Invalid request body or invalid value provided");
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleNoResourceFound
    //
    //  Description         : Handles requests for endpoints that do not
    //                        exist in the application.
    //
    //                        It returns an HTTP NOT_FOUND response with
    //                        an appropriate error message indicating
    //                        that the requested endpoint was not found.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoResourceFound(NoResourceFoundException exception)
    {
        // Return a response indicating that the endpoint was not found
        return new ErrorResponse(404, "The requested endpoint was not found");
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleTypeMismatch
    //
    //  Description         : Handles errors caused by an invalid value
    //                        provided for a request parameter.
    //
    //                        It returns an HTTP BAD_REQUEST response
    //                        with an error message containing the name
    //                        of the parameter with the invalid value.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException exception)
    {
        // Return a response containing the invalid parameter name
        return new ErrorResponse(400, "Invalid value provided for parameter : " + exception.getName());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleMissingRequestParameter
    //
    //  Description         : Handles requests where a required request
    //                        parameter is missing.
    //
    //                        It returns an HTTP BAD_REQUEST response
    //                        with an error message containing the name
    //                        of the missing request parameter.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestParameter(MissingServletRequestParameterException exception)
    {
        // Return a response containing the missing parameter name
        return new ErrorResponse(400, "Required paramter is missing : " + exception.getParameterName());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleMissingRequestHeader
    //
    //  Description         : Handles requests where a required request
    //                        header is missing.
    //
    //                        It returns an HTTP BAD_REQUEST response
    //                        with an error message containing the name
    //                        of the missing request header.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeader(MissingRequestHeaderException exception)
    {
        // Return a response indicating that the required header is missing
        return new ErrorResponse(400, "Required request header is missing");
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleMethodNotSupported
    //
    //  Description         : Handles requests where the HTTP method is
    //                        not supported by the requested endpoint.
    //
    //                        It returns an HTTP METHOD_NOT_ALLOWED
    //                        response with an error message containing
    //                        the unsupported HTTP method.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 21/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotSupported(HttpRequestMethodNotSupportedException exception)
    {
        // Return a response containing the unsupported HTTP method
        return new ErrorResponse(405,"HTTP method " + exception.getMethod() + " is not supported for this endpoint");
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : handleCustomerAccessDenied
    //
    //  Description         : Handles authorization failures when a
    //                        customer tries to access another customer's
    //                        profile.
    //
    //                        It returns an HTTP FORBIDDEN response.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 29/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @ExceptionHandler(CustomerAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleCustomerAccessDenied(CustomerAccessDeniedException exception)
    {
        // Return a forbidden response for unauthorized customer access
        return new ErrorResponse(403, "You are not authorized to access this customer");
    }
}