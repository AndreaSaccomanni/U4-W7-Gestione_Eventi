package com.example.U4_W7_Gestione_Eventi.service;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.exception.EventoNotFoundException;
import com.example.U4_W7_Gestione_Eventi.repository.EventoRepository;
import com.example.U4_W7_Gestione_Eventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UtenteRepository utenteRepository; // Assicurati di avere un repository per Utente

    public Evento creaEvento(Long userId, String titolo, String descrizione, LocalDate dataEvento, String luogo) throws AccessDeniedException {
        // Verifica se l'utente è un organizzatore
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));
        if (!utente.isOrganizzatore()) {
            throw new AccessDeniedException("Solo gli organizzatori possono creare eventi.");
        }

        Evento evento = new Evento();
        evento.setOrganizzatore(utente); // Assicurati di avere un campo "organizzatore" in Evento
        evento.setTitolo(titolo);
        evento.setDescrizione(descrizione);
        evento.setDataEvento(dataEvento);
        evento.setLuogo(luogo);
        return eventoRepository.save(evento);
    }

    public Evento modificaEvento(Long userId, Long eventoId, String titolo, String descrizione, LocalDate dataEvento, String luogo) throws AccessDeniedException {
        // Verifica se l'utente è un organizzatore
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));
        if (!utente.isOrganizzatore()) {
            throw new AccessDeniedException("Solo gli organizzatori possono modificare eventi.");
        }

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato."));

        // Verifica che l'utente sia l'organizzatore dell'evento
        if (!evento.getOrganizzatore().getId().equals(userId)) {
            throw new AccessDeniedException("Non sei l'organizzatore di questo evento.");
        }

        evento.setTitolo(titolo);
        evento.setDescrizione(descrizione);
        evento.setDataEvento(dataEvento);
        evento.setLuogo(luogo);
        return eventoRepository.save(evento);
    }

    public void rimuoviEvento(Long userId, Long eventoId) throws AccessDeniedException {
        // Verifica se l'utente è un organizzatore
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));
        if (!utente.isOrganizzatore()) {
            throw new AccessDeniedException("Solo gli organizzatori possono rimuovere eventi.");
        }

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato."));

        // Verifica che l'utente sia l'organizzatore dell'evento
        if (!evento.getOrganizzatore().getId().equals(userId)) {
            throw new AccessDeniedException("Non sei l'organizzatore di questo evento.");
        }

        eventoRepository.delete(evento);
    }
}
