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
    UtenteRepository repoUser;

    //metodo dell'interfaccia UserDetailsService gia presente in Spring
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utente> utente = (repoUser.findByUsername(username));

        Utente user = utente.orElseThrow();

        //ritornare un oggetto di tipo interfaccia UserDetails
        //contenitore delle info che inserirò nel token
        return UserDetailsImpl.costruisciOggetto(user);
    }
}