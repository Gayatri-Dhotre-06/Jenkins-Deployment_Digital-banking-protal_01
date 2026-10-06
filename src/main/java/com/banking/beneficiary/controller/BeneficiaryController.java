package com.banking.beneficiary.controller;

import com.banking.beneficiary.dto.BeneficiaryRequestDTO;
import com.banking.beneficiary.dto.BeneficiaryResponseDTO;
import com.banking.beneficiary.dto.BeneficiaryTransferRequestDTO;
import com.banking.beneficiary.service.BeneficiaryService;
import com.banking.customer.entity.Customer;
import com.banking.transaction.dto.TransactionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.banking.auth.security.JwtService;
import com.banking.transaction.service.TransactionService;

import java.util.List;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : BeneficiaryController
//
//  Description         : Provides REST APIs for beneficiary
//                        management and beneficiary transactions.
//
//                        It allows authenticated customers to add,
//                        view, update, and delete beneficiaries.
//
//                        It also provides an API to transfer money
//                        to a beneficiary and retrieve beneficiary
//                        related transactions.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

@RestController
@RequestMapping("/api/beneficiaries")
@Tag(
        name = "Beneficiary",
        description = "APIs for beneficiary management and beneficiary transactions"
)
public class BeneficiaryController
{
    // Provides business logic for beneficiary operations
    private final BeneficiaryService beneficiaryService;

    // Extracts customer information from JWT tokens
    private final JwtService jwtService;

    // Provides business logic for transaction operations
    private final TransactionService transactionService;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : BeneficiaryController
    //
    //  Description         : Initializes the BeneficiaryController
    //                        with all required service dependencies.
    //
    //                        The dependencies are used for beneficiary,
    //                        JWT authentication, and transaction
    //                        operations.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public BeneficiaryController(BeneficiaryService beneficiaryService,
                                 JwtService jwtService,
                                 TransactionService transactionService)
    {
        // Initialize BeneficiaryService dependency
        this.beneficiaryService = beneficiaryService;

        // Initialize JwtService dependency
        this.jwtService = jwtService;

        // Initialize TransactionService dependency
        this.transactionService = transactionService;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : addBeneficiary
    //
    //  Description         : Adds a new beneficiary for the logged-in
    //                        customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It sends the beneficiary details to the
    //                        service to create the beneficiary.
    //
    //                        The newly created beneficiary details are
    //                        returned with HTTP CREATED status.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Add beneficiary",
            description = "Adds a new beneficiary for the currently authenticated customer"
    )
    @PostMapping
    public ResponseEntity<BeneficiaryResponseDTO> addBeneficiary(@RequestHeader("Authorization") String authorizationHeader,
                                                                 @Valid @RequestBody BeneficiaryRequestDTO request)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer using the extracted email
        Customer customer =beneficiaryService.getCustomerFromEmail(email);

        // Add the beneficiary for the customer
        BeneficiaryResponseDTO response = beneficiaryService.addBeneficiary(customer.getId(), request);

        // Return the created beneficiary with HTTP CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getBeneficiaries
    //
    //  Description         : Retrieves all beneficiaries of the
    //                        logged-in customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It gets all beneficiaries belonging to
    //                        the customer.
    //
    //                        The list of beneficiary details is
    //                        returned with HTTP OK status.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get beneficiary",
            description = "Returns all beneficiary belonging to the currently authenticated customer"
    )
    @GetMapping
    public ResponseEntity<List<BeneficiaryResponseDTO>> getBeneficiaries(@RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer using the extracted email
        Customer customer = beneficiaryService.getCustomerFromEmail(email);

        // Get all beneficiaries belonging to the customer
        List<BeneficiaryResponseDTO> beneficiaries = beneficiaryService.getBeneficiaryByCustomer(customer.getId());

        // Return the beneficiary list with HTTP OK status
        return ResponseEntity.ok(beneficiaries);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : updateBeneficiary
    //
    //  Description         : Updates the details of an existing
    //                        beneficiary.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It updates the beneficiary after verifying
    //                        that it belongs to the customer.
    //
    //                        The updated beneficiary details are
    //                        returned with HTTP OK status.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Update beneficiary",
            description = "Updates the details of an existing beneficiary belonging to the authenticated customer"
    )
    @PutMapping("/{beneficiaryId}")
    public ResponseEntity <BeneficiaryResponseDTO> updateBeneficiary(@PathVariable Long beneficiaryId,
                                                                     @RequestHeader("Authorization") String authorizationHeader,
                                                                     @Valid @RequestBody BeneficiaryRequestDTO request)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer using the extracted email
        Customer customer = beneficiaryService.getCustomerFromEmail(email);

        // Update the beneficiary for the customer
        BeneficiaryResponseDTO response = beneficiaryService.updateBeneficiary(beneficiaryId, customer.getId(), request);

        // Return the updated beneficiary with HTTP OK status
        return ResponseEntity.ok(response);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : deleteBeneficiary
    //
    //  Description         : Deletes an existing beneficiary of the
    //                        logged-in customer.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It deletes the beneficiary after verifying
    //                        that it belongs to the customer.
    //
    //                        A no-content response is returned after
    //                        successful deletion.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Delete beneficiary",
            description = "Deletes an existing beneficiary belonging to the authenticated customer"
    )
    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long beneficiaryId, @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer using the extracted email
        Customer customer = beneficiaryService.getCustomerFromEmail(email);

        // Delete the beneficiary for the customer
        beneficiaryService.deleteBeneficiary(beneficiaryId, customer.getId());

        // Return no-content response after successful deletion
        return ResponseEntity.noContent().build();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : transferToBeneficiary
    //
    //  Description         : Transfers money from the customer's
    //                        account to the beneficiary's account.
    //
    //                        The customer's email is taken from the
    //                        JWT token to identify the customer.
    //
    //                        The transfer is processed by the
    //                        BeneficiaryService, and the transaction
    //                        response is returned with HTTP CREATED.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Transfer money to the beneficiary",
            description = "Transfers money from the authenticated customer's account to the selected beneficiary's account"
    )
    @PostMapping("/{beneficiaryId}/transfer")
    public ResponseEntity<TransactionResponseDTO> transferToBeneficiary(@PathVariable Long beneficiaryId,
                                                                        @RequestHeader("Authorization") String authorizationHeader,
                                                                        @Valid @RequestBody BeneficiaryTransferRequestDTO request)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer using the extracted email
        Customer customer = beneficiaryService.getCustomerFromEmail(email);

        // Transfer money to the selected beneficiary
        TransactionResponseDTO response = beneficiaryService.transferToBeneficiary(beneficiaryId, customer.getId(), request);

        // Return the transaction response with HTTP CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getBeneficiaryTransactions
    //
    //  Description         : Retrieves transactions related to the
    //                        selected beneficiary.
    //
    //                        It extracts the customer's email from
    //                        the JWT token and identifies the customer.
    //
    //                        It then gets the transactions related to
    //                        the beneficiary for that customer.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get beneficiary transactions",
            description = "Returns transactions related to a selected beneficiary for the authenticated customer"
    )
    @GetMapping("/{beneficiaryId}/transactions")
    public List<TransactionResponseDTO> getBeneficiaryTransactions(@PathVariable Long beneficiaryId,
                                                                   @RequestHeader("Authorization") String authorizationHeader)
    {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Extract the customer's email from the JWT token
        String email = jwtService.extractEmail(token);

        // Find the customer using the extracted email
        Customer customer = beneficiaryService.getCustomerFromEmail(email);

        // Get transactions related to the selected beneficiary
        return transactionService.getTransactionsByBeneficiary(beneficiaryId, customer.getId());
    }
}