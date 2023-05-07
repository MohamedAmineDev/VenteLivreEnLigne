package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Page<Book> findByTitleContains(String title, Pageable pageable);
}
