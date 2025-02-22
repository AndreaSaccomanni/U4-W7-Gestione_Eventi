package com.example.U4_W7_Gestione_Eventi.security;

import com.example.U4_W7_Gestione_Eventi.security.jwt.AuthEntryPoint;
import com.example.U4_W7_Gestione_Eventi.security.jwt.FiltroAuthToken;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl detailsImpl;

    @Autowired
    private FiltroAuthToken filtroAuthToken;

    @Autowired
    private AuthEntryPoint gestoreNOAuthorization;

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

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(detailsImpl);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(gestoreNOAuthorization))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/user/new").permitAll()
                                .anyRequest().authenticated());

        // Aggiungi il filtro di autenticazione JWT alla catena di filtri
        http.addFilterBefore(filtroAuthToken, UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(authenticationProvider());
        return http.build();
    }
}
