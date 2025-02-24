package com.example.U4_W7_Gestione_Eventi.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreazionePrenotazioneRequest {

    @NotNull(message = "L'ID dell'evento è obbligatorio")
    private Long eventoId;

    // Getter e Setter
    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}