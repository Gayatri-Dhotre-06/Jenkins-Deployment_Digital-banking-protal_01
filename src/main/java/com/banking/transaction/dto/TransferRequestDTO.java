package com.banking.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : TransferRequestDTO
//
//  Description         : Represents the request data required to
//                        transfer money from one account to another.
//
//                        It contains the sender account number,
//                        receiver account number, and transfer amount.
//
//                        Validation annotations are used to ensure
//                        that the required transfer details are
//                        provided correctly.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public class TransferRequestDTO
{
    // Stores the account number from which money will be transferred
    @NotBlank(message = "Sender account number is required")
    private String senderAccountNumber;

    // Stores the account number to which money will be transferred
    @NotBlank(message = "Receiver account number is required")
    private String receiverAccountNumber;

    // Stores the amount that will be transferred
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Amount must be at least 1.00")
    private BigDecimal amount;

    public TransferRequestDTO()
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
}
