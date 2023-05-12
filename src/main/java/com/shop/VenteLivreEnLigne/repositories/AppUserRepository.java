package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.AppUser;
import com.shop.VenteLivreEnLigne.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    AppUser findByUsername(String username);

    //@Query("select u from AppUser u where u.locked=false")
    AppUser findByEmail(String email);

    Page<AppUser> findByUsernameContains(String username, Pageable pageable);
}
