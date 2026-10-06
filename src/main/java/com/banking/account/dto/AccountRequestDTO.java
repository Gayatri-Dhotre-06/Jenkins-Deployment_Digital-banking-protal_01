package com.banking.account.dto;

import com.banking.account.entity.AccountType;
import jakarta.validation.constraints.NotNull;

public class AccountRequestDTO
{
    // Stores the type of bank account requested by the customer
    @NotNull(message = "Account type is required")
    private AccountType accountType;

    public AccountRequestDTO()
    {

    }

    public AccountType getAccountType()
    {
        return accountType;
    }
    public void setAccountType(AccountType accountType)
    {
        this.accountType = accountType;
    }
}
