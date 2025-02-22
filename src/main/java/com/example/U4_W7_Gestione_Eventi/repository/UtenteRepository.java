package com.example.U4_W7_Gestione_Eventi.repository;

import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByEmail(String email);

    Optional<Utente> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
