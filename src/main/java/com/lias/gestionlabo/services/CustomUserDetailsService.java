package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.StatutMembre;
import com.lias.gestionlabo.repositories.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MembreRepository membreRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Membre membre = membreRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Membre introuvable: " + email));

        // L'admin système n'a pas de StatutMembre — toujours actif
        // Les RETRAITE et ANCIEN sont bloqués
        boolean compteActif;
        if ("ADMIN".equals(membre.getRole())) {
            compteActif = true;
        } else {
            compteActif = membre.getStatutActuel() != StatutMembre.RETRAITE
                       && membre.getStatutActuel() != StatutMembre.ANCIEN;
        }

        String role = membre.getRole();

        return User.builder()
                .username(membre.getEmail())
                .password(membre.getPassword())
                .roles(role)
                .disabled(!compteActif)
                .build();
    }
}