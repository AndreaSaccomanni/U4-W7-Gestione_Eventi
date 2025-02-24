package com.example.U4_W7_Gestione_Eventi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "utenti")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERuolo ruolo;

    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.ALL)
    private Set<Evento> eventiOrganizzati = new HashSet<>();

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private Set<Prenotazione> prenotazioni = new HashSet<>();

    public Utente(String nome, String cognome, String username, String password, String email, ERuolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.email = email;
        this.ruolo = ruolo;
    }

    public boolean isOrganizzatore() {
        return this.ruolo == ERuolo.ROLE_EVENT_ORGANIZER;
    }

    @Override
    public String toString() {
        return "Utente{" +

                ", nome='" + nome + '\'' +

                '}';
    }
}

