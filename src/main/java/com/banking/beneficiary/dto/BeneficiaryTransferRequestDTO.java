package com.banking.beneficiary.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : BeneficiaryTransferRequestDTO
//
//  Description         : Represents the request data required
//                        for transferring money to a beneficiary.

//                        It contains the sender account number
//                        and transfer amount with validation
//                        constraints for required and valid
//                        input values.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class BeneficiaryTransferRequestDTO
{
    // Stores the account number from which the money is transferred
    @NotBlank(message = "Sender account number is required")
    private String senderAccountNumber;

    // Stores the amount to be transferred to the beneficiary
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;


    public BeneficiaryTransferRequestDTO()
    {

    }

    public String getSenderAccountNumber()
    {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber)
    {
        this.senderAccountNumber = senderAccountNumber;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
