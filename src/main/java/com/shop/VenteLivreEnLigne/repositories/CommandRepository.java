package com.shop.VenteLivreEnLigne.repositories;

import com.shop.VenteLivreEnLigne.models.Book;
import com.shop.VenteLivreEnLigne.models.Command;
import com.shop.VenteLivreEnLigne.models.CommandStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommandRepository extends JpaRepository<Command, UUID> {
    @Query("select c from Command c where c.user.id=?1")
    Page<Command> findAllCommandsByUserId(UUID userId, Pageable pageable);

    @Query("select c from Command c where c.user.id=?1 and c.status=?2")
    Page<Command> findAllCommandsByStatus(UUID userId, CommandStatus commandStatus, Pageable pageable);

    @Query("select c from Command c where c.status=?1")
    Page<Command> findAllCommandsByStatus(CommandStatus commandStatus, Pageable pageable);

    @Query("select c from Command c")
    Page<Command> findAllCommands(Pageable pageable);
}
