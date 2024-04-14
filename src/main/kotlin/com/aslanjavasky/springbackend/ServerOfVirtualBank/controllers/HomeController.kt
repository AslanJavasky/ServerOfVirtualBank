package com.aslanjavasky.springbackend.ServerOfVirtualBank.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class HomeController {

    @GetMapping("/")
    fun homePage(model: Model): String {
        return "index"
    }
}