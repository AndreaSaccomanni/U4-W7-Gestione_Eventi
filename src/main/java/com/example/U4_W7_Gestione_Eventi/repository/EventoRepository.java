package com.example.U4_W7_Gestione_Eventi.repository;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByLuogo(String luogo);

    List<Evento> findByDataEvento(LocalDate dataEvento);

    List<Evento> findByPostiDisponibiliGreaterThan(int posti);

}
