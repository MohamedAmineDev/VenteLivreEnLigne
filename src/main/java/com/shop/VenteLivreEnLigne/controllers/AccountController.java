package com.shop.VenteLivreEnLigne.controllers;

import com.shop.VenteLivreEnLigne.models.AppRole;
import com.shop.VenteLivreEnLigne.models.AppUser;
import com.shop.VenteLivreEnLigne.repositories.AppRoleRepository;
import com.shop.VenteLivreEnLigne.repositories.AppUserRepository;
import com.shop.VenteLivreEnLigne.service.AccountServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        user.setLocked(false);
        appUserRepository.save(user);
        return "redirect:/login";
    }

    @RequestMapping("/admin/all_users")
    public String getAllUsers(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<AppUser> appUserPage = appUserRepository.findByUsernameContains(keyword, PageRequest.of(page, size));
        List<AppUser> users = appUserPage.stream()
                .map(appUser -> {
                    AppUser user = new AppUser(appUser.getId(), appUser.getUsername(), appUser.getPassword(), appUser.getEmail(), "", new ArrayList<>());
                    List<AppRole> roles = appUser.getRoles();
                    for (int i = 0; i < roles.size(); i++) {
                        if (roles.get(i).getRole().equalsIgnoreCase("CLIENT")) {
                            user.setRole("Client");
                        }
                        if (roles.get(i).getRole().equalsIgnoreCase("ADMIN")) {
                            user.setRole("Admin");
                        }
                    }
                    user.setIsMale(appUser.getIsMale());
                    user.setLocked(appUser.getLocked());
                    return user;
                })
                .toList();
        model.addAttribute("users", users);
        model.addAttribute("pages", new int[appUserPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "all-users.html";
    }

    @RequestMapping("/admin/user/lock")
    public String lockUser(@RequestParam(name = "user_id", defaultValue = "") UUID userId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        AppUser user = appUserRepository.findById(userId).orElse(null);
        user.setLocked(true);
        user.setConfirmPassword("mm");
        appUserRepository.save(user);
        return "redirect:/admin/all_users?page=" + page + "&size=" + size;
    }

    @RequestMapping("/admin/user/unlock")
    public String unlockUser(@RequestParam(name = "user_id", defaultValue = "") UUID userId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        AppUser user = appUserRepository.findById(userId).orElse(null);
        user.setLocked(false);
        user.setConfirmPassword("mm");
        appUserRepository.save(user);
        return "redirect:/admin/all_users?page=" + page + "&size=" + size;
    }
}
