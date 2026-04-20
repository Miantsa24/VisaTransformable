package com.visa.transformable.controller;

import com.visa.transformable.dto.DemandeDTO;
import com.visa.transformable.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backoffice/demande")
public class DemandeController {
    @Autowired
    private DemandeService demandeService;

    @PostMapping
    public ResponseEntity<?> createDemande(@RequestBody DemandeDTO dto) {
        demandeService.createDemande(dto);
        return ResponseEntity.ok().build();
    }
}
