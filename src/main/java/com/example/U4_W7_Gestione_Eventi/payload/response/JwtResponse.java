package com.example.U4_W7_Gestione_Eventi.payload.response;

import com.example.U4_W7_Gestione_Eventi.entities.ERuolo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    //info chhe ritorneremo all'interno di un oggetto JSON tramite ResponseEntity

    private String username;
    private Long id;
    private String email;
    private String ruolo;

    private String token;
    private String type="Bearer ";


    public JwtResponse(String username, Long id, String email, String ruolo, String token) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.ruolo = ruolo;
        this.token = token;
    }
}
