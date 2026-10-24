package com.lias.gestionlabo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // ── 1. Public — sans login ──────────────────────────────────
                .requestMatchers(
                    "/", "/login", "/public/**",
                    "/css/**", "/js/**", "/images/**",
                    "/uploads/**", "/adhesion/**"
                ).permitAll()

                // ── 2. Admin système + Directeur ────────────────────────────
                .requestMatchers(
                    "/mandats/**",
                    "/rapport/**",
                    "/admin/rapport/**",
                    "/demandes/**",
                    "/roles/**"
                ).hasAnyAuthority(
                    "ROLE_DIRECTEUR", "DIRECTEUR",
                    "ROLE_ADMIN",     "ADMIN"
                )

                // ── 3. Gestion membres ───────────────────────────────────────
                .requestMatchers("/membres/**")
                .hasAnyAuthority(
                    "ROLE_PERMANENT", "PERMANENT",
                    "ROLE_DIRECTEUR", "DIRECTEUR",
                    "ROLE_ADMIN",     "ADMIN"
                )

                // ── 4. Matériel ──────────────────────────────────────────────
                .requestMatchers(
                    "/materiel/**",
                    "/demande-materiel/**",
                    "/mes-demandes/**",
                    "/admin/demandes/**"
                ).hasAnyAuthority(
                    "ROLE_PERMANENT",  "PERMANENT",
                    "ROLE_DIRECTEUR",  "DIRECTEUR",
                    "ROLE_DOCTORANT",  "DOCTORANT",
                    "ROLE_ASSOCIE",    "ASSOCIE",
                    "ROLE_ADMIN",      "ADMIN"
                )

                // ── 5. Publications ──────────────────────────────────────────
                .requestMatchers("/publications/**")
                .hasAnyAuthority(
                    "ROLE_PERMANENT",  "PERMANENT",
                    "ROLE_DIRECTEUR",  "DIRECTEUR",
                    "ROLE_DOCTORANT",  "DOCTORANT",
                    "ROLE_ADMIN",      "ADMIN"
                )

                // ── 6. Profil — tout membre connecté ────────────────────────
                .requestMatchers("/profil/**").authenticated()

                // ── 7. Dashboard — tout membre connecté ─────────────────────
                .requestMatchers("/dashboard").authenticated()

                // ── 8. Événements ────────────────────────────────────────────
                .requestMatchers("/evenements/**")
                .hasAnyAuthority(
                    "ROLE_PERMANENT",  "PERMANENT",
                    "ROLE_DIRECTEUR",  "DIRECTEUR",
                    "ROLE_DOCTORANT",  "DOCTORANT",
                    "ROLE_ASSOCIE",    "ASSOCIE",
                    "ROLE_ADMIN",      "ADMIN"
                )

                // ── 9. Documents ─────────────────────────────────────────────
                .requestMatchers("/documents/**")
                .hasAnyAuthority(
                    "ROLE_PERMANENT",  "PERMANENT",
                    "ROLE_DIRECTEUR",  "DIRECTEUR",
                    "ROLE_DOCTORANT",  "DOCTORANT",
                    "ROLE_ASSOCIE",    "ASSOCIE",
                    "ROLE_ADMIN",      "ADMIN"
                )

                // ── 10. Messagerie ───────────────────────────────────────────
                .requestMatchers("/messages/**").authenticated()

                // ── 11. Conventions ──────────────────────────────────────────
                .requestMatchers("/conventions/**")
                .hasAnyAuthority(
                    "ROLE_PERMANENT",  "PERMANENT",
                    "ROLE_DIRECTEUR",  "DIRECTEUR",
                    "ROLE_ADMIN",      "ADMIN"
                )

                // ── 12. Procès-verbaux ────────────────────────────────────────
                .requestMatchers("/reunions/**")
                .hasAnyAuthority(
                    "ROLE_PERMANENT",  "PERMANENT",
                    "ROLE_DIRECTEUR",  "DIRECTEUR",
                    "ROLE_ADMIN",      "ADMIN"
                )

                // ── 13. Notifications ─────────────────────────────────────────
                .requestMatchers("/notifications/**").authenticated()

                // ── 14. Recherche globale ──────────────────────────────────────
                .requestMatchers("/recherche/**").authenticated()

                // ── 15. Tout le reste — connecté ───────────────────────────────
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
