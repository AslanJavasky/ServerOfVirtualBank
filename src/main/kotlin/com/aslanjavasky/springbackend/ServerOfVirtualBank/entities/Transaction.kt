package com.aslanjavasky.springbackend.ServerOfVirtualBank.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Transaction(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @ManyToOne
        val sourceAccount: Account,
        @ManyToOne
        val targetAccount: Account? = null,
        @Enumerated(EnumType.STRING)
        val transactionType: TransactionType,
        val amountGalleons: Int? = 0,
        val amountSickles: Int? = 0,
        val amountKnuts: Int? = 0,
        val description: String,
        val timestamp: LocalDateTime = LocalDateTime.now()
)

