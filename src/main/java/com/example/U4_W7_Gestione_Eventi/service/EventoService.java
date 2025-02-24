package com.example.U4_W7_Gestione_Eventi.service;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.payload.EventoDTO;
import com.example.U4_W7_Gestione_Eventi.repository.EventoRepository;
import com.example.U4_W7_Gestione_Eventi.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@Transactional
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    public Evento creaEvento(Long userId, EventoDTO eventoDTO) throws AccessDeniedException {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));

        if (!utente.isOrganizzatore()) {
            throw new AccessDeniedException("Solo gli organizzatori possono creare eventi.");
        }

        Evento evento = new Evento();
        evento.setOrganizzatore(utente);
        evento.setTitolo(eventoDTO.getTitolo());
        evento.setDescrizione(eventoDTO.getDescrizione());
        evento.setDataEvento(eventoDTO.getDataEvento());
        evento.setLuogo(eventoDTO.getLuogo());
        evento.setPostiDisponibili(eventoDTO.getPostiDisponibili());

        utente.getEventiOrganizzati().add(evento);
        return eventoRepository.save(evento);
    }

    public Evento modificaEvento(Long userId, Long eventoId, EventoDTO eventoDTO) throws AccessDeniedException {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));

        if (!utente.isOrganizzatore()) {
            throw new AccessDeniedException("Solo gli organizzatori possono modificare eventi.");
        }

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato."));

        if (!evento.getOrganizzatore().getId().equals(userId)) {
            throw new AccessDeniedException("Non sei l'organizzatore di questo evento.");
        }

        evento.setTitolo(eventoDTO.getTitolo());
        evento.setDescrizione(eventoDTO.getDescrizione());
        evento.setDataEvento(eventoDTO.getDataEvento());
        evento.setLuogo(eventoDTO.getLuogo());
        evento.setPostiDisponibili(eventoDTO.getPostiDisponibili());

        return eventoRepository.save(evento);
    }

    public void rimuoviEvento(Long userId, Long eventoId) throws AccessDeniedException {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));

        if (!utente.isOrganizzatore()) {
            throw new AccessDeniedException("Solo gli organizzatori possono rimuovere eventi.");
        }

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato."));

        if (!evento.getOrganizzatore().getId().equals(userId)) {
            throw new AccessDeniedException("Non sei l'organizzatore di questo evento.");
        }

        eventoRepository.delete(evento);
    }
}
