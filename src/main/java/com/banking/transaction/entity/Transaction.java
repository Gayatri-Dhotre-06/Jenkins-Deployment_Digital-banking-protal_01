package com.banking.transaction.entity;

import com.banking.account.entity.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : Transaction
//
//  Description         : Represents a transaction record in the
//                        Digital Banking Portal.
//
//                        It stores transaction details such as
//                        reference number, sender account, receiver
//                        account, amount, transaction type, status,
//                        and transaction date.
//
//                        The class is mapped to the transactions
//                        table in the database using JPA.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

@Entity
@Table(name = "transactions")
public class Transaction
{
    // Stores the unique ID of the transaction
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the unique reference number of the transaction
    @Column(nullable = false, unique = true)
    private String referenceNumber;

    // Stores the account from which the money is sent
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    // Stores the account to which the money is received
    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;

    // Stores the amount involved in the transaction
    @Column(nullable = false)
    private BigDecimal amount;

    // Stores the type of transaction such as CREDIT or DEBIT
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    // Stores the current status of the transaction
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    // Stores the date and time when the transaction occurred
    @Column(nullable = false)
    private LocalDateTime transactionDate;

    public Transaction()
    {

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

    public Account getSenderAccount()
    {
        return senderAccount;
    }
    public void setSenderAccount(Account senderAccount)
    {
        this.senderAccount = senderAccount;
    }

    public Account getReceiverAccount()
    {
        return receiverAccount;
    }
    public void setReceiverAccount(Account receiverAccount)
    {
        this.receiverAccount = receiverAccount;
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
