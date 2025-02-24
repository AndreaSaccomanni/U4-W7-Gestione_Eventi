package com.example.U4_W7_Gestione_Eventi.service;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import com.example.U4_W7_Gestione_Eventi.entities.Prenotazione;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.repository.EventoRepository;
import com.example.U4_W7_Gestione_Eventi.repository.PrenotazioneRepository;
import com.example.U4_W7_Gestione_Eventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional
    public ResponseEntity<?> creaPrenotazione(Long eventoId, Long utenteId) {

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        // controllo per vedere se ci sono posti disponibili
        if (evento.getPrenotazioni().size() >= evento.getPostiDisponibili()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non ci sono più posti disponibili per questo evento.");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setEvento(evento);

        prenotazioneRepository.save(prenotazione);

        return ResponseEntity.status(HttpStatus.CREATED).body("Prenotazione creata con successo.");
    }
}