package com.shop.VenteLivreEnLigne.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {
    @RequestMapping("/not_authorized")
    public String notAuthorized() {
        return "not_authorized.html";
    }

    @RequestMapping("/login")
    public String login() {
        return "login.html";
    }
}
