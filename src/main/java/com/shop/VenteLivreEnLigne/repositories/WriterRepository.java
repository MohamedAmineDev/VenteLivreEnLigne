package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.Writer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WriterRepository extends JpaRepository<Writer, UUID> {
    Page<Writer> findByNameContains(String name, Pageable pageable);
}
