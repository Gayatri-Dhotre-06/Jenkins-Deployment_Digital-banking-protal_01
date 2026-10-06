package com.banking.beneficiary.dto;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : BeneficiaryRequestDTO
//
//  Description         : Stores the beneficiary details provided
//                        by a customer while adding or updating
//                        a beneficiary.
//
//                        It contains the beneficiary name, account
//                        number, and bank name.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class BeneficiaryRequestDTO
{
    // Stores the name of the beneficiary
    private String name;

    // Stores the account number of the beneficiary
    private String accountNumber;

    // Stores the bank name of the beneficiary
    private String bankName;

    public BeneficiaryRequestDTO()
    {

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
}
