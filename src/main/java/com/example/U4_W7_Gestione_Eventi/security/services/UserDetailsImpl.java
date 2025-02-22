package com.example.U4_W7_Gestione_Eventi.security.services;

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

//L'interfaccia userDetails contiene le info necessarie  durante l'autenticazione
//per personalizzare i dettagli da inserire nel token JWT
// aggiungendo email, id
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    //Si usa collection perche raggruppa List  Map HashMap,
    // all'intenro ci devono essere filgi di GrantedAuthority
    private Collection<? extends GrantedAuthority> ruoli;


    public static UserDetailsImpl costruisciOgggetto(Utente user) {

        //ogni oggetto ruolo lo trasfonrmo in un oggetto SimpleGrantedAuthority che contiene il nome del ruolo
        List<GrantedAuthority> ruoliUtente = user.getRuolo().stream().map(ruolo -> new SimpleGrantedAuthority(ruolo.getNomeRuolo().name())).collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getIdUtente(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                ruoliUtente);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ruoli;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
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
