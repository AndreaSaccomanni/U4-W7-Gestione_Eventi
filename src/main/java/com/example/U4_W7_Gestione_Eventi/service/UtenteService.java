package com.example.U4_W7_Gestione_Eventi.service;


import com.example.U4_W7_Gestione_Eventi.entities.ERuolo;
import com.example.U4_W7_Gestione_Eventi.entities.Ruolo;
import com.example.U4_W7_Gestione_Eventi.entities.Utente;
import com.example.U4_W7_Gestione_Eventi.exception.EmailDuplicateException;
import com.example.U4_W7_Gestione_Eventi.exception.UsernameDuplicateException;
import com.example.U4_W7_Gestione_Eventi.payload.UtentePayload;
import com.example.U4_W7_Gestione_Eventi.payload.request.RegistrazioneRequest;
import com.example.U4_W7_Gestione_Eventi.repository.RuoloRepository;
import com.example.U4_W7_Gestione_Eventi.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private UtentePayload utenteDTO;

@Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RuoloRepository ruoloRepo;

















//    public String insertUtente(RegistrazioneRequest userDto) throws UsernameDuplicateException, EmailDuplicateException {
//
//        checkDuplicateKey(userDto.getUsername(), userDto.getEmail());
//
//        Utente user = new Utente();
//
//        Set<Ruolo> ruoliUser = new HashSet<>();
//
//        // Gestione dei ruoli -> Set<String> in Set<Ruolo>
//        if (userDto.getRuoli() == null || userDto.getRuoli().isEmpty()) {
//            // Se il Set di ruoli è null o vuoto, assegno il ruolo di default "ROLE_USER"
//            Ruolo defaultRole = ruoloRepo.findByNomeRuolo(ERuolo.ROLE_USER)
//                    .orElseThrow(() -> new IllegalArgumentException("Ruolo di default non trovato"));
//            ruoliUser.add(defaultRole);
//        } else {
//            // Altrimenti, cerco ogni ruolo nel Set di stringhe
//            for (String ruoloStr : userDto.getRuoli()) {
//                // Verifica se il ruolo è valido
//                ERuolo ruoloEnum = ERuolo.valueOf(ruoloStr); // Converte la stringa in un enum
//                Ruolo ruolo = ruoloRepo.findByNomeRuolo(ruoloEnum)
//                        .orElseThrow(() -> new IllegalArgumentException("Ruolo " + ruoloStr + " non trovato"));
//                ruoliUser.add(ruolo);
//            }
//        }
//
//        // Imposto i ruoli dell'utente
//        user.setRuolo(ruoliUser);
//
//        // Popolo i dati dell'utente con i dati dalla registrazione
//        user.setUsername(userDto.getUsername());
//        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Assicurati di usare un encoder per la password
//        user.setEmail(userDto.getEmail());
//        user.setCognome(userDto.getCognome());
//
//
//
//        Long idUtente = utenteRepo.save(user).getIdUtente();
//        return "L'utente " + user.getCognome() + " è stato salvato correttamente con id: " + idUtente;
//    }
//
//
//    public void checkDuplicateKey(String username, String email) throws UsernameDuplicateException, EmailDuplicateException {
//        if (utenteRepo.existsByEmail(email)) {
//            throw new EmailDuplicateException("Email gia presente nel sistema");
//        }
//        if (utenteRepo.existsByUsername(username)) {
//            throw new UsernameDuplicateException("Email gia presente nel sistema");
//        }
//    }


}
