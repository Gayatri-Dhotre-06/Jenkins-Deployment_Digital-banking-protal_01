package com.banking.account.dto;

import com.banking.account.entity.AccountStatus;
import com.banking.account.entity.AccountType;

import java.math.BigDecimal;

public class AccountResponseDTO
{
    // Stores the unique ID of the account
    private Long id;

    // Stores the account number
    private String accountNumber;

    // Stores the type of bank account
    private AccountType accountType;

    // Stores the current account balance
    private BigDecimal balance;

    // Stores the current status of the account
    private AccountStatus status;

    // Stores the ID of the customer who owns the account
    private Long customerId;

    public AccountResponseDTO(
                                  Long id,
                                  String accountNumber,
                                  AccountType accountType,
                                  BigDecimal balance,
                                  AccountStatus status,
                                  Long aLong
                             )
    {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.customerId = aLong;
    }

    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType()
    {
        return accountType;
    }
    public void setAccountType(AccountType accountType)
    {
        this.accountType = accountType;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }
    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public AccountStatus getStatus()
    {
        return status;
    }
    public void setStatus(AccountStatus status)
    {
        this.status = status;
    }

    public Long getCustomerId()
    {
        return customerId;
    }
    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }
}
