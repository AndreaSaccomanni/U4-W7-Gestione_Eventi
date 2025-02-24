package com.example.U4_W7_Gestione_Eventi.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreazioneEventoRequest {

    @NotBlank(message = "Il campo 'titolo' è obbligatorio")
    private String titolo;

    @NotBlank(message = "Il campo 'descrizione' è obbligatorio")
    private String descrizione;

    @NotNull(message = "La data dell'evento è obbligatoria")
    private LocalDate dataEvento;

    @NotBlank(message = "Il campo 'luogo' è obbligatorio")
    private String luogo;

    @NotBlank(message = "Il campo 'posti disponibili' è obbligatorio")
    private int postiDisponibili;
}

