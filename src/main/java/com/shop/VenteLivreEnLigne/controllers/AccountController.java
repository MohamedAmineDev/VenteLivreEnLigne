package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.AppRole;
import com.shop.VenteLivreEnLigne.models.AppUser;
import com.shop.VenteLivreEnLigne.repositories.AppRoleRepository;
import com.shop.VenteLivreEnLigne.repositories.AppUserRepository;
import com.shop.VenteLivreEnLigne.service.AccountServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class AccountController {
    //private final AccountServiceImp accountService;
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @RequestMapping("/register_user")
    public String registerUser(Model model) {
        model.addAttribute("user", new AppUser());
        return "register-user.html";
    }

    @RequestMapping("/save_user")
    public String saveUser(Model model, @Valid AppUser user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register-user.html";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new ArrayList<>());
        AppRole role = appRoleRepository.findById("USER").orElse(null);
        if (role == null) {
            role = appRoleRepository.save(new AppRole("USER"));
        }
        user.getRoles().add(role);
        role = appRoleRepository.findById("CLIENT").orElse(null);
        if (role == null) {
            role = appRoleRepository.save(new AppRole("CLIENT"));
        }
        user.getRoles().add(role);
        appUserRepository.save(user);
        return "redirect:/login";
    }
}
