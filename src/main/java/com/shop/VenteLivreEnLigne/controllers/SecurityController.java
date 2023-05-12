package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.AppUser;
import com.shop.VenteLivreEnLigne.models.Basket;
import com.shop.VenteLivreEnLigne.repositories.AppUserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final AppUserRepository appUserRepository;

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

    @RequestMapping("/logout")
    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser u = appUserRepository.findByUsername(authentication.getName());
        Basket.storages.remove(u.getId());
        return "redirect:/login";
    }

    @RequestMapping("/change_password")
    public String changePassword(Model model) {
        model.addAttribute("user", new AppUser());
        return "change-password.html";
    }
}
