package com.banking.beneficiary.entity;

import com.banking.customer.entity.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : Beneficiary
//
//  Description         : Represents a beneficiary entity in the
//                        Digital Banking Portal.
//
//                        It stores beneficiary details such as name,
//                        account number, bank name, customer reference,
//                        and creation date and time.
//
//                        The entity is mapped to the beneficiaries
//                        table in the database.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

@Entity
@Table(name = "beneficiaries")
public class Beneficiary
{
    // Stores the unique ID of the beneficiary
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the name of the beneficiary
    private String name;

    // Stores the account number of the beneficiary
    @Column(nullable = false)
    private String accountNumber;

    // Stores the bank name of the beneficiary
    private String bankName;

    // Stores the customer who owns this beneficiary
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Stores the date and time when the beneficiary was created
    private LocalDateTime createdAt;

    public Beneficiary()
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

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getBankName()
    {
        return bankName;
    }
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public Customer getCustomer()
    {
        return customer;
    }
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}



