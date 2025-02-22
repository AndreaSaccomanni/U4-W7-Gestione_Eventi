package com.example.U4_W7_Gestione_Eventi.service;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.exception.EventoNotFoundException;
import com.example.U4_W7_Gestione_Eventi.repository.EventoRepository;
import com.example.U4_W7_Gestione_Eventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UtenteRepository utenteRepository;


    public Evento creaEvento(Long idUtente, String titolo, String descrizione, LocalDate dataEvento, String luogo) {

        Utente utente = utenteRepository.findById(idUtente).orElseThrow(() ->
                new IllegalArgumentException("Utente non trovato"));

        // Verifica RUOLO UTENTE
        if (utente.getRuolo().stream().noneMatch(ruolo -> ruolo.getNomeRuolo().equals("ORGANIZZATORE"))) {
            throw new IllegalArgumentException("Solo gli organizzatori possono creare eventi.");
        }


        Evento evento = new Evento();
        evento.setTitolo(titolo);
        evento.setDescrizione(descrizione);
        evento.setDataEvento(dataEvento);
        evento.setLuogo(luogo);
        evento.setOrganizzatore(utente);

        return eventoRepository.save(evento);
    }

    public List<Evento> trovaTutti() {
        return eventoRepository.findAll();
    }


    public Evento trovaPerId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNotFoundException("Evento non trovato con ID: " + id));
    }

    public Evento salvaEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Evento aggiornaEvento(Long id, Evento eventoDettagli) {
        Evento evento = trovaPerId(id);
        evento.setTitolo(eventoDettagli.getTitolo());
        evento.setDescrizione(eventoDettagli.getDescrizione());
        evento.setDataEvento(eventoDettagli.getDataEvento());
        evento.setLuogo(eventoDettagli.getLuogo());

        return eventoRepository.save(evento);
    }

    public void eliminaEvento(Long id) {
        Evento evento = trovaPerId(id);
        eventoRepository.delete(evento);
    }




}
