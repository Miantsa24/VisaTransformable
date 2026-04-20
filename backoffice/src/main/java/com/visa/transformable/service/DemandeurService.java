package com.visa.transformable.service;

import com.visa.transformable.model.Demandeur;
import com.visa.transformable.repository.DemandeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandeurService {
    @Autowired
    private DemandeurRepository demandeurRepository;

    public Demandeur createDemandeur(Demandeur demandeur) {
        return demandeurRepository.save(demandeur);
    }
}
