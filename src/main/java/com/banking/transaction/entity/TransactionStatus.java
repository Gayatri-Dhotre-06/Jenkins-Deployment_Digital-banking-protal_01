package com.banking.transaction.entity;

//////////////////////////////////////////////////////////////////
//
//  Enum Name            : TransactionStatus
//
//  Description         : Represents the possible status values of
//                        a banking transaction.
//
//                        SUCCESS indicates that the transaction was
//                        completed successfully.
//
//                        FAILED indicates that the transaction could
//                        not be completed.
//
//                        PENDING indicates that the transaction is
//                        still being processed.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 17/09/2026
//
//////////////////////////////////////////////////////////////////

public enum TransactionStatus
{
    // Indicates that the transaction was completed successfully
    SUCCESS,

    // Indicates that the transaction failed
    FAILED,

    // Indicates that the transaction is still being processed
    PENDING
}