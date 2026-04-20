package com.visa.transformable.controller;

import com.visa.transformable.model.Demandeur;
import com.visa.transformable.service.DemandeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backoffice/demandeur")
public class DemandeurController {
    @Autowired
    private DemandeurService demandeurService;

    @PostMapping
    public ResponseEntity<Demandeur> createDemandeur(@RequestBody Demandeur demandeur) {
        Demandeur saved = demandeurService.createDemandeur(demandeur);
        return ResponseEntity.ok(saved);
    }
}
