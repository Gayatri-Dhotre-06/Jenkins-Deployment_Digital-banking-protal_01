//////////////////////////////////////////////////////////////////
//
//  Class Name          : TransactionService
//
//  Description         : Provides business logic for transaction
//                        management in the Digital Banking Portal.
//
//                        It handles money transfers, transaction
//                        history, transaction filtering, customer
//                        transaction access, beneficiary transactions,
//                        account ownership, and transaction limits.
//
//                        It also communicates with account,
//                        customer, beneficiary, and transaction
//                        repositories to perform required database
//                        operations.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

package com.banking.transaction.service;

import com.banking.account.entity.Account;
import com.banking.account.entity.AccountStatus;
import com.banking.account.repository.AccountRepository;

import com.banking.common.exception.AccountNotFoundException;
import com.banking.common.exception.TransactionNotFoundException;
import com.banking.common.exception.BeneficiaryNotFoundException;

import com.banking.beneficiary.entity.Beneficiary;
import com.banking.beneficiary.repository.BeneficiaryRepository;

import com.banking.transaction.dto.TransferRequestDTO;
import com.banking.transaction.dto.TransactionResponseDTO;
import com.banking.transaction.entity.Transaction;
import com.banking.transaction.entity.TransactionStatus;
import com.banking.transaction.entity.TransactionType;
import com.banking.transaction.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.banking.customer.repository.CustomerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
public class TransactionService
{
    // Repository used to perform account-related database operations
    private final AccountRepository accountRepository;

    // Repository used to perform transaction-related database operations
    private final TransactionRepository transactionRepository;

    // Repository used to perform customer-related database operations
    private final CustomerRepository customerRepository;

    // Repository used to perform beneficiary-related database operations
    private final BeneficiaryRepository beneficiaryRepository;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : TransactionService
    //
    //  Description         : Initializes the TransactionService with
    //                        the required repository dependencies.
    //
    //                        Constructor-based dependency injection
    //                        is used to provide the repositories.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository,
                              CustomerRepository customerRepository,
                              BeneficiaryRepository beneficiaryRepository)
    {
        // Store the account repository
        this.accountRepository = accountRepository;

        // Store the transaction repository
        this.transactionRepository = transactionRepository;

        // Store the customer repository
        this.customerRepository = customerRepository;

        // Store the beneficiary repository
        this.beneficiaryRepository = beneficiaryRepository;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : transferMoney
    //
    //  Description         : Transfers money from the sender account
    //                        to the receiver account.
    //
    //                        It validates the sender and receiver
    //                        account numbers and checks account
    //                        ownership and account status.
    //
    //                        It validates the transfer amount and
    //                        checks the daily and maximum transfer
    //                        limits.
    //
    //                        It checks the sender's balance before
    //                        transferring the money.
    //
    //                        It updates both account balances and
    //                        creates debit and credit transactions.
    //
    //                        The transaction is processed safely
    //                        using the TransactionService.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Transactional
    public TransactionResponseDTO transferMoney(TransferRequestDTO request, Long customerId)
    {
        // Get sender and receiver account numbers from the request
        String senderAccountNumber = request.getSenderAccountNumber();
        String receiverAccountNumber = request.getReceiverAccountNumber();

        // Validate that sender account number is provided
        if(request.getSenderAccountNumber () == null || request.getSenderAccountNumber().isBlank())
        {
            throw new IllegalArgumentException(("Sender Account number is required"));
        }

        // Validate that receiver account number is provided
        if(request.getReceiverAccountNumber () == null || request.getReceiverAccountNumber().isBlank())
        {
            throw new IllegalArgumentException(("Receiver Account number is required"));
        }

        // Validate sender account number format
        if(!senderAccountNumber.matches("\\d{10}"))
        {
            throw new IllegalArgumentException("Sender account number must be exactly 10 digits");
        }

        // Validate receiver account number format
        if(!receiverAccountNumber.matches("\\d{10}"))
        {
            throw new IllegalArgumentException("Receiver account number must be exactly 10 digits");
        }

        // Find the sender account using the account number
        Account senderAccount = accountRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        // Check whether the sender account belongs to the customer
        if(!senderAccount.getCustomer().getId().equals(customerId))
        {
            throw new IllegalArgumentException("You are not authorized to transfer from this account");
        }

        // Find the receiver account using the account number
        Account receiverAccount = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        // Prevent transferring money to the same account
        if(senderAccount.getAccountNumber().equals(receiverAccount.getAccountNumber()))
        {
            throw new IllegalArgumentException("Sender and receiver accounts cannot be the same");
        }

        // Check whether the sender account is active
        if(senderAccount.getStatus() != AccountStatus.ACTIVE)
        {
            throw new IllegalArgumentException("Sender account is inactive");
        }

        // Check whether the receiver account is active
        if(receiverAccount.getStatus() != AccountStatus.ACTIVE)
        {
            throw new IllegalArgumentException("Receiver account is inactive");
        }

        // Get the transfer amount from the request
        BigDecimal amount = request.getAmount();

        // Validate that the transfer amount is greater than zero
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // Set the start and end time of the current day
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        // Calculate the total amount transferred by the sender today
        BigDecimal dailyTransferredAmount = transactionRepository.sumAmountBySenderAccountAndDateRangeAndType(
                senderAccount.getAccountNumber(),
                startOfDay,
                endOfDay,
                TransactionType.DEBIT);

        // Set daily transferred amount to zero when no previous transfer exists
        if(dailyTransferredAmount == null)
        {
            dailyTransferredAmount = BigDecimal.ZERO;
        }

        // Define the maximum amount that can be transferred in one day
        BigDecimal maximumDailyTransferAmount = new BigDecimal("200000.00");

        // Check whether the daily transfer limit is exceeded
        if (dailyTransferredAmount.add(amount).compareTo(maximumDailyTransferAmount) > 0)
        {
            throw new IllegalArgumentException("Daily transfer limit of ₹200000.00 exceeded");
        }

        // Define the maximum amount allowed for a single transfer
        BigDecimal maximumTransferAmount = new BigDecimal("100000.00");

        // Check whether the single transfer limit is exceeded
        if(amount.compareTo(maximumTransferAmount) > 0)
        {
            throw new IllegalArgumentException("Transfer amount cannot exceed ₹100000.00");
        }

        // Check whether the sender account has a valid balance
        if(senderAccount.getBalance() == null)
        {
            throw new IllegalArgumentException("Sender account balance is not available");
        }

        // Check whether the sender has sufficient balance
        if(senderAccount.getBalance().compareTo(amount) < 0)
        {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Deduct the transfer amount from the sender account
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

        // Set the receiver's balance to zero if it is currently null
        if(receiverAccount.getBalance() == null)
        {
            receiverAccount.setBalance(BigDecimal.ZERO);
        }

        // Add the transfer amount to the receiver's balance
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        // Save the updated sender and receiver account balances
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Get the current transaction date and time
        LocalDateTime transactionDate = LocalDateTime.now();

        // Create a debit transaction for the sender account
        Transaction transaction = new Transaction();

        // Generate a unique reference number for the transaction
        transaction.setReferenceNumber("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Set sender and receiver accounts for the transaction
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);

        // Set transfer amount and transaction details
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionDate(transactionDate);

        // Save the debit transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Create a credit transaction for the receiver account
        Transaction creditTransaction = new Transaction();

        // Use the same reference number for the credit transaction
        creditTransaction.setReferenceNumber(transaction.getReferenceNumber());

        // Set receiver account for the credit transaction
        creditTransaction.setReceiverAccount(receiverAccount);

        // Set credit transaction amount and details
        creditTransaction.setAmount(amount);
        creditTransaction.setTransactionType(TransactionType.CREDIT);
        creditTransaction.setStatus(TransactionStatus.SUCCESS);
        creditTransaction.setTransactionDate(transactionDate);

        // Save the credit transaction
        transactionRepository.save(creditTransaction);

        // Return the transaction details as a response
        return new TransactionResponseDTO(savedTransaction.getId(),
                savedTransaction.getReferenceNumber(),
                senderAccount.getAccountNumber(),
                receiverAccount.getAccountNumber(),
                savedTransaction.getAmount(),
                savedTransaction.getTransactionType(),
                savedTransaction.getStatus(),
                savedTransaction.getTransactionDate());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionHistory
    //
    //  Description         : Retrieves the transaction history for
    //                        the specified account.
    //
    //                        It finds all transactions where the
    //                        account is either the sender or receiver.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getTransactionHistory(Long accountId)
    {
        // Find all transactions related to the specified account
        List<Transaction> transactions = transactionRepository.findAccountTransactionHistory(accountId);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionHistoryByAccountNumber
    //
    //  Description         : Retrieves the transaction history for
    //                        the specified account number.
    //
    //                        It finds transactions where the account
    //                        number is either the sender or receiver.
    //
    //                        The transactions are retrieved in pages
    //                        using the provided Pageable object.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The paginated transaction details are
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public Page<TransactionResponseDTO> getTransactionHistoryByAccountNumber(
            String accountNumber,
            Pageable pageable)
    {
        // Find transactions related to the specified account number
        Page<Transaction> transactions = transactionRepository.findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(
                accountNumber,
                accountNumber,
                pageable);

        // Convert each transaction into a response DTO
        return transactions.map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate()));
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionsByType
    //
    //  Description         : Retrieves transactions based on the
    //                        specified transaction type.
    //
    //                        It finds all transactions that match
    //                        the given transaction type.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getTransactionsByType(TransactionType transactionType)
    {
        // Find all transactions with the specified transaction type
        List<Transaction> transactions = transactionRepository.findByTransactionType(transactionType);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionsByStatus
    //
    //  Description         : Retrieves transactions based on the
    //                        specified transaction status.
    //
    //                        It finds all transactions that match
    //                        the given status.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getTransactionsByStatus(TransactionStatus status)
    {
        // Find all transactions with the specified status
        List<Transaction> transactions = transactionRepository.findByStatus(status);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionsByAccountNumberAndType
    //
    //  Description         : Retrieves transactions for the specified
    //                        account number and transaction type.
    //
    //                        It finds transactions where the account
    //                        number is either the sender or receiver
    //                        and matches the given transaction type.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getTransactionsByAccountNumberAndType(String accountNumber,
                                                                              TransactionType transactionType)
    {
        // Find transactions related to the account number and type
        List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionType(accountNumber,
                accountNumber,
                transactionType);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionsByDateRange
    //
    //  Description         : Retrieves transactions within the
    //                        specified start and end date.
    //
    //                        It finds all transactions whose
    //                        transaction date falls between the
    //                        given start and end date.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate)
    {
        // Find all transactions within the specified date range
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerIdByEmail
    //
    //  Description         : Retrieves the customer ID using the
    //                        customer's email address.
    //
    //                        It searches for the customer and returns
    //                        the customer ID when found.
    //
    //                        An exception is thrown when the customer
    //                        does not exist.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public Long getCustomerIdByEmail(String email)
    {
        // Find the customer using the email address
        // Return the customer's ID
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer Not Found"))
                .getId();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionById
    //
    //  Description         : Retrieves a transaction using its
    //                        transaction ID.
    //
    //                        It checks whether the transaction exists
    //                        in the database.
    //
    //                        It verifies that the customer is either
    //                        the sender or receiver of the transaction.
    //
    //                        If the customer is not related to the
    //                        transaction, access is denied.
    //
    //                        The transaction details are converted
    //                        into a TransactionResponseDTO object.
    //
    //                        The transaction response is returned
    //                        to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public TransactionResponseDTO getTransactionById(Long transactionId, Long customerId)
    {
        // Find the transaction using the provided transaction ID
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        // Check whether the customer is the sender of the transaction
        boolean isSender = transaction.getSenderAccount() != null
                && transaction.getSenderAccount().getCustomer().getId().equals(customerId);

        // Check whether the customer is the receiver of the transaction
        boolean isReceiver = transaction.getReceiverAccount() != null
                && transaction.getReceiverAccount().getCustomer().getId().equals(customerId);

        // Deny access if the customer is neither sender nor receiver
        if(!isSender && !isReceiver)
        {
            throw new IllegalArgumentException("You are not authorized to view this transaction");
        }

        // Convert the transaction into a response DTO
        return new TransactionResponseDTO(transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate());
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerAccountIds
    //
    //  Description         : Retrieves all account IDs belonging to
    //                        the specified customer.
    //
    //                        The account records are converted into
    //                        a list containing only their IDs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<Long> getCustomerAccountIds(Long customerId)
    {
        // Find all accounts belonging to the customer
        // Extract and return only the account IDs
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(Account::getId)
                .toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerTransactionHistory
    //
    //  Description         : Retrieves the transaction history of a
    //                        customer with pagination.
    //
    //                        It gets all account IDs belonging to the
    //                        customer.
    //
    //                        It finds transactions where the customer's
    //                        accounts are used as sender or receiver.
    //
    //                        The transactions are converted into response
    //                        DTOs and returned as a paginated result.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public Page<TransactionResponseDTO> getCustomerTransactionHistory(Long customerId, Pageable pageable)
    {
        // Get all account IDs belonging to the customer
        List<Long> accountIds = getCustomerAccountIds(customerId);

        // Return an empty page if the customer has no accounts
        if(accountIds.isEmpty())
        {
            return Page.empty(pageable);
        }

        // Find all transactions related to the customer's accounts
        Page<Transaction> transactions = transactionRepository.findBySenderAccountIdInOrReceiverAccountIdIn(
                accountIds,
                accountIds,
                pageable);

        // Convert each transaction into a response DTO
        return transactions.map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate()));
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : validateAccountOwnership
    //
    //  Description         : Verifies that the specified account belongs
    //                        to the given customer.
    //
    //                        An exception is thrown when the customer
    //                        does not have access to the account.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public void validateAccountOwnership(String accountNumber, Long customerId)
    {
        // Find the account using account number and customer ID
        // Throw an exception if the account does not belong to the customer
        accountRepository.findByAccountNumberAndCustomerId(accountNumber, customerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You are not authorized to access this account"));
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerTransactionsByType
    //
    //  Description         : Retrieves the customer's transactions
    //                        based on the specified transaction type.
    //
    //                        It first gets all account IDs belonging
    //                        to the customer.
    //
    //                        If the customer has no accounts, an empty
    //                        list is returned.
    //
    //                        It finds transactions of the given type
    //                        related to the customer's accounts.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getCustomerTransactionsByType(Long customerId,
                                                                      TransactionType transactionType)
    {
        // Get all account IDs belonging to the customer
        List<Long> accountIds = getCustomerAccountIds(customerId);

        // Return an empty list if the customer has no accounts
        if(accountIds.isEmpty())
        {
            return List.of();
        }

        // Find transactions of the specified type for customer's accounts
        List<Transaction> transactions = transactionRepository.findCustomerTransactionsByType(
                transactionType,
                accountIds);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerTransactionsByStatus
    //
    //  Description         : Retrieves the customer's transactions
    //                        based on the specified transaction status.
    //
    //                        It first gets all account IDs belonging
    //                        to the customer.
    //
    //                        If the customer has no accounts, an empty
    //                        list is returned.
    //
    //                        It finds transactions with the given
    //                        status where the customer's account is
    //                        either the sender or receiver.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getCustomerTransactionsByStatus(Long customerId,
                                                                        TransactionStatus status)
    {
        // Get all account IDs belonging to the customer
        List<Long> accountIds = getCustomerAccountIds(customerId);

        // Return an empty list if the customer has no accounts
        if(accountIds.isEmpty())
        {
            return List.of();
        }

        // Find transactions with the given status for customer's accounts
        List<Transaction> transactions = transactionRepository.findCustomerTransactionsByStatus(
                status,
                accountIds);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getCustomerTransactionByDateAndRange
    //
    //  Description         : Retrieves the customer's transactions
    //                        within the specified date range.
    //
    //                        It first gets all account IDs belonging
    //                        to the customer.
    //
    //                        If the customer has no accounts, an empty
    //                        list is returned.
    //
    //                        It finds transactions within the given
    //                        date range where the customer's account
    //                        is either the sender or receiver.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details is
    //                        returned to the customer.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getCustomerTransactionByDateAndRange(Long customerId,
                                                                             LocalDateTime startDate,
                                                                             LocalDateTime endDate)
    {
        // Get all account IDs belonging to the customer
        List<Long> accountIds = getCustomerAccountIds(customerId);

        // Return an empty list if the customer has no accounts
        if(accountIds.isEmpty())
        {
            return List.of();
        }

        // Find transactions within the date range for customer's accounts
        List<Transaction> transactions = transactionRepository.findCustomerTransactionsByDateRange(
                startDate,
                endDate,
                accountIds);

        // Convert each transaction into a response DTO
        return transactions.stream().map(transaction -> new TransactionResponseDTO(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getTransactionDate())).toList();
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getTransactionsByBeneficiary
    //
    //  Description         : Retrieves transactions related to the
    //                        specified beneficiary.
    //
    //                        It first verifies that the beneficiary
    //                        belongs to the customer.
    //
    //                        It gets all account IDs belonging to the
    //                        customer and returns an empty list if the
    //                        customer has no accounts.
    //
    //                        It finds transactions related to the
    //                        customer's accounts and filters them to
    //                        include only transactions involving the
    //                        selected beneficiary.
    //
    //                        Each transaction is converted into a
    //                        TransactionResponseDTO object.
    //
    //                        The list of transaction details related
    //                        to the beneficiary is returned.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public List<TransactionResponseDTO> getTransactionsByBeneficiary(Long beneficiaryId, Long customerId)
    {
        // Find and validate the beneficiary for the customer
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndCustomerId(beneficiaryId, customerId)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));

        // Get all account IDs belonging to the customer
        List<Long> accountIds = getCustomerAccountIds(customerId);

        // Return an empty list if the customer has no accounts
        if(accountIds.isEmpty())
        {
            return List.of();
        }

        // Create a list to store transactions of customer's accounts
        List<Transaction> transactions =
                transactionRepository.findBySenderAccountIdInOrReceiverAccountIdIn(
                        accountIds,
                        accountIds);

        // Filter transactions related to the selected beneficiary
        return transactions.stream()
                .filter(transaction ->
                        (transaction.getReceiverAccount() != null
                                && transaction.getReceiverAccount()
                                .getAccountNumber()
                                .equals(beneficiary.getAccountNumber()))
                                ||
                                (transaction.getSenderAccount() != null
                                        && transaction.getSenderAccount()
                                        .getAccountNumber()
                                        .equals(beneficiary.getAccountNumber())))

                // Convert each transaction into a response DTO
                .map(transaction -> new TransactionResponseDTO(
                        transaction.getId(),
                        transaction.getReferenceNumber(),
                        transaction.getSenderAccount() != null
                                ? transaction.getSenderAccount().getAccountNumber()
                                : null,
                        transaction.getReceiverAccount() != null
                                ? transaction.getReceiverAccount().getAccountNumber()
                                : null,
                        transaction.getAmount(),
                        transaction.getTransactionType(),
                        transaction.getStatus(),
                        transaction.getTransactionDate()))
                .toList();
    }
}