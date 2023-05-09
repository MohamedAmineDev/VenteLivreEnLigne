package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommandRepository extends JpaRepository<Command, UUID> {
}
