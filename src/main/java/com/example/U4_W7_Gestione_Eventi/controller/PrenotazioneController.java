package com.example.U4_W7_Gestione_Eventi.controller;


import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.payload.request.CreazionePrenotazioneRequest;
import com.example.U4_W7_Gestione_Eventi.security.services.UserDetailsImpl;
import com.example.U4_W7_Gestione_Eventi.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.stream.Collectors;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    // Endpoint per creare una prenotazione
    @PostMapping("/new")
    @PreAuthorize("hasRole('USER')") // Solo gli utenti con ruolo USER possono creare prenotazioni
    public ResponseEntity<?> creaPrenotazione(@Validated @RequestBody CreazionePrenotazioneRequest prenotazioneRequest,
                                              BindingResult bindingResult) {
        try {
            // Recupera l'utente autenticato dal contesto di sicurezza
            Authentication autenticazione = SecurityContextHolder.getContext().getAuthentication();
            if (autenticazione == null || !(autenticazione.getPrincipal() instanceof UserDetailsImpl)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non autenticato.");
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) autenticazione.getPrincipal();
            Utente utente = userDetails.getUtente(); // Recupera l'oggetto Utente completo

            // Controllo errori di validazione
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.joining("\n"));
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // Creazione prenotazione
            ResponseEntity<?> nuovaPrenotazione = prenotazioneService.creaPrenotazione(
                    utente.getId(),
                    prenotazioneRequest.getEventoId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(nuovaPrenotazione);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}