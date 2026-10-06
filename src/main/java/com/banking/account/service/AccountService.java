//////////////////////////////////////////////////////////////////
//
//  Class Name          : AccountService
//
//  Description         : Provides business logic for managing
//                        customer bank accounts.
//
//                        It handles account creation, account
//                        retrieval, account status updates,
//                        customer account management, and account
//                        number generation.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.account.service;

import com.banking.account.dto.AccountRequestDTO;
import com.banking.account.dto.AccountResponseDTO;
import com.banking.account.entity.Account;
import com.banking.account.entity.AccountStatus;
import com.banking.account.repository.AccountRepository;
import com.banking.common.exception.AccountNotFoundException;
import com.banking.customer.entity.Customer;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;
import java.util.List;

@Service
public class AccountService
{
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : AccountService
    //
    //  Description         : Initializes the AccountService with the
    //                        required repository dependencies.
    //
    //                        It assigns the account and customer
    //                        repositories to the service fields.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository)
    {
        // Initialize the account repository
        this.accountRepository = accountRepository;

        // Initialize the customer repository
        this.customerRepository = customerRepository;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : createAccount
    //
    //  Description         : Creates a new bank account for a customer.
    //
    //                        It finds the customer using the given
    //                        customer ID.
    //
    //                        It generates a unique account number and
    //                        creates a new account with the requested
    //                        account type.
    //
    //                        The account starts with zero balance and
    //                        ACTIVE status.
    //
    //                        The new account is saved in the database
    //                        and returned as a response DTO.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AccountResponseDTO createAccount(Long customerID, AccountRequestDTO request)
    {
        // Find the customer using the given customer ID
        Customer customer = customerRepository.findById(customerID)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found with id : " + customerID));

        // Generate a unique account number
        String accountNumber = generateAccountNumber();

        // Create a new account object
        Account account = new Account();

        // Set the generated account number
        account.setAccountNumber(accountNumber);

        // Set the account type from the request
        account.setAccountType(request.getAccountType());

        // Set the initial account balance to zero
        account.setBalance(BigDecimal.ZERO);

        // Set the initial account status as ACTIVE
        account.setStatus(AccountStatus.ACTIVE);

        // Associate the account with the customer
        account.setCustomer(customer);

        // Save the new account in the database
        Account savedAccount = accountRepository.save(account);

        // Convert the saved account into a response DTO
        return new AccountResponseDTO(savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getStatus(),
                savedAccount.getCustomer().getId());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : generateAccountNumber
    //
    //  Description         : Generates a unique account number.
    //
    //                        It creates a random account number within
    //                        the required range.
    //
    //                        It checks whether the generated account
    //                        number already exists in the database.
    //
    //                        If the number already exists, a new number
    //                        is generated until a unique number is found.
    //
    //                        The unique account number is returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    private String generateAccountNumber()
    {
        // Create a Random object to generate a random number
        Random random = new Random();

        // Variable to store the generated account number
        String accountNumber;

        do
        {
            // Generate a random 10-digit account number
            accountNumber = String.valueOf( 1000000000L + random.nextLong(900000000L));

            // Continue generating until a unique account number is found
        }while(accountRepository.existsByAccountNumber(accountNumber));

        // Return the unique account number
        return accountNumber;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getAllAccounts
    //
    //  Description         : Retrieves all accounts from the system.
    //
    //                        It fetches all account records from the
    //                        account repository.
    //
    //                        Each account is converted into an account
    //                        response DTO.
    //
    //                        The list of account details is returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<AccountResponseDTO> getAllAccounts()
    {
        // Get all accounts from the account repository
        return accountRepository.findAll().stream().map(account ->
                new AccountResponseDTO(account.getId(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getStatus(),
                        account.getCustomer().getId())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : updateAccountStatus
    //
    //  Description         : Updates the status of an existing account.
    //
    //                        It finds the account using the given
    //                        account ID.
    //
    //                        It updates the account status with the
    //                        provided status value.
    //
    //                        The updated account is saved in the
    //                        database and returned as a response DTO.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AccountResponseDTO updateAccountStatus(Long id, AccountStatus status)
    {
        // Find the account using the given account ID
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id : " + id));

        // Update the account status
        account.setStatus(status);

        // Save the updated account in the database
        Account updateAccount = accountRepository.save(account);

        // Convert the updated account into a response DTO
        return new AccountResponseDTO(updateAccount.getId(),
                updateAccount.getAccountNumber(),
                updateAccount.getAccountType(),
                updateAccount.getBalance(),
                updateAccount.getStatus(),
                updateAccount.getCustomer().getId());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getAccountBalance
    //
    //  Description         : Retrieves the balance and details of a
    //                        specific customer account.
    //
    //                        It searches for the account using the
    //                        account number and customer ID.
    //
    //                        If the account is not found or does not
    //                        belong to the customer, an exception is
    //                        thrown.
    //
    //                        The account details are converted into a
    //                        response DTO and returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AccountResponseDTO getAccountBalance(String accountNumber, Long customerId)
    {
        // Find the account using account number and customer ID
        Account account = accountRepository.findByAccountNumberAndCustomerId(accountNumber, customerId)
                .orElseThrow(()-> new AccountNotFoundException("Account not found or you are not authorized to access this account"));

        // Convert account details into a response DTO
        return new AccountResponseDTO(account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCustomer().getId());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerAccounts
    //
    //  Description         : Retrieves all accounts belonging to a
    //                        customer.
    //
    //                        It finds all accounts associated with the
    //                        given customer ID.
    //
    //                        Each account is converted into an account
    //                        response DTO.
    //
    //                        The list of account details is returned
    //                        to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<AccountResponseDTO> getCustomerAccounts(Long customerId)
    {
        // Find all accounts belonging to the customer
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        // Convert each account into a response DTO
        return accounts.stream().map(account -> new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCustomer().getId())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerIdByEmail
    //
    //  Description         : Retrieves the customer ID using the
    //                        customer's email address.
    //
    //                        It searches for the customer in the
    //                        database using the given email.
    //
    //                        If the customer is not found, an
    //                        exception is thrown.
    //
    //                        The customer ID is returned after the
    //                        customer is found.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public Long getCustomerIdByEmail(String email)
    {
        // Find the customer using the provided email
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Customer not found"));

        // Return the customer's ID
        return customer.getId();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerAccount
    //
    //  Description         : Retrieves a specific account of a
    //                        customer.
    //
    //                        It finds the account using the given
    //                        account ID.
    //
    //                        It verifies that the account belongs to
    //                        the logged-in customer.
    //
    //                        If the customer is not authorized to
    //                        access the account, an exception is
    //                        thrown.
    //
    //                        The account details are converted into a
    //                        response DTO and returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AccountResponseDTO getCustomerAccount(Long customerId, Long accountId)
    {
        // Find the account using the given account ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));

        // Check whether the account belongs to the customer
        if(!account.getCustomer().getId().equals(customerId))
        {
            // Reject access if the account does not belong to the customer
            throw new IllegalArgumentException("You are not authorized to access this account");
        }

        // Convert account details into a response DTO
        return new AccountResponseDTO(account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCustomer().getId());
    }
}