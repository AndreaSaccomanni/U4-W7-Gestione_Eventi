package com.example.U4_W7_Gestione_Eventi.security;

import com.example.U4_W7_Gestione_Eventi.security.jwt.AuthEntryPoint;
import com.example.U4_W7_Gestione_Eventi.security.jwt.FiltroAuthToken;
import com.example.U4_W7_Gestione_Eventi.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserDetailsServiceImpl detailsImpl;
    private final FiltroAuthToken filtroAuthToken;
    private final AuthEntryPoint gestoreNOAuthorization;

    public WebSecurityConfig(UserDetailsServiceImpl detailsImpl, FiltroAuthToken filtroAuthToken, AuthEntryPoint gestoreNOAuthorization) {
        this.detailsImpl = detailsImpl;
        this.filtroAuthToken = filtroAuthToken;
        this.gestoreNOAuthorization = gestoreNOAuthorization;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detailsImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(gestoreNOAuthorization))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/new").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/eventi/new").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.PUT, "/eventi/**").hasRole("EVENT_ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/eventi/**").hasRole("EVENT_ORGANIZER")
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(filtroAuthToken, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
