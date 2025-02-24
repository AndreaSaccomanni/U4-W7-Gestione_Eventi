package com.example.U4_W7_Gestione_Eventi.security.services;

import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)

                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username: " + username));

        return UserDetailsImpl.costruisciOggetto(utente);
    }
}