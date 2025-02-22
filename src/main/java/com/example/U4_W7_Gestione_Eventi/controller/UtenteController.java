package com.example.U4_W7_Gestione_Eventi.controller;

import com.example.U4_W7_Gestione_Eventi.exception.EmailDuplicateException;
import com.example.U4_W7_Gestione_Eventi.exception.UsernameDuplicateException;
import com.example.U4_W7_Gestione_Eventi.payload.request.LoginRequest;
import com.example.U4_W7_Gestione_Eventi.payload.request.RegistrazioneRequest;
import com.example.U4_W7_Gestione_Eventi.payload.response.JwtResponse;
import com.example.U4_W7_Gestione_Eventi.security.jwt.JwtUtils;
import com.example.U4_W7_Gestione_Eventi.security.services.UserDetailsImpl;
import com.example.U4_W7_Gestione_Eventi.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UtenteController {

    @Autowired
    private UtenteService servizi;

    @Autowired
    AuthenticationManager managerAuth;

    @Autowired
    JwtUtils utilitiesJwt;

    @PostMapping("/new")
    public ResponseEntity<String> inserisciUtente(@RequestBody @Validated RegistrazioneRequest nuovoUtente, BindingResult validazione) {
        try {
            if(validazione.hasErrors()){
                String messaggioErrore = "ERRORE DI VALIDAZIONE";
                for(ObjectError errore : validazione.getAllErrors()){
                    messaggioErrore += errore.getDefaultMessage()+ "\n";
                }
                return new ResponseEntity<>(messaggioErrore, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(servizi.insertUtente(nuovoUtente), HttpStatus.CREATED);
        } catch (UsernameDuplicateException e) {
            return new ResponseEntity<>("Username gia utilizzato", HttpStatus.BAD_REQUEST);
        } catch (EmailDuplicateException e) {
            return new ResponseEntity<>("Email gia utilizzata", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginDto, BindingResult validazione){
        if(validazione.hasErrors()){
            String messaggioErrore = "ERRORE DI VALIDAZIONE";
            for(ObjectError errore : validazione.getAllErrors()){
                messaggioErrore += errore.getDefaultMessage()+ "\n";
            }
            return new ResponseEntity<>(messaggioErrore.toString(), HttpStatus.BAD_REQUEST);
        }

        //creo un oggetto che occorre per l'autenticazione
        UsernamePasswordAuthenticationToken tokenNOAuthentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //invocare e recuperare l'authentication -> autenticazionne va a buon fine
        //utilizzo ii gestore delle autenticazini che si basa su username e password
        //recuperiamo l'autenticazione attraverso il metodo authenticate()
        Authentication autenticazione = managerAuth.authenticate(tokenNOAuthentication);

        //impostare l'autenticazione nel contesto di sicurezza String
        SecurityContextHolder.getContext().setAuthentication(autenticazione);

        //genero il token finale in formato stringa
        String token = utilitiesJwt.creaJwtToken(autenticazione);


        //recupero le info che voglio inserire nwll risposta al client
        UserDetailsImpl dettagliUtente = (UserDetailsImpl) autenticazione.getPrincipal();
        List<String>  ruoliWeb = dettagliUtente.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        //creo un oggetto JWTResponse che andrà nel ResponseEntity
        JwtResponse responseJwt = new JwtResponse(
                dettagliUtente.getUsername(),
                dettagliUtente.getId(),
                dettagliUtente.getPassword(),
                ruoliWeb,
                dettagliUtente.getEmail(),
                token
        );

        // gestione della risposta al client -> ResponseEntity
        return new ResponseEntity<>(responseJwt, HttpStatus.OK);

    }

}

