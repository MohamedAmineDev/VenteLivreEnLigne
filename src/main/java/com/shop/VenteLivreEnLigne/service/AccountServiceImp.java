package com.shop.VenteLivreEnLigne.service;

import com.shop.VenteLivreEnLigne.models.AppRole;
import com.shop.VenteLivreEnLigne.models.AppUser;
import com.shop.VenteLivreEnLigne.repositories.AccountService;
import com.shop.VenteLivreEnLigne.repositories.AppRoleRepository;
import com.shop.VenteLivreEnLigne.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser != null) {
            throw new RuntimeException("This user already exists !");
        }
        if (!password.equalsIgnoreCase(confirmPassword)) {
            throw new RuntimeException("Passwords not match !");
        }
        appUser = AppUser.builder()
                .id(null)
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
        if (appRole != null) {
            throw new RuntimeException("This role already exists !");
        }
        appRole = AppRole.builder()
                .role(role)
                .build();
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
        if (appUser == null) {
            throw new RuntimeException("User not found !");
        }
        if (appRole == null) {
            throw new RuntimeException("Role not found !");
        }
        appUser.getRoles().add(appRole);
    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
        if (appUser == null) {
            throw new RuntimeException("User not found !");
        }
        if (appRole == null) {
            throw new RuntimeException("Role not found !");
        }
        appUser.getRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

}
