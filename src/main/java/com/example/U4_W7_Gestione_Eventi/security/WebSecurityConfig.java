package com.example.U4_W7_Gestione_Eventi.security;


import com.example.U4_W7_Gestione_Eventi.security.jwt.AuthEntryPoint;
import com.example.U4_W7_Gestione_Eventi.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl detailsImpl; // CONTIENE I DETTAGLI

    @Autowired
    private AuthEntryPoint gestoreNOAuthorization;

    // spring crea in automatico un ogggetto passwordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }


    // FORNISCE L'AUTENTICAZIONE ATTRAVERSO I DETTAGLI (USERNAME, PASSWORD)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // gestione di come deve essere creato e inizializzato l'oggetto DaoAuthenticationProvider
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        //l'oggetto importa tutti i dettagli dell'uutente
        auth.setUserDetailsService(detailsImpl);

        //DaoAuthenticationProviedr fornisce un metodo per accettare la  password criptata
        auth.setPasswordEncoder(passwordEncoder());

        return auth;
    }

    //disabilitare il csrf ->
    //impostare il gestore delle autorizzazioni
    //gestione della sessione
    //gestione autorizzazione sulle richieste
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(gestoreNOAuthorization))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("api/users/**").permitAll()
                                .requestMatchers("api/product/**").permitAll()
                                .anyRequest().authenticated());

        http.authenticationProvider((authenticationProvider()));
        return http.build(); //ritorna un securityFilterChain
    }

}