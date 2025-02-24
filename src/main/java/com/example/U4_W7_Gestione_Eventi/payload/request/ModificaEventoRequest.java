package com.example.U4_W7_Gestione_Eventi.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificaEventoRequest {
    private String titolo;
    private String descrizione;
    private LocalDate dataEvento;
    private String luogo;
    private int postiDisponibili;


}
