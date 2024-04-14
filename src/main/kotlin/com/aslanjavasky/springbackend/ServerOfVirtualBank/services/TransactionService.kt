package com.aslanjavasky.springbackend.ServerOfVirtualBank.services


import com.aslanjavasky.springbackend.ServerOfVirtualBank.configs.CurrencyConversionUtils
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Account
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Transaction
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.TransactionType
import com.aslanjavasky.springbackend.ServerOfVirtualBank.exceptions.InsufficientFundsException
import com.aslanjavasky.springbackend.ServerOfVirtualBank.repos.TransactionRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountService: AccountService,
    private val currencyConversionUtils: CurrencyConversionUtils
) {

    fun transferMoney(
        sourceAccount: Account, targetAccount: Account,
        amountGalleons: Int, amountSickles: Int, amountKnuts: Int, description: String
    ): Transaction {
        // Проверяем достаточно ли средств на счете для выполнения транзакции
        if (!areSufficientFunds(sourceAccount, amountGalleons, amountSickles, amountKnuts)) {
            throw InsufficientFundsException("Insufficient funds for the transaction")
        }

        // Переводим средства  и обновляем балансы
        accountService.transferAndupdateAccountBalance(
            sourceAccount.id!!, targetAccount.id!!,
            amountGalleons, amountSickles, amountKnuts
        )

        // Создаем новую транзакцию
        val transaction = Transaction(
            sourceAccount = sourceAccount,
            targetAccount = targetAccount,
            amountGalleons = amountGalleons,
            amountSickles = amountSickles,
            amountKnuts = amountKnuts,
            description = description,
            transactionType = TransactionType.TRANSFER
        )
        // Сохраняем транзакцию
        return transactionRepository.save(transaction)
    }

    private fun areSufficientFunds(
        account: Account, amountGalleons: Int, amountSickles: Int, amountKnuts: Int
    ): Boolean {
        // Переводим сумму транзакции в кнуты для удобства сравнения
        val totalTransactionAmountInKnuts =
            currencyConversionUtils.convertGalleonsToKnuts(amountGalleons) +
                    currencyConversionUtils.convertSicklesToKnuts(amountSickles) +
                    amountKnuts

        // Считаем суммарное количество кнутов на счете
        val totalBalanceInKnuts = currencyConversionUtils.convertGalleonsToKnuts(account.balanceGalleons)

        // Проверяем достаточно ли средств на счете для выполнения транзакции
        return totalBalanceInKnuts >= totalTransactionAmountInKnuts
    }


    fun getTransactionById(transactionId: Long): Transaction {
        return transactionRepository
            .findById(transactionId)
            .orElseThrow { EntityNotFoundException("Transaction not found with id: $transactionId") }
    }

    fun getTransactionHistoryForAccount(accountId: Long): List<Transaction> {
        return transactionRepository.findAllBySourceAccountIdOrTargetAccountId(accountId, accountId)
    }

    fun getTransactionsByAccount(accountId: Long): List<Transaction> {
        // Метод для получения всех транзакций по заданному аккаунту
        return transactionRepository.findAllBySourceAccountIdOrTargetAccountId(accountId, accountId)
    }

    fun getTransactionsByType(transactionType: TransactionType): List<Transaction> {
        // Метод для получения всех транзакций заданного типа
        return transactionRepository.findAllByTransactionType(transactionType)
    }

    @Transactional
    fun convertGalleonsToSickles(accountId: Long, amountGalleons: Int) {
        val account = accountService.getAccountById(accountId)
        if (amountGalleons > account.balanceGalleons) {
            throw InsufficientFundsException("Insufficient Galleons for conversion")
        }
        val totalSickles = currencyConversionUtils.convertGalleonsToSickles(amountGalleons)

        // Создаем и сохраняем транзакцию
        val transaction = Transaction(
            sourceAccount = account,
            transactionType = TransactionType.CONVERSION,
            amountGalleons = -amountGalleons,
            amountSickles = totalSickles,
            description = "Conversion of $amountGalleons Galleons to $totalSickles Sickles"
        )
        transactionRepository.save(transaction)


        // Обновляем баланс аккаунта
        account.balanceGalleons = account.balanceGalleons - amountGalleons
        account.balanceSickles = account.balanceSickles + totalSickles
        accountService.updateAccount(accountId, account)
    }

    @Transactional
    fun convertSicklesToGalleons(accountId: Long, amountSickles: Int) {
        val account = accountService.getAccountById(accountId)
        if (amountSickles > account.balanceSickles) {
            throw InsufficientFundsException("Insufficient Sickles for conversion")
        }
        val totalGalleons = currencyConversionUtils.convertSicklesToGalleons(amountSickles)

        if (totalGalleons > 0) {
            // Создаем и сохраняем транзакцию
            val transaction = Transaction(
                sourceAccount = account,
                transactionType = TransactionType.CONVERSION,
                amountSickles = -amountSickles,
                amountGalleons = totalGalleons,
                description = "Conversion of $amountSickles Sickles to $totalGalleons Galleons"
            )
            transactionRepository.save(transaction)

            // Обновляем баланс аккаунта
            account.balanceSickles -= amountSickles
            account.balanceGalleons += totalGalleons
            accountService.updateAccount(accountId, account)
        }

    }

    @Transactional
    fun convertSicklesToKnuts(accountId: Long, amountSickles: Int) {
        val account = accountService.getAccountById(accountId)
        if (amountSickles > account.balanceSickles) {
            throw InsufficientFundsException("Insufficient Sickles for conversion")
        }
        val totalKnuts = currencyConversionUtils.convertSicklesToKnuts(amountSickles)

        // Создаем и сохраняем транзакцию
        val transaction = Transaction(
            sourceAccount = account,
            transactionType = TransactionType.CONVERSION,
            amountSickles = -amountSickles,
            amountKnuts = totalKnuts,
            description = "Conversion of $amountSickles Sickles to $totalKnuts Knuts"
        )
        transactionRepository.save(transaction)

        // Обновляем баланс аккаунта
        account.balanceSickles -= amountSickles
        account.balanceKnuts += totalKnuts
        accountService.updateAccount(accountId, account)
    }

    @Transactional
    fun convertKnutsToGalleons(accountId: Long, amountKnuts: Int) {
        val account = accountService.getAccountById(accountId)
        if (amountKnuts > account.balanceKnuts) {
            throw InsufficientFundsException("Insufficient Knuts for conversion")
        }
        val totalGalleons = currencyConversionUtils.convertKnutsToGalleons(amountKnuts)

        if (totalGalleons > 0) {
            // Создаем и сохраняем транзакцию
            val transaction = Transaction(
                sourceAccount = account,
                transactionType = TransactionType.CONVERSION,
                amountKnuts = -amountKnuts,
                amountGalleons = totalGalleons,
                description = "Conversion of $amountKnuts Knuts to $totalGalleons Galleons"
            )
            transactionRepository.save(transaction)

            // Обновляем баланс аккаунта
            account.balanceKnuts -= amountKnuts
            if (totalGalleons != null) {
                account.balanceGalleons += totalGalleons
            }
            accountService.updateAccount(accountId, account)
        }
    }

    @Transactional
    fun convertKnutsToSickles(accountId: Long, amountKnuts: Int) {
        val account = accountService.getAccountById(accountId)
        if (amountKnuts > account.balanceKnuts) {
            throw InsufficientFundsException("Insufficient Knuts for conversion")
        }
        val totalSickles = currencyConversionUtils.convertKnutsToSickles(amountKnuts)

        if (totalSickles > 0) {
            // Создаем и сохраняем транзакцию
            val transaction = Transaction(
                sourceAccount = account,
                transactionType = TransactionType.CONVERSION,
                amountKnuts = -amountKnuts,
                amountSickles = totalSickles,
                description = "Conversion of $amountKnuts Knuts to $totalSickles Sickles"
            )
            transactionRepository.save(transaction)

            // Обновляем баланс аккаунта
            account.balanceKnuts -= amountKnuts
            account.balanceSickles += totalSickles
            accountService.updateAccount(accountId, account)
        }

    }

    @Transactional
    fun convertGalleonsToKnuts(accountId: Long, amountGalleons: Int) {
        val account = accountService.getAccountById(accountId)
        if (amountGalleons > account.balanceGalleons) {
            throw InsufficientFundsException("Insufficient Galleons for conversion")
        }
        val totalKnuts = currencyConversionUtils.convertGalleonsToKnuts(amountGalleons)

        // Создаем и сохраняем транзакцию
        val transaction = Transaction(
            sourceAccount = account,
            transactionType = TransactionType.CONVERSION,
            amountGalleons = -amountGalleons,
            amountKnuts = totalKnuts,
            description = "Conversion of $amountGalleons Galleons to $totalKnuts Knuts"
        )
        transactionRepository.save(transaction)

        // Обновляем баланс аккаунта
        account.balanceGalleons -= amountGalleons
        account.balanceKnuts += totalKnuts
        accountService.updateAccount(accountId, account)
    }

    fun getAllTransactions() = transactionRepository.findAll()


}


