package com.banking.transaction.controller;

import com.banking.transaction.dto.TransactionResponseDTO;
import com.banking.transaction.dto.TransferRequestDTO;
import com.banking.transaction.entity.TransactionStatus;
import com.banking.transaction.entity.TransactionType;
import com.banking.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.banking.auth.security.JwtService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : TransactionController
//
//  Description         : Handles REST API requests related to
//                        money transfers and transaction management.
//
//                        It provides APIs to transfer money,
//                        retrieve transaction history, filter
//                        transactions, and retrieve transaction
//                        details for authenticated customers.
//
//                        JWT authentication is used to identify
//                        the logged-in customer.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

@RestController
@RequestMapping("/api/transactions")
@Tag(
        name = "Transaction",
        description = "APIs for money transfer and transaction management"
)
public class TransactionController
{
    // Provides transaction-related business operations
    private final TransactionService transactionService;

    // Extracts customer information from the JWT token
    private final JwtService jwtService;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : TransactionController
    //
    //  Description         : Initializes the transaction controller
    //                        using constructor-based dependency
    //                        injection.
    //
    //                        It receives the transaction service
    //                        and JWT service required by the
    //                        controller methods.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public TransactionController(TransactionService transactionService, JwtService jwtService)
    {
        // Store the transaction service
        this.transactionService = transactionService;

        // Store the JWT service
        this.jwtService = jwtService;
    }

    //#1
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : transferMoney
    //
    //  Description         : Transfers money from the sender account
    //                        to the receiver account.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It validates the transfer request and
    //                        sends it to the service for processing.
    //
    //                        The transaction details are returned with
    //                        HTTP CREATED status.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Transfer money",
            description = "Transfers money from the sender account to the receiver account for the authenticated customer"
    )
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO transferMoney(@RequestHeader ("Authorization") String authorizationHeader,
                                                @Valid @RequestBody TransferRequestDTO request)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Process the money transfer for the customer
        return transactionService.transferMoney(request, customerId);
    }

    //#2
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionHistory
    //
    //  Description         : Retrieves the transaction history of the
    //                        logged-in customer with pagination and
    //                        sorting.
    //
    //                        It validates the page number and page size
    //                        before processing the request.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It validates the sorting direction and
    //                        creates pagination details with the
    //                        selected sorting field and direction.
    //
    //                        The customer's transaction history is
    //                        returned as a paginated result.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get transaction history",
            description = "Returns the authenticated customer's transaction history with pagination and sorting")
    @GetMapping("/history")
    public Page<TransactionResponseDTO> getTransactionHistory(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "transactionDate") String sortBy,
                                                              @RequestParam(defaultValue = "desc") String direction,
                                                              @RequestHeader("Authorization") String authorizationHeader)
    {
        // Validate that the page number is not negative
        if(page < 0)
        {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        // Validate that the page size is between 1 and 100
        if(size <= 0 || size > 100)
        {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }

        // Define the fields that are allowed for sorting
        List<String> allowedSortFields = List.of("transactionDate",
                "amount",
                "id",
                "referenceNumber",
                "status",
                "transactionType");

        // Validate that the requested sort field is allowed
        if(!allowedSortFields.contains(sortBy))
        {
            throw new IllegalArgumentException("Invalid sort field");
        }

        // Store the sorting direction
        Sort.Direction sortDirection;

        try
        {
            // Convert the direction string into a Sort.Direction value
            sortDirection = Sort.Direction.fromString(direction);
        }
        catch(IllegalArgumentException exception)
        {
            // Reject invalid sorting directions
            throw new IllegalArgumentException("Sort direction must be asc or desc");
        }

        // Create pagination and sorting details
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Get the customer's paginated transaction history
        return transactionService.getCustomerTransactionHistory(customerId, pageable);
    }

    //#3
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionById
    //
    //  Description         : Retrieves the details of a specific
    //                        transaction for the logged-in customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It retrieves the transaction after checking
    //                        that it belongs to the customer.
    //
    //                        The transaction details are returned to
    //                        the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get transaction by ID",
            description = "Returns details of a specific transaction belonging to the authenticated customer")
    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Get the transaction details for the customer
        return transactionService.getTransactionById(id, customerId);
    }

    //#4
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionHistoryByAccountNumber
    //
    //  Description         : Retrieves the transaction history of a
    //                        specific account with pagination.
    //
    //                        It validates the page number and page size
    //                        before processing the request.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It verifies that the account belongs to
    //                        the logged-in customer.
    //
    //                        It creates pagination details and retrieves
    //                        the account's transaction history.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get account transaction history",
            description = "Returns the transaction history of an account belonging to the authenticated customer with pagination")
    @GetMapping("/history/account/{accountNumber}")
    public Page<TransactionResponseDTO> getTransactionHistoryByAccountNumber(@PathVariable String accountNumber,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestHeader("Authorization") String authorizationHeader)
    {
        // Validate that the page number is not negative
        if(page < 0)
        {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        // Validate that the page size is between 1 and 100
        if(size <= 0 || size > 100)
        {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }

        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Verify that the account belongs to the customer
        transactionService.validateAccountOwnership(accountNumber, customerId);

        // Create pagination details using page number and page size
        Pageable pageable = PageRequest.of(page, size);

        // Get the account's paginated transaction history
        return transactionService.getTransactionHistoryByAccountNumber(accountNumber, pageable);
    }

    //#5
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionByType
    //
    //  Description         : Retrieves transactions of a specific type
    //                        for the logged-in customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It retrieves the customer's transactions
    //                        based on the given transaction type.
    //
    //                        The transaction details are returned as
    //                        a list of response DTOs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get transaction by type",
            description = "Returns the authenticated customer's transactions filtered by transaction type")
    @GetMapping("/filter/type")
    public List<TransactionResponseDTO> getTransactionByType(@RequestParam TransactionType transactionType,
                                                             @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Get the customer's transactions by transaction type
        return transactionService.getCustomerTransactionsByType(customerId, transactionType);
    }

    //#6
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionByStatus
    //
    //  Description         : Retrieves transactions with a specific
    //                        status for the logged-in customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It retrieves the customer's transactions
    //                        based on the given transaction status.
    //
    //                        The transaction details are returned as
    //                        a list of response DTOs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get transaction by status",
            description = "Returns the authenticated customer's transactions filtered by transaction status")
    @GetMapping("/filter/status")
    public List<TransactionResponseDTO> getTransactionByStatus(@RequestParam TransactionStatus status,
                                                               @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Get the customer's transactions by transaction status
        return transactionService.getCustomerTransactionsByStatus(customerId, status);
    }

    //#7
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionsByAccountNumberAndType
    //
    //  Description         : Retrieves transactions of a specific type
    //                        for the selected account.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It verifies that the account belongs to
    //                        the logged-in customer.
    //
    //                        It retrieves the account transactions based
    //                        on the given transaction type.
    //
    //                        The transaction details are returned as
    //                        a list of response DTOs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get account transaction by type",
            description = "Returns transactions of a specific type for an account belonging to the authenticated customer")
    @GetMapping("/filter/account/{accountNumber}")
    public List<TransactionResponseDTO> getTransactionsByAccountNumberAndType(@PathVariable String accountNumber,
                                                                              @RequestParam TransactionType transactionType,
                                                                              @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Verify that the account belongs to the customer
        transactionService.validateAccountOwnership(accountNumber, customerId);

        // Get account transactions by transaction type
        return transactionService.getTransactionsByAccountNumberAndType(accountNumber, transactionType);
    }

    //#8
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionsByDateRange
    //
    //  Description         : Retrieves transactions of the logged-in
    //                        customer within a specific date range.
    //
    //                        It validates that the start date is not
    //                        after the end date.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It retrieves the customer's transactions
    //                        between the given start and end dates.
    //
    //                        The transaction details are returned as
    //                        a list of response DTOs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get transactions by date range",
            description = "Returns the authenticated customer's transactions within the specific date range")
    @GetMapping("/filter/date")
    public List<TransactionResponseDTO> getTransactionsByDateRange(@RequestParam LocalDateTime startDate,
                                                                   @RequestParam LocalDateTime endDate,
                                                                   @RequestHeader("Authorization") String authorizationHeader)
    {
        // Validate that the start date is not after the end date
        if(startDate.isAfter(endDate))
        {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer ID using the extracted email
        Long customerId = transactionService.getCustomerIdByEmail(email);

        // Get the customer's transactions within the given date range
        return transactionService.getCustomerTransactionByDateAndRange(customerId, startDate, endDate);
    }
}