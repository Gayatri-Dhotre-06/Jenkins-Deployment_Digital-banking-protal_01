package com.banking.transaction.dto;

import com.banking.transaction.entity.TransactionStatus;
import com.banking.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : TransactionResponseDTO
//
//  Description         : Represents the response data returned for
//                        a transaction.
//
//                        It contains transaction identification,
//                        account details, transaction amount, type,
//                        status, and transaction date.
//
//                        This DTO is used to transfer transaction
//                        information from the backend to the client.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class TransactionResponseDTO
{
    // Stores the unique ID of the transaction
    private Long id;

    // Stores the unique reference number of the transaction
    private String referenceNumber;

    // Stores the account number from which money was sent
    private String senderAccountNumber;

    // Stores the account number to which money was sent
    private String receiverAccountNumber;

    // Stores the amount involved in the transaction
    private BigDecimal amount;

    // Stores the type of transaction
    private TransactionType transactionType;

    // Stores the current status of the transaction
    private TransactionStatus status;

    // Stores the date and time when the transaction occurred
    private LocalDateTime transactionDate;

    public TransactionResponseDTO()
    {

    }

    public TransactionResponseDTO(Long id,
                                  String referenceNumber,
                                  String senderAccountNumber,
                                  String receiverAccountNumber,
                                  BigDecimal amount,
                                  TransactionType transactionType,
                                  TransactionStatus status,
                                  LocalDateTime transactionDate)
    {
        this.id = id;
        this.referenceNumber = referenceNumber;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getReferenceNumber()
    {
        return referenceNumber;
    }
    public void setReferenceNumber(String referenceNumber)
    {
        this.referenceNumber = referenceNumber;
    }

    public String getSenderAccountNumber()
    {
        return senderAccountNumber;
    }
    public void setSenderAccountNumber(String senderAccountNumber)
    {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountNumber()
    {
        return receiverAccountNumber;
    }
    public void setReceiverAccountNumber(String receiverAccountNumber)
    {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public TransactionType getTransactionType()
    {
        return transactionType;
    }
    public void setTransactionType(TransactionType transactionType)
    {
        this.transactionType = transactionType;
    }

    public TransactionStatus getStatus()
    {
        return status;
    }
    public void setStatus(TransactionStatus status)
    {
        this.status = status;
    }

    public LocalDateTime getTransactionDate()
    {
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate)
    {
        this.transactionDate = transactionDate;
    }
}
