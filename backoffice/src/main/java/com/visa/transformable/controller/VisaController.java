package com.visa.transformable.controller;

import com.visa.transformable.model.Visa;
import com.visa.transformable.service.VisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backoffice/visa")
public class VisaController {
    @Autowired
    private VisaService visaService;

    @PostMapping
    public ResponseEntity<Visa> createVisa(@RequestBody Visa visa) {
        Visa saved = visaService.createVisa(visa);
        return ResponseEntity.ok(saved);
    }
}
