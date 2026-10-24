package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Mandat;
import com.lias.gestionlabo.repositories.MandatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class MandatService {

    @Autowired
    private MandatRepository mandatRepository;

    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    @Transactional
    public void cloturerMandat(Long id) {
        Mandat mandat = mandatRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mandat introuvable"));
        
        mandat.setEstActif(false);
        mandat.setDateFin(new Date()); // تسجيل تاريخ اليوم كـ تاريخ نهاية
        mandatRepository.save(mandat);
    }
    
    // جلب كل المندات
    public List<Mandat> getAllMandats() {
        return mandatRepository.findAll();
    }

    /**
     * دالة ذكية للتحقق: 
     * هل المستخدم الحالي هو مدير (DIRECTEUR) وحالته نشطة (estActif = true)؟
     * تستخدم هذه الدالة في الـ Controllers لحماية الروابط وفي الـ HTML لإخفاء القوائم.
     */
    public boolean isUserDirecteurActif(String email) {
        return mandatRepository.findAll().stream()
            .anyMatch(m -> m.getResponsable() != null 
                        && m.getResponsable().getEmail().equals(email) 
                        && m.isEstActif() 
                        && "DIRECTEUR".equals(m.getRole()));
    }
}