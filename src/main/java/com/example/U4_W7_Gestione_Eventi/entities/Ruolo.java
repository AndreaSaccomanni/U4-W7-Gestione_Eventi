package com.example.U4_W7_Gestione_Eventi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ruoli")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ERuolo nomeRuolo;

    public Ruolo(ERuolo nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }
}

