package com.example.U4_W7_Gestione_Eventi.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UtentePayload {
    @NotBlank(message = "Il campo 'username' non può essere vuoto")
    private String username;

    @NotBlank(message = "Il campo 'nome' non può essere vuoto")
    private String nome;

    @NotBlank(message = "Il campo 'cognome' non può essere vuoto")
    private String cognome;

    @NotBlank(message = "Il campo 'email' non può essere vuoto")
    @Email(message = "Inserire una email valida")
    private String email;


}
