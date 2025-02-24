package com.example.U4_W7_Gestione_Eventi.payload;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    @NotBlank(message = "Il titolo è obbligatorio")
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    @NotNull(message = "La data dell'evento è obbligatoria")
    private LocalDate dataEvento;

    @NotBlank(message = "Il luogo è obbligatorio")
    private String luogo;

    @Min(value = 1, message = "Il numero di posti disponibili deve essere almeno 1")
    private int postiDisponibili;
}
