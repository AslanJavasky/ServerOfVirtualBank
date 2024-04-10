package com.aslanjavasky.springbackend.ServerOfVirtualBank.controllers

import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Account
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Transaction
import com.aslanjavasky.springbackend.ServerOfVirtualBank.services.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @PostMapping("/create")
    fun createAccount(@RequestBody account: Account): ResponseEntity<Account> {
        val createdAccount = accountService.createAccount(account)
        return ResponseEntity.ok(createdAccount)
    }

    @GetMapping("/{accountId}")
    fun getAccountById(@PathVariable accountId: Long): ResponseEntity<Account> {
        val account = accountService.getAccountById(accountId)
        return ResponseEntity.ok(account)
    }

    @GetMapping("/email/{email}")
    fun findAccountByEmail(@PathVariable email: String): ResponseEntity<Account> {
        val account = accountService.findAccountByEmail(email)
        return if (account != null) {
            ResponseEntity.ok(account)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/all")
    fun getAllAccounts(): ResponseEntity<List<Account>> {
        val accounts = accountService.getAllAccounts()
        return ResponseEntity.ok(accounts)
    }

    @PostMapping("/update/{accountId}")
    fun updateAccount(
            @PathVariable accountId: Long,
            @RequestBody account: Account): ResponseEntity<Account> {
        val updatedAccount = accountService.updateAccount(accountId, account)
        return ResponseEntity.ok(updatedAccount)
    }


    @GetMapping("/balance/{accountId}")
    fun getAccountBalance(@PathVariable accountId: Long): ResponseEntity<Map<String, Int>> {
        val balance = accountService.getAccountBalance(accountId)
        return ResponseEntity.ok(balance)
    }

    @PostMapping("/delete/{accountId}")
    fun deleteAccount(@PathVariable accountId: Long): ResponseEntity<Unit> {
        accountService.deleteAccount(accountId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/exists/{accountId}")
    fun checkAccountExistence(@PathVariable accountId: Long): ResponseEntity<Boolean> {
        val exists = accountService.checkAccountExistence(accountId)
        return ResponseEntity.ok(exists)
    }

    @GetMapping("/transactions/{accountId}")
    fun getAccountTransactions(@PathVariable accountId: Long): ResponseEntity<List<Transaction>> {
        val transactions = accountService.getAccountTransactions(accountId)
        return ResponseEntity.ok(transactions)
    }


}
