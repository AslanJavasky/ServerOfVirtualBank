package com.aslanjavasky.springbackend.ServerOfVirtualBank.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ErrorController : org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    fun handleError(): String {
        return "error"
    }

    fun getErrorPath(): String {
        return "/error"
    }
}