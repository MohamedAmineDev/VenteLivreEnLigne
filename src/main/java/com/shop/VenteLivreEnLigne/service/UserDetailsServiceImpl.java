package com.shop.VenteLivreEnLigne.service;

import com.shop.VenteLivreEnLigne.models.AppUser;
import com.shop.VenteLivreEnLigne.repositories.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User was not found !");
        }
        String[] myRoles = appUser.getRoles().stream().map(role -> role.getRole()).toArray(String[]::new);
        return User.withUsername(appUser.getUsername()).password(appUser.getPassword())
                .roles(myRoles)
                .build()
                ;
    }
}
