package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Page<Book> findByTitleContains(String title, Pageable pageable);

    @Query("select b from Book b where b.category.id=?1")
    List<Book> findByCategoryById(UUID categoryId);
}
