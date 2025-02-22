package com.example.U4_W7_Gestione_Eventi.repository;

import com.example.U4_W7_Gestione_Eventi.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

//    List<Prenotazione> findByUtenteId(Long utenteId);
//
//    public List<Prenotazione> findByEvento_Id(Long idEvento);



}
