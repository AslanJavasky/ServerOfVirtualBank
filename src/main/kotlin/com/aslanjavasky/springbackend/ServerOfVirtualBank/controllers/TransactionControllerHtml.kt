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
@RequestMapping("/transactions")
class TransactionControllerHtml @Autowired constructor(
    private val transactionService: TransactionService,
    private val accountService: AccountService
) {

    @GetMapping
    fun getAllTransactions(model: Model): String {
        val transactions = transactionService.getAllTransactions()
        model.addAttribute("transactions", transactions)
        return "transaction-list"
    }


    @GetMapping("/{transactionId}")
    fun getTransactionById(@PathVariable transactionId: Long, model: Model): String {
        val transaction = transactionService.getTransactionById(transactionId)
        model.addAttribute("transaction", transaction)
        return "transaction"
    }

    @PostMapping("/make")
    fun makeTransaction(
        @RequestParam sourceAccountId: Long,
        @RequestParam targetAccountId: Long,
        @RequestParam amountGalleons: Int,
        @RequestParam amountSickles: Int,
        @RequestParam amountKnuts: Int,
        @RequestParam description: String
    ): String {
        transactionService.transferMoney(
            accountService.getAccountById(sourceAccountId),
            accountService.getAccountById(targetAccountId),
            amountGalleons, amountSickles, amountKnuts, description
        )
        return "redirect:/accounts/${sourceAccountId}"
    }

    // Другие методы контроллера для работы с транзакциями
}