package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.AppRole;
import com.shop.VenteLivreEnLigne.models.AppUser;

public interface AccountService {
    AppUser addNewUser(String username, String password, String email, String confirmPassword);

    AppRole addNewRole(String role);

    void addRoleToUser(String username, String role);

    void removeRoleFromUser(String username, String role);

    AppUser loadUserByUsername(String username);
}
