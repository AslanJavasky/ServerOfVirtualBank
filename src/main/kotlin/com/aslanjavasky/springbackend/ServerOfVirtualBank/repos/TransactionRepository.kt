package com.aslanjavasky.springbackend.ServerOfVirtualBank.repos

import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Transaction
import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {

    fun findAllBySourceAccountIdOrTargetAccountId(
            sourceAccountId: Long, targetAccountId: Long
    ): List<Transaction>

    fun findAllByTransactionType(transactionType: TransactionType): List<Transaction>
}
