package com.example.U4_W7_Gestione_Eventi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "eventi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evento {


    @Id
    @GeneratedValue
    private Long idEvento;

    private String titolo;
    private String descrizione;
    private LocalDate dataEvento;
    private String luogo;
    private int postiDisponibili;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizzatore_id")
    private Utente organizzatore;

    @OneToMany(mappedBy = "evento")
    private List<Prenotazione> prenotazioni;
}
