package com.example.U4_W7_Gestione_Eventi.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//filtro per l'autenticazione
//OncePerRequestFilter -> classe astratta presente nella libreria
public class FiltroAuthToken extends OncePerRequestFilter {

    @Autowired
    JwtUtils utils;


    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //ottengo un JWT dai cookie http

        String jwt = analiizaJwt((request));

        // se la richiesta presenta un JWT, viene convalidata
        if(jwt != null && utils.validazioneJwtToken(jwt)){

            //recupero l'username dal token JWT
            String username = utils.recuperoUsernameDaToken(jwt);

            //recupero UserDetails(dettagli  del token) da Username -> creare un oggetto Authentication
            UserDetails dettagliUtente = userDetailsService.loadUserByUsername(username);

            //creazione di un oggetto UsernamePasswordAuthenicationToken
            UsernamePasswordAuthenticationToken autenticazione =
                    new UsernamePasswordAuthenticationToken(
                            dettagliUtente,
                            null,
                            dettagliUtente.getAuthorities()
                    );
            // setto i dettagli dell'oggetto UsernamePasswordAuthenticationToken
            autenticazione.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //imposto userDetails corrente nel contesto (ambiente) di security
            SecurityContextHolder.getContext().setAuthentication(autenticazione);
        }
    }

    private String analiizaJwt(HttpServletRequest request) {
        String headAutenticazione = request.getHeader("Authorization");
        //controllo sulla presenza di testo nel valore di autenticazione
        // controllo se il valore recuperato inizia con "Bearer "
        //Bearer 80d78a3dad1103b23937ed0787cfbc87
        if (StringUtils.hasText(headAutenticazione) && (headAutenticazione.startsWith("Bearer "))) {
            return headAutenticazione.substring(7);
        }

        return null;
    }
}
