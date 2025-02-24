package com.example.U4_W7_Gestione_Eventi.service;

import com.example.U4_W7_Gestione_Eventi.entities.ERuolo;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.exception.EmailDuplicateException;
import com.example.U4_W7_Gestione_Eventi.exception.UsernameDuplicateException;
import com.example.U4_W7_Gestione_Eventi.payload.request.RegistrazioneRequest;
import com.example.U4_W7_Gestione_Eventi.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String insertUtente(RegistrazioneRequest userDto) throws UsernameDuplicateException, EmailDuplicateException {

        // Controlla se username o email sono già presenti nel sistema
        checkDuplicateKey(userDto.getUsername(), userDto.getEmail());

        Utente user = new Utente();

        // Se il ruolo non è specificato, assegno il ruolo predefinito "ROLE_USER"
        ERuolo ruolo = (userDto.getRuolo() == null) ? ERuolo.ROLE_USER : userDto.getRuolo();


        user.setRuolo(ruolo);
        user.setNome(userDto.getNome());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setCognome(userDto.getCognome());

        // Salva l'utente nel repository e recupera l'ID assegnato
        Long idUtente = utenteRepo.save(user).getId();


        return "L'utente " + user.getCognome() + " è stato salvato correttamente con id: " + idUtente;
    }

    // Controllo se email o username sono già presenti nel sistema
    public void checkDuplicateKey(String username, String email) throws UsernameDuplicateException, EmailDuplicateException {
        if (utenteRepo.existsByEmail(email)) {
            throw new EmailDuplicateException("Email già presente nel sistema");
        }
        if (utenteRepo.existsByUsername(username)) {
            throw new UsernameDuplicateException("Username già presente nel sistema");
        }
    }
}
