package com.banking.beneficiary.dto;

import java.time.LocalDateTime;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : BeneficiaryResponseDTO
//
//  Description         : Stores the beneficiary details returned
//                        to the customer after performing beneficiary
//                        related operations.
//
//                        It contains the beneficiary ID, name,
//                        account number, bank name, and creation time.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class BeneficiaryResponseDTO
{
    // Stores the unique ID of the beneficiary
    private Long id;

    // Stores the name of the beneficiary
    private String name;

    // Stores the account number of the beneficiary
    private String accountNumber;

    // Stores the bank name of the beneficiary
    private String bankName;

    // Stores the date and time when the beneficiary was created
    private LocalDateTime createdAt;

    public BeneficiaryResponseDTO(Long id, String name, String accountNumber, String bankName, LocalDateTime createdAt)
    {
        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
}
