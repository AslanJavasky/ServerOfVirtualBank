package com.aslanjavasky.springbackend.ServerOfVirtualBank.controllers

import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Account
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Transaction
import com.aslanjavasky.springbackend.ServerOfVirtualBank.services.AccountService
import com.aslanjavasky.springbackend.ServerOfVirtualBank.services.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/accounts")
class AccountControllerHtml @Autowired constructor(private val accountService: AccountService) {

    @GetMapping
    fun getAccounts(model: Model): String {
        val accounts = accountService.getAllAccounts()
        model.addAttribute("accounts", accounts)
        return "account-list"
    }

    @GetMapping("/{accountId}")
    fun getAccountById(@PathVariable accountId: Long, model: Model): String {
        val account = accountService.getAccountById(accountId)
        model.addAttribute("account", account)
        return "account"
    }

    @GetMapping("/create")
    fun showCreateAccountForm(model: Model): String {
        model.addAttribute("account", Account(email = "", firstName = "", lastName = "", hogwartsHouse = ""))
        return "create-account"
    }

    @PostMapping("/create")
    fun createAccount(@ModelAttribute("account") account: Account): String {
        accountService.createAccount(account)
        return "redirect:/accounts/${account.id}"
    }

    @GetMapping("/delete/{accountId}")
    fun deleteAccount(@PathVariable accountId: Long): String {
        accountService.deleteAccount(accountId)
        return "redirect:/"
    }
}


