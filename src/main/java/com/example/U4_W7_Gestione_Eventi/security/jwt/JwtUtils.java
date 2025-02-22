package com.example.U4_W7_Gestione_Eventi.security.jwt;

import com.example.U4_W7_Gestione_Eventi.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
@Component
// funzionalità principali del token
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private String jwtExpiration;

    //Creazione del JWT
    //Authentication ha le info dell'utente che devo recuperare
    public String creaJwtToken(Authentication autenticazione){
        //recupero il dettaglio principal (username)
        UserDetailsImpl utentePrincipal = (UserDetailsImpl) autenticazione.getPrincipal();

        //creaizone del jwt
        //con setExpiration setto la data di scadenza, il giorno di oggi in millisecondi
        //più il valore nel properties di jwt.expiration che è in millisecondi
        return Jwts.builder()
                .setSubject(utentePrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(recuperoChiave(), SignatureAlgorithm.HS256)
                .compact();
    }

    //recupero username dal JWT
    public String recuperoUsernameDaToken(String token){
        String username= Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parseClaimsJwt(token).getBody().getSubject();
        return username;
    }

    //recupero username dal JWT
    public Date recuperoScandenzaDaToken(String token){
        Date scadenza= Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parseClaimsJwt(token).getBody().getExpiration();
        return scadenza;
    }

    //validazione del TOKEN JWT
    public boolean validazioneJwtToken(String token){
        Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parse(token);
        return true;
    }

    //recupero della chiave
    public Key recuperoChiave(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }





}
