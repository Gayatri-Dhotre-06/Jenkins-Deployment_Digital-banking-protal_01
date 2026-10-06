package com.banking.transaction.repository;

import com.banking.transaction.entity.Transaction;
import com.banking.transaction.entity.TransactionStatus;
import com.banking.transaction.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//////////////////////////////////////////////////////////////////
//
//  Interface Name       : TransactionRepository
//
//  Description          : Provides database operations for
//                         Transaction entities.
//
//                         It uses Spring Data JPA to perform
//                         transaction searches, filtering,
//                         pagination, and amount calculations.
//
//                         It also provides custom queries for
//                         retrieving customer transactions.
//
//  Author               : Shubham Somanath Gadhe
//
//  Date                 : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findAccountTransactionHistory
    //
    //  Description         : Retrieves transactions where the given
    //                        account is either the sender or receiver.
    //
    //                        It returns debit transactions for the
    //                        sender account and credit transactions
    //                        for the receiver account.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find transactions where the account is sender or receiver
    @Query("""
    SELECT t FROM Transaction t
    WHERE
        (t.senderAccount.id = :accountId AND t.transactionType = com.banking.transaction.entity.TransactionType.DEBIT)
        OR
        (t.receiverAccount.id = :accountId AND t.transactionType = com.banking.transaction.entity.TransactionType.CREDIT)
    """)
    List<Transaction> findAccountTransactionHistory(@Param("accountId") Long accountId);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findBySenderAccountIdInOrReceiverAccountIdIn
    //
    //  Description         : Retrieves transactions associated with
    //                        multiple sender or receiver account IDs.
    //
    //                        It returns all matching transactions
    //                        without pagination.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find transactions for multiple sender or receiver accounts
    List<Transaction> findBySenderAccountIdInOrReceiverAccountIdIn(List<Long> senderAccountIds,
                                                                   List<Long> receiverAccountIds);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findBySenderAccountIdInOrReceiverAccountIdIn
    //
    //  Description         : Retrieves transactions associated with
    //                        multiple sender or receiver account IDs
    //                        using pagination.
    //
    //                        Pageable is used to control the page
    //                        number, page size, and sorting.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find paginated transactions for multiple sender or receiver accounts
    Page<Transaction> findBySenderAccountIdInOrReceiverAccountIdIn(List<Long> senderAccountIds,
                                                                   List<Long> receiverAccountIds,
                                                                   Pageable pageable);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findBySenderAccountAccountNumberOrReceiverAccountAccountNumber
    //
    //  Description         : Retrieves paginated transactions using
    //                        the sender or receiver account number.
    //
    //                        It supports pagination and sorting using
    //                        the provided Pageable object.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find paginated transactions using sender or receiver account number
    Page<Transaction> findBySenderAccountAccountNumberOrReceiverAccountAccountNumber(String senderAccountNumber,
                                                                                     String receiverAccountNumber,
                                                                                     Pageable pageable);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByTransactionType
    //
    //  Description         : Retrieves all transactions having the
    //                        specified transaction type.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find all transactions of the given transaction type
    List<Transaction> findByTransactionType(TransactionType transactionType);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByStatus
    //
    //  Description         : Retrieves all transactions having the
    //                        specified transaction status.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find all transactions with the given status
    List<Transaction> findByStatus(TransactionStatus status);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByTransactionDateBetween
    //
    //  Description         : Retrieves all transactions that occurred
    //                        between the specified start and end dates.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find transactions within the given date range
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findCustomerTransactionsByStatus
    //
    //  Description         : Retrieves transactions of a customer
    //                        having the specified transaction status.
    //
    //                        The search is performed using the
    //                        customer's account IDs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find customer transactions with the given status
    @Query("""
        SELECT t FROM Transaction t
        WHERE
        (
            t.status = :status
            AND t.senderAccount.id IN :accountIds
        )
        OR
        (
            t.status = :status
            AND t.receiverAccount.id IN :accountIds
        )
        """)

    List<Transaction> findCustomerTransactionsByStatus(
            @Param("status") TransactionStatus status,
            @Param("accountIds") List<Long> accountIds);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findCustomerTransactionsByDateRange
    //
    //  Description         : Retrieves customer transactions that
    //                        occurred within the specified date range.
    //
    //                        The search checks both sender and
    //                        receiver account IDs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find customer transactions within the given date range
    @Query("""
        SELECT t FROM Transaction t
        WHERE
        (
            t.transactionDate BETWEEN :startDate AND :endDate
            AND t.senderAccount.id IN :accountIds
        )
        OR
        (
            t.transactionDate BETWEEN :startDate AND :endDate
            AND t.receiverAccount.id IN :accountIds
        )
        """)

    List<Transaction> findCustomerTransactionsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("accountIds") List<Long> accountIds);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : sumAmountBySenderAccountAndDateRangeAndType
    //
    //  Description         : Calculates the total transaction amount
    //                        for a sender account within a specified
    //                        date range and transaction type.
    //
    //                        COALESCE returns zero when no matching
    //                        transaction records are found.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Calculate the total transaction amount for an account within a date range
    @Query(""" 
            SELECT COALESCE(SUM(t.amount), 0) 
            FROM Transaction t WHERE t
            .senderAccount.accountNumber = :accountNumber 
            AND t.transactionDate BETWEEN :startDate 
            AND :endDate AND t.transactionType = :transactionType 
            """)

    BigDecimal sumAmountBySenderAccountAndDateRangeAndType(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("transactionType") TransactionType transactionType);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findCustomerTransactionsByType
    //
    //  Description         : Retrieves customer transactions having
    //                        the specified transaction type.
    //
    //                        The search is performed using the
    //                        customer's sender or receiver account IDs.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Find customer transactions by transaction type and account IDs
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.transactionType = :transactionType
        AND (
            t.senderAccount.id IN :accountIds
            OR
            t.receiverAccount.id IN :accountIds
        )
        """)

    List<Transaction> findCustomerTransactionsByType(
            @Param("transactionType") TransactionType transactionType,
            @Param("accountIds") List<Long> accountIds);

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : findByAccountNumberAndTransactionType
    //
    //  Description         : Retrieves transactions for the specified
    //                        sender or receiver account numbers and
    //                        transaction type.
    //
    //                        It checks both account numbers and
    //                        filters the results by transaction type.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 17/09/2026
    //
    //////////////////////////////////////////////////////////////////

    // Retrieves transactions for the specified sender/receiver account numbers and transaction type
    @Query("""
        SELECT t FROM Transaction t
        WHERE
        (
            t.senderAccount.accountNumber = :senderAccountNumber
            OR
            t.receiverAccount.accountNumber = :receiverAccountNumber
        )
        AND t.transactionType = :transactionType
        """)

    List<Transaction> findByAccountNumberAndTransactionType(
            @Param("senderAccountNumber") String senderAccountNumber,
            @Param("receiverAccountNumber") String receiverAccountNumber,
            @Param("transactionType") TransactionType transactionType);
}