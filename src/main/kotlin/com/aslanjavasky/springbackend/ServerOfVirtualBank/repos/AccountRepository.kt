package com.aslanjavasky.springbackend.ServerOfVirtualBank.repos

import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Account?
}
