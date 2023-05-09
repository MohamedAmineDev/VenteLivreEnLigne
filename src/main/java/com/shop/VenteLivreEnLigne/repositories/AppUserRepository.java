package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    AppUser findByUsername(String username);

    AppUser findByEmail(String email);
}
