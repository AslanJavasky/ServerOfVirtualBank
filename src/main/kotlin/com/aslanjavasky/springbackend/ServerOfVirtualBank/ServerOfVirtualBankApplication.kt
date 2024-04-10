package com.aslanjavasky.springbackend.ServerOfVirtualBank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.aslanjavasky.springbackend")
class ServerOfVirtualBankApplication

fun main(args: Array<String>) {
	runApplication<ServerOfVirtualBankApplication>(*args)
}
