package com.example.U4_W7_Gestione_Eventi.security.services;

import com.example.U4_W7_Gestione_Eventi.entities.ERuolo;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;

    private ERuolo ruolo;

    @JsonIgnore
    private String password;

    // Rimuovi il campo ruoli (può essere derivato direttamente dal ruolo)
    // private Collection<? extends GrantedAuthority> ruoli;

    public static UserDetailsImpl costruisciOggetto(Utente user) {

        // Creazione di un oggetto ruoli come List di GrantedAuthority
        List<GrantedAuthority> ruoliUtente = List.of(new SimpleGrantedAuthority(user.getRuolo().name()));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRuolo(),
                user.getPassword()
        );
    }

    public Long getId() {
        return id;
    }

    // Restituisce un elenco di authorities basato sul ruolo dell'utente
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruolo.name()));  // Restituisce una sola authority basata sul ruolo
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
