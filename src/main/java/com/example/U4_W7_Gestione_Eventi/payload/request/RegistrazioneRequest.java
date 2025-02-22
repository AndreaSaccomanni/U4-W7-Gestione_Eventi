package com.example.U4_W7_Gestione_Eventi.payload.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;
@Data
public class RegistrazioneRequest {

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(min = 3, max = 15)
    private String password;

    @NotBlank
    private String cognome;
    @NotBlank
    @Email
    private String email;

    private Set<String> ruoli;
}

