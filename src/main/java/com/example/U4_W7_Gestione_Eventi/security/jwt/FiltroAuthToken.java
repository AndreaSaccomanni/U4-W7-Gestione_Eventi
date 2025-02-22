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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class FiltroAuthToken extends OncePerRequestFilter {

    @Autowired
    JwtUtils utils;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Ottengo un JWT dall'intestazione Authorization
        String jwt = analizzaJwt(request);

        // Se la richiesta contiene un JWT valido, viene convalidato
        if (jwt != null && utils.validazioneJwtToken(jwt)) {
            // Recupero l'username dal token JWT
            String username = utils.recuperoUsernameDaToken(jwt);

            // Recupero i dettagli dell'utente
            UserDetails dettagliUtente = userDetailsService.loadUserByUsername(username);

            // Creo un oggetto di autenticazione (UsernamePasswordAuthenticationToken)
            UsernamePasswordAuthenticationToken autenticazione =
                    new UsernamePasswordAuthenticationToken(
                            dettagliUtente,
                            null,
                            dettagliUtente.getAuthorities()
                    );

            // Imposto i dettagli dell'oggetto di autenticazione
            autenticazione.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Imposto l'autenticazione nel contesto di sicurezza di Spring
            SecurityContextHolder.getContext().setAuthentication(autenticazione);
        }

        // Passo la richiesta al filtro successivo nella catena
        filterChain.doFilter(request, response);
    }

    private String analizzaJwt(HttpServletRequest request) {
        // Recupero l'intestazione Authorization dalla richiesta HTTP
        String headAutenticazione = request.getHeader("Authorization");

        // Controllo se l'intestazione contiene un token di tipo "Bearer"
        if (StringUtils.hasText(headAutenticazione) && headAutenticazione.startsWith("Bearer ")) {
            // Rimuovo la parte "Bearer " e restituisco il token JWT
            return headAutenticazione.substring(7);
        }

        return null;
    }
}
