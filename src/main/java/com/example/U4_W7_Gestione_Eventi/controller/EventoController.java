package com.example.U4_W7_Gestione_Eventi.controller;

import com.example.U4_W7_Gestione_Eventi.entities.Evento;
import com.example.U4_W7_Gestione_Eventi.payload.request.CreazioneEventoRequest;
import com.example.U4_W7_Gestione_Eventi.security.services.UserDetailsImpl;
import com.example.U4_W7_Gestione_Eventi.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping("/new")
    public ResponseEntity<?> creaEvento(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody CreazioneEventoRequest eventoRequest,
                                        BindingResult bindingResult) {
        try {
            // Controllo errori di validazione
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.joining("\n"));
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // Creazione evento
            Evento nuovoEvento = eventoService.creaEvento(
                    userDetails.getId(),
                    eventoRequest.getTitolo(),
                    eventoRequest.getDescrizione(),
                    eventoRequest.getDataEvento(),
                    eventoRequest.getLuogo()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(nuovoEvento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
