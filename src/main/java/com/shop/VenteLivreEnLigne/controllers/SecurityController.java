package com.shop.VenteLivreEnLigne.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {
    @RequestMapping("/not_authorized")
    public String notAuthorized() {
        return "not_authorized.html";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", false);
        return "login.html";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "login.html";
    }
}
