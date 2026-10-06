package com.banking.account.entity;

import com.banking.customer.entity.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account
{
    // Stores the unique ID of the account
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the unique account number
    @Column(unique = true, nullable = false)
    private String accountNumber;

    // Stores the type of the bank account
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    // Stores the current balance of the account
    @Column(nullable = false)
    private BigDecimal balance;

    // Stores the version used for optimistic locking
    @Version
    private Long version;

    // Stores the current status of the account
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    // Stores the customer who owns this account
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Account()
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

    public Long getVersion()
    {
        return version;
    }
    public void setVersion(Long version)
    {
        this.version = version;
    }

    public AccountStatus getStatus()
    {
        return status;
    }
    public void setStatus(AccountStatus status)
    {
        this.status = status;
    }

    public Customer getCustomer()
    {
        return customer;
    }
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }
}
