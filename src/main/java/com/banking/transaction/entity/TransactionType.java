package com.banking.transaction.entity;

//////////////////////////////////////////////////////////////////
//
//  Enum Name            : TransactionType
//
//  Description         : Represents the type of a banking
//                        transaction.
//
//                        DEBIT indicates that money is deducted
//                        from an account.
//
//                        CREDIT indicates that money is added
//                        to an account.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public enum TransactionType
{
    // Indicates that money is deducted from the account
    DEBIT,

    // Indicates that money is added to the account
    CREDIT
}