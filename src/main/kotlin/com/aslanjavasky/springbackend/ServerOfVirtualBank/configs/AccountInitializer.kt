package com.aslanjavasky.springbackend.ServerOfVirtualBank.configs

import com.aslanjavasky.springbackend.ServerOfVirtualBank.entities.Account
import com.aslanjavasky.springbackend.ServerOfVirtualBank.services.AccountService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class AccountInitializer(private val accountService: AccountService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        // Удаляем аккаунты с указанными именами, если они уже существуют
        accountService.deleteAccountByEmail("aslanbolurov@list.ru")
//        accountService.deleteAccountByEmail("ron@example.com")
//        accountService.deleteAccountByEmail("hermione@example.com")

        // Создаем новые аккаунты
        val account = Account(
                firstName = "Aslan",
                lastName = "Bolurov",
                email = "aslanbolurov@list.ru",
                hogwartsHouse = "Griffindor"
        )
//        val ronWeasley = Account(
//                firstName = "Рон",
//                lastName = "Уизли",
//                email = "ron@example.com",
//                hogwartsHouse = "Гриффиндор"
//        )
//        val hermioneGranger = Account(
//                firstName = "Гермиона",
//                lastName = "Грейнджер",
//                email = "hermione@example.com",
//                hogwartsHouse = "Гриффиндор"
//        )

        // Сохраняем новые аккаунты
        accountService.createAccount(account)
//        accountService.createAccount(ronWeasley)
//        accountService.createAccount(hermioneGranger)
    }
}