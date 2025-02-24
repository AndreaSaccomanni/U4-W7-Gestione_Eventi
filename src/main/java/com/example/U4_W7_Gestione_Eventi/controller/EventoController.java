package com.example.U4_W7_Gestione_Eventi.controller;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.payload.EventoDTO;
import com.example.U4_W7_Gestione_Eventi.payload.request.CreazioneEventoRequest;
import com.example.U4_W7_Gestione_Eventi.payload.request.ModificaEventoRequest;
import com.example.U4_W7_Gestione_Eventi.security.services.UserDetailsImpl;
import com.example.U4_W7_Gestione_Eventi.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping("/new")
    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<?> creaEvento(@RequestBody @Validated CreazioneEventoRequest eventoRequest,
                                        BindingResult validation,
                                        @AuthenticationPrincipal(errorOnInvalidType=true) UserDetailsImpl utenteImpl) {

        Utente utente  = utenteImpl.getUser();
        // Gestione validazione dei dati
        if (validation.hasErrors()) {
            String errorMessage = validation.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            EventoDTO eventoDTO = new EventoDTO(
                    eventoRequest.getTitolo(),
                    eventoRequest.getDescrizione(),
                    eventoRequest.getDataEvento(),
                    eventoRequest.getLuogo(),
                    eventoRequest.getPostiDisponibili()
            );

            Evento nuovoEvento = eventoService.creaEvento(utente.getId(), eventoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuovoEvento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server.");
        }
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<?> modificaEvento(@AuthenticationPrincipal Utente utente,
                                            @PathVariable Long id,
                                            @RequestBody @Validated ModificaEventoRequest eventoRequest,
                                            BindingResult validation) {

        if (validation.hasErrors()) {
            String errorMessage = validation.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            EventoDTO eventoDTO = new EventoDTO(
                    eventoRequest.getTitolo(),
                    eventoRequest.getDescrizione(),
                    eventoRequest.getDataEvento(),
                    eventoRequest.getLuogo(),
                    eventoRequest.getPostiDisponibili()
            );

            Evento eventoModificato = eventoService.modificaEvento(utente.getId(), id, eventoDTO);
            return ResponseEntity.ok(eventoModificato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento non trovato.");
        }
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<?> rimuoviEvento(@AuthenticationPrincipal Utente utente,
                                           @PathVariable Long id) {
        if (!utente.isOrganizzatore()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo gli organizzatori possono rimuovere eventi.");
        }

        try {

            eventoService.rimuoviEvento(utente.getId(), id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento non trovato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server.");
        }
    }
}
