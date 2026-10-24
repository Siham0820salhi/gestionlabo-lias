package com.lias.gestionlabo.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.lias.gestionlabo.services.MandatService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired private MandatService mandatService;

    @ModelAttribute("isDirecteurActif")
    public boolean addDirecteurStatus(Principal principal) {
        if (principal == null) return false;
        return mandatService.isUserDirecteurActif(principal.getName());
    }

    @ModelAttribute("isAdminActif")
    public boolean addAdminStatus(Authentication auth) {
        if (auth == null) return false;
        return auth.getAuthorities().stream()
            .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())
                       || "ADMIN".equals(a.getAuthority()));
    }
}