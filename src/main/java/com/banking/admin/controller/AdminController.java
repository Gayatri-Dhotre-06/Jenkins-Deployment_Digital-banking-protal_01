package com.banking.admin.controller;

import com.banking.account.dto.AccountResponseDTO;
import com.banking.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//////////////////////////////////////////////////////////////////
//
//  Class Name          : AdminController
//
//  Description         : Provides REST APIs for administrator
//                        dashboard and account management.
//
//                        It allows administrators to access the
//                        admin dashboard and retrieve all bank
//                        accounts.
//
//  Author              : Shubham Somanath Gadhe
//
//  Date                : 20/09/2026
//
//////////////////////////////////////////////////////////////////

@RestController
@RequestMapping("/api/admin")
@Tag(
        name = "Admin",
        description = "APIs for administrator dashboard and account management"
)
public class AdminController
{
    private final AccountService accountService;

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : AdminController
    //
    //  Description         : Initializes the AdminController with
    //                        AccountService dependency.
    //
    //                        The AccountService is used to perform
    //                        account-related operations for the
    //                        administrator.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    public AdminController(AccountService accountService)
    {
        // Initialize AccountService dependency
        this.accountService = accountService;
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : adminDashboard
    //
    //  Description         : Returns a welcome message for the
    //                        administrator dashboard.
    //
    //                        This endpoint is used to verify that
    //                        the administrator has access to the
    //                        admin dashboard.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get admin dashboard",
            description = "Returns the admin dashboard welcome message"
    )
    @GetMapping("/dashboard")
    public String adminDashboard()
    {
        // Return the admin dashboard welcome message
        return "Welcome to Admin Dashboard";
    }

    //////////////////////////////////////////////////////////////////
    //
    //  Method Name         : getAllAccounts
    //
    //  Description         : Retrieves all bank accounts available
    //                        in the system.
    //
    //                        It calls AccountService to fetch all
    //                        account details and returns them to
    //                        the administrator.
    //
    //  Author              : Shubham Somanath Gadhe
    //
    //  Date                : 20/09/2026
    //
    //////////////////////////////////////////////////////////////////

    @Operation(
            summary = "Get all accounts",
            description = "Returns all bank accounts available to the administrator"
    )
    @GetMapping("/accounts")
    public List<AccountResponseDTO> getAllAccounts()
    {
        // Retrieve all accounts from AccountService
        return accountService.getAllAccounts();
    }
}