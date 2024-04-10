package com.aslanjavasky.springbackend.ServerOfVirtualBank.entities

import jakarta.persistence.*

@Entity
data class Account(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        var firstName: String,
        var lastName: String,
        var email: String,
        var hogwartsHouse: String,
        @ElementCollection
        var transactionIds: List<Long> = mutableListOf(),
        var balanceGalleons: Int = 1_000,
        var balanceSickles: Int = 0,
        var balanceKnuts: Int = 0
)