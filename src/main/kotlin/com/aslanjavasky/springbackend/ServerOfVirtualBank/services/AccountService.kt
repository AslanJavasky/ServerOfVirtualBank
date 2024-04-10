package com.aslanjavasky.springbackend.ServerOfVirtualBank.services


import com.aslanjavasky.springbackend.ServerOfVirtualBank.configs.CurrencyConversionUtils
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Account
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Transaction
import com.aslanjavasky.springbackend.ServerOfVirtualBank.repos.AccountRepository
import com.aslanjavasky.springbackend.ServerOfVirtualBank.repos.TransactionRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
        private val accountRepository: AccountRepository,
        private val transactionRepository: TransactionRepository,
        private val currencyConversionUtils: CurrencyConversionUtils) {

    fun createAccount(account: Account): Account {
        return accountRepository.save(account)
    }

    fun getAccountById(accountId: Long): Account {
        return accountRepository
                .findById(accountId)
                .orElseThrow { EntityNotFoundException("Account not found with id: $accountId") }
    }

    fun getAccountBalance(accountId: Long): Map<String, Int> {
        val account = getAccountById(accountId)
        val balance = mutableMapOf<String, Int>()
        balance["galleons"] = account.balanceGalleons
        balance["sickles"] = account.balanceSickles
        balance["knuts"] = account.balanceKnuts
        return balance
    }

    fun findAccountByEmail(email: String): Account? {
        return accountRepository.findByEmail(email)
    }

    fun getAllAccounts(): List<Account> {
        return accountRepository.findAll()
    }

    fun deleteAccount(accountId: Long) {
        val account = getAccountById(accountId)
        accountRepository.delete(account)
    }

    fun deleteAccountByEmail(email: String) {
        val account = accountRepository.findByEmail(email)
        account?.let {
            accountRepository.delete(account)
        }
    }

    fun updateAccount(accountId: Long, account: Account): Account {
        val existingAccount = getAccountById(accountId)
        existingAccount.apply {
            firstName = account.firstName
            lastName = account.lastName
            email = account.email
            hogwartsHouse = account.hogwartsHouse
        }
        return accountRepository.save(existingAccount)
    }

    fun checkAccountExistence(userId: Long): Boolean {
        return accountRepository.existsById(userId)
    }

    fun getAccountTransactions(accountId: Long): List<Transaction> {
        val account = getAccountById(accountId)
        val transactionIds = account.transactionIds
        return transactionIds.mapNotNull { transactionRepository.findById(it).orElse(null) }
    }

    @Transactional
    fun transferAndupdateAccountBalance(sourceAccountId: Long, targetAccountId: Long,
                                        amountGalleons: Int?, amountSickles: Int?, amountKnuts: Int?) {
        // Получаем аккаунты по их идентификаторам
        val sourceAccount = getAccountById(sourceAccountId)
        val targetAccount = getAccountById(targetAccountId)


        // Обновляем балансы аккаунтов
        if (amountGalleons != null) {
            sourceAccount.balanceGalleons -= amountGalleons
            targetAccount.balanceGalleons += amountGalleons
        }
        if (amountSickles != null) {
            sourceAccount.balanceSickles -= amountSickles
            targetAccount.balanceSickles += amountSickles
        }
        if (amountKnuts != null) {
            sourceAccount.balanceKnuts -= amountKnuts
            targetAccount.balanceKnuts += amountKnuts
        }

        // Сохраняем изменения в базе данных
        accountRepository.save(sourceAccount)
        accountRepository.save(targetAccount)
    }


}
