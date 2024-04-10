package com.aslanjavasky.springbackend.ServerOfVirtualBank.controllers

import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Transaction
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.TransactionType
import com.aslanjavasky.springbackend.ServerOfVirtualBank.services.AccountService
import com.aslanjavasky.springbackend.ServerOfVirtualBank.services.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transactions")
class TransactionController(
        private val transactionService: TransactionService,
        private val accountService: AccountService) {

    @PostMapping("/transfer")
    fun makeTransaction(
            @RequestParam sourceAccountId: Long,
            @RequestParam targetAccountId: Long,
            @RequestParam amountGalleons: Int,
            @RequestParam amountSickles: Int,
            @RequestParam amountKnuts: Int,
            @RequestParam description: String
    ): ResponseEntity<Transaction> {
        val sourceAccount = accountService.getAccountById(sourceAccountId)
        val targetAccount = accountService.getAccountById(targetAccountId)
        val transaction = transactionService.transferMoney(
                sourceAccount, targetAccount,
                amountGalleons, amountSickles, amountKnuts, description
        )
        return ResponseEntity(transaction, HttpStatus.CREATED)
    }

    @GetMapping("/{transactionId}")
    fun getTransactionById(@PathVariable transactionId: Long): ResponseEntity<Transaction> {
        val transaction = transactionService.getTransactionById(transactionId)
        return ResponseEntity.ok(transaction)
    }

    @GetMapping("/history/{accountId}")
    fun getTransactionHistoryForAccount(@PathVariable accountId: Long): ResponseEntity<List<Transaction>> {
        val transactions = transactionService.getTransactionHistoryForAccount(accountId)
        return ResponseEntity.ok(transactions)
    }

    @GetMapping("/byAccount/{accountId}")
    fun getTransactionsByAccount(@PathVariable accountId: Long): ResponseEntity<List<Transaction>> {
        val transactions = transactionService.getTransactionsByAccount(accountId)
        return ResponseEntity.ok(transactions)
    }

    @GetMapping("/byType/{transactionType}")
    fun getTransactionsByType(@PathVariable transactionType: TransactionType): ResponseEntity<List<Transaction>> {
        val transactions = transactionService.getTransactionsByType(transactionType)
        return ResponseEntity.ok(transactions)
    }

    @PostMapping("/convert/galleons-to-sickles")
    fun convertGalleonsToSickles(
            @RequestParam accountId: Long,
            @RequestParam amount: Int
    ): ResponseEntity<Unit> {
        transactionService.convertGalleonsToSickles(accountId, amount)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/convert/sickles-to-galleons")
    fun convertSicklesToGalleons(
            @RequestParam accountId: Long,
            @RequestParam amount: Int
    ): ResponseEntity<Unit> {
        transactionService.convertSicklesToGalleons(accountId, amount)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/convert/sickles-to-knuts")
    fun convertSicklesToKnuts(
            @RequestParam accountId: Long,
            @RequestParam amount: Int
    ): ResponseEntity<Unit> {
        transactionService.convertSicklesToKnuts(accountId, amount)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/convert/galleons-to-knuts")
    fun convertGalleonsToKnuts(
            @RequestParam accountId: Long,
            @RequestParam amount: Int
    ): ResponseEntity<Unit> {
        transactionService.convertGalleonsToKnuts(accountId, amount)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/convert/knuts-to-galleons")
    fun convertKnutsToGalleons(
            @RequestParam accountId: Long,
            @RequestParam amount: Int
    ): ResponseEntity<Unit> {
        transactionService.convertKnutsToGalleons(accountId, amount)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/convert/knuts-to-sickles")
    fun convertKnutsToSickles(
            @RequestParam accountId: Long,
            @RequestParam amount: Int
    ): ResponseEntity<Unit> {
        transactionService.convertKnutsToSickles(accountId, amount)
        return ResponseEntity.noContent().build()
    }



}
