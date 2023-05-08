package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", false);
        model.addAttribute("user", new ApplicationUser());
        return "login.html";
    }

    @RequestMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "login.html";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "index.html";
    }
}
