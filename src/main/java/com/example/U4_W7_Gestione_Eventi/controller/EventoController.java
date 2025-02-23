package com.example.U4_W7_Gestione_Eventi.controller;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.payload.request.CreazioneEventoRequest;
import com.example.U4_W7_Gestione_Eventi.payload.request.ModificaEventoRequest;
import com.example.U4_W7_Gestione_Eventi.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Endpoint per creare un evento
    @PostMapping("/new")
    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<?> creaEvento(@AuthenticationPrincipal Utente utente,
                                        @RequestBody CreazioneEventoRequest eventoRequest,
                                        BindingResult bindingResult) {
        try {
            // Verifica se l'utente è un organizzatore
            if (!utente.isOrganizzatore()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo gli organizzatori possono creare eventi.");
            }

            // Controllo errori di validazione
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.joining("\n"));
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // Creazione evento
            Evento nuovoEvento = eventoService.creaEvento(
                    utente.getId(),
                    eventoRequest.getTitolo(),
                    eventoRequest.getDescrizione(),
                    eventoRequest.getDataEvento(),
                    eventoRequest.getLuogo()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(nuovoEvento);
        } catch (IllegalArgumentException | AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint per modificare un evento
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<?> modificaEvento(@AuthenticationPrincipal Utente utente,
                                            @PathVariable Long id,
                                            @RequestBody ModificaEventoRequest eventoRequest,
                                            BindingResult bindingResult) {
        try {
            // Verifica se l'utente è un organizzatore
            if (!utente.isOrganizzatore()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo gli organizzatori possono modificare eventi.");
            }

            // Controllo errori di validazione
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.joining("\n"));
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // Modifica evento
            Evento eventoModificato = eventoService.modificaEvento(
                    utente.getId(),
                    id,
                    eventoRequest.getTitolo(),
                    eventoRequest.getDescrizione(),
                    eventoRequest.getDataEvento(),
                    eventoRequest.getLuogo()
            );

            return ResponseEntity.ok(eventoModificato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint per eliminare un evento
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<?> rimuoviEvento(@AuthenticationPrincipal Utente utente,
                                           @PathVariable Long id) {
        try {
            // Verifica se l'utente è un organizzatore
            if (!utente.isOrganizzatore()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo gli organizzatori possono rimuovere eventi.");
            }

            // Rimozione evento
            eventoService.rimuoviEvento(utente.getId(), id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }
}