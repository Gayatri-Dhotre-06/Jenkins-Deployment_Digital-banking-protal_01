package com.banking.beneficiary.service;

import com.banking.account.repository.AccountRepository;
import com.banking.beneficiary.dto.BeneficiaryRequestDTO;
import com.banking.beneficiary.dto.BeneficiaryResponseDTO;
import com.banking.beneficiary.dto.BeneficiaryTransferRequestDTO;
import com.banking.transaction.dto.TransactionResponseDTO;
import com.banking.transaction.dto.TransferRequestDTO;
import com.banking.transaction.service.TransactionService;
import com.banking.beneficiary.entity.Beneficiary;
import com.banking.beneficiary.repository.BeneficiaryRepository;
import com.banking.common.exception.AccountNotFoundException;
import com.banking.common.exception.BeneficiaryNotFoundException;
import com.banking.customer.entity.Customer;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import com.banking.account.entity.Account;

import java.time.LocalDateTime;
import java.util.List;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : BeneficiaryService
//
//  Description         : Provides business logic for managing
//                        customer beneficiaries.
//
//                        It handles adding, retrieving, updating,
//                        deleting beneficiaries, and transferring
//                        money to a beneficiary.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

@Service
public class BeneficiaryService
{
    // Provides database operations for beneficiaries
    private final BeneficiaryRepository beneficiaryRepository;

    // Provides database operations for customers
    private final CustomerRepository customerRepository;

    // Provides database operations for bank accounts
    private final AccountRepository accountRepository;

    // Handles money transfer operations
    private final TransactionService transactionService;

    //////////////////////////////////////////////////////////////////
    //
    //  Constructor Name    : BeneficiaryService
    //
    //  Description         : Initializes the BeneficiaryService with
    //                        the required repository and service
    //                        dependencies.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository,
                              CustomerRepository customerRepository,
                              AccountRepository accountRepository,
                              TransactionService transactionService)
    {
        // Initialize the beneficiary repository
        this.beneficiaryRepository = beneficiaryRepository;

        // Initialize the customer repository
        this.customerRepository = customerRepository;

        // Initialize the account repository
        this.accountRepository = accountRepository;

        // Initialize the transaction service
        this.transactionService = transactionService;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : addBeneficiary
    //
    //  Description         : Adds a new beneficiary for a customer.
    //
    //                        It validates the beneficiary name,
    //                        account number, and bank name.
    //
    //                        It also checks whether the account exists,
    //                        whether the beneficiary already exists,
    //                        and prevents a customer from adding
    //                        their own account as a beneficiary.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public BeneficiaryResponseDTO addBeneficiary(Long customerId, BeneficiaryRequestDTO request)
    {
        // Validate the beneficiary name
        if(request.getName() == null || request.getName().isBlank())
        {
            throw new IllegalArgumentException("Beneficiary name is required");
        }

        // Validate that the account number contains exactly 10 digits
        if(request.getAccountNumber() == null || !request.getAccountNumber().matches("\\d{10}"))
        {
            throw new IllegalArgumentException("Beneficiary account number must be exactly 10 digits");
        }

        // Validate the bank name
        if(request.getBankName() == null || request.getBankName().isBlank())
        {
            throw new IllegalArgumentException("Bank name is required");
        }

        // Find the customer using the customer ID
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found"));

        // Check whether the beneficiary account is already added
        if(beneficiaryRepository.existsByCustomerIdAndAccountNumber(customerId, request.getAccountNumber()))
        {
            throw new IllegalArgumentException("Beneficiary with the account number already exists");
        }

        // Find the beneficiary account using the account number
        Account beneficiaryAccount = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Beneficiary Account not found"));

        // Prevent the customer from adding their own account
        if(beneficiaryAccount.getCustomer().getId().equals(customerId))
        {
            throw new IllegalArgumentException("You cannot add your own account as a beneficiary");
        }

        // Create a new beneficiary object
        Beneficiary beneficiary = new Beneficiary();

        // Set the beneficiary name
        beneficiary.setName(request.getName());

        // Set the beneficiary account number
        beneficiary.setAccountNumber(request.getAccountNumber());

        // Set the beneficiary bank name
        beneficiary.setBankName(request.getBankName());

        // Associate the beneficiary with the customer
        beneficiary.setCustomer(customer);

        // Set the beneficiary creation date and time
        beneficiary.setCreatedAt(LocalDateTime.now());

        // Save the beneficiary in the database
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);

        // Convert the saved beneficiary into a response DTO
        return mapToResponse(savedBeneficiary);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : mapToResponse
    //
    //  Description         : Converts a Beneficiary entity into a
    //                        BeneficiaryResponseDTO object.
    //
    //                        It prepares beneficiary information to
    //                        be returned to the client.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    private BeneficiaryResponseDTO mapToResponse(Beneficiary beneficiary)
    {
        // Create and return the response DTO using beneficiary details
        return new BeneficiaryResponseDTO(beneficiary.getId(),
                beneficiary.getName(),
                beneficiary.getAccountNumber(),
                beneficiary.getBankName(),
                beneficiary.getCreatedAt());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getBeneficiaryByCustomer
    //
    //  Description         : Retrieves all beneficiaries associated
    //                        with the specified customer.
    //
    //                        The beneficiary entities are converted
    //                        into response DTOs before returning them.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<BeneficiaryResponseDTO> getBeneficiaryByCustomer(Long customerId)
    {
        // Find all beneficiaries belonging to the customer
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByCustomerId(customerId);

        // Convert each beneficiary entity into a response DTO
        return beneficiaries.stream().map(this::mapToResponse).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : updateBeneficiary
    //
    //  Description         : Updates the name and bank name of an
    //                        existing beneficiary.
    //
    //                        It verifies that the beneficiary exists
    //                        and belongs to the specified customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public BeneficiaryResponseDTO updateBeneficiary(Long beneficiaryId, Long customerId, BeneficiaryRequestDTO request)
    {
        // Find the beneficiary using the beneficiary ID
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));

        // Check whether the beneficiary belongs to the customer
        if(!beneficiary.getCustomer().getId().equals(customerId))
        {
            throw new IllegalArgumentException("You are not authorized to update this beneficiary");
        }

        // Validate the beneficiary name
        if(request.getName() == null || request.getName().isBlank())
        {
            throw new IllegalArgumentException("Beneficiary name is required");
        }

        // Validate the bank name
        if(request.getBankName() == null || request.getBankName().isBlank())
        {
            throw new IllegalArgumentException("Bank name is required");
        }

        // Update the beneficiary name
        beneficiary.setName(request.getName());

        // Update the beneficiary bank name
        beneficiary.setBankName(request.getBankName());

        // Save the updated beneficiary
        Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary);

        // Convert the updated beneficiary into a response DTO
        return mapToResponse(updatedBeneficiary);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : deleteBeneficiary
    //
    //  Description         : Deletes an existing beneficiary.
    //
    //                        It first verifies that the beneficiary
    //                        exists and belongs to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public void deleteBeneficiary(Long beneficiaryId, Long customerId)
    {
        // Find the beneficiary using the beneficiary ID
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));

        // Check whether the beneficiary belongs to the customer
        if(!beneficiary.getCustomer().getId().equals(customerId))
        {
            throw new IllegalArgumentException("You are not authorized to delete this beneficiary");
        }

        // Delete the beneficiary from the database
        beneficiaryRepository.delete(beneficiary);
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerFromEmail
    //
    //  Description         : Retrieves a customer using their email
    //                        address.
    //
    //                        Throws an exception if the customer
    //                        cannot be found.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 16/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public Customer getCustomerFromEmail(String email)
    {
        // Find the customer using the email address
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getBeneficiaryById
    //
    //  Description         : Retrieves a beneficiary using its ID
    //                        and customer ID.
    //
    //                        It ensures that the beneficiary belongs
    //                        to the specified customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public Beneficiary getBeneficiaryById(Long beneficiaryId, Long customerId)
    {
        // Find the beneficiary using beneficiary ID and customer ID
        return beneficiaryRepository.findByIdAndCustomerId(beneficiaryId, customerId)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : transferToBeneficiary
    //
    //  Description         : Transfers money from the customer's
    //                        account to the beneficiary's account.
    //
    //                        It validates the beneficiary and checks
    //                        that the sender account belongs to the
    //                        customer.
    //
    //                        It creates a transfer request with the
    //                        sender account, beneficiary account,
    //                        and transfer amount.
    //
    //                        The transaction is processed through
    //                        the TransactionService.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public TransactionResponseDTO transferToBeneficiary(Long beneficiaryId, Long customerId, BeneficiaryTransferRequestDTO request)
    {
        // Find the beneficiary and verify customer ownership
        Beneficiary beneficiary = getBeneficiaryById(beneficiaryId, customerId);

        // Find the sender account using the account number
        Account senderAccount = accountRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        // Check whether the sender account belongs to the customer
        if(!senderAccount.getCustomer().getId().equals(customerId))
        {
            throw new IllegalArgumentException("You are not authorized to transfer from this account");
        }

        // Create a transfer request for the transaction service
        TransferRequestDTO transferRequest = new TransferRequestDTO();

        // Set the sender account number
        transferRequest.setSenderAccountNumber(senderAccount.getAccountNumber());

        // Set the beneficiary account number as the receiver account
        transferRequest.setReceiverAccountNumber(beneficiary.getAccountNumber());

        // Set the transfer amount
        transferRequest.setAmount(request.getAmount());

        // Process the transfer through the transaction service
        return transactionService.transferMoney(transferRequest, customerId);
    }
}