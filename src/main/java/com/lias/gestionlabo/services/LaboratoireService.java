package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.Laboratoire;
import com.lias.gestionlabo.repositories.LaboratoireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratoireService {

    @Autowired
    private LaboratoireRepository laboRepo;

    // Had l-méthode hiya li k-t-jib info wa7da (la premiére)
    public Laboratoire getLaboratoireInfo() {
        List<Laboratoire> labos = laboRepo.findAll();
        if (!labos.isEmpty()) {
            return labos.get(0);
        }
        return null; // I-l-a kan DB khawi
    }

    // Had l-méthode hya li zednaha bach t-affichi list (hta hya khdama)
    public List<Laboratoire> getAllLaboratoires() {
        return laboRepo.findAll();
    }
}