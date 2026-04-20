package com.visa.transformable.controller;

import com.visa.transformable.model.Passeport;
import com.visa.transformable.service.PasseportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backoffice/passeport")
public class PasseportController {
    @Autowired
    private PasseportService passeportService;

    @PostMapping
    public ResponseEntity<Passeport> createPasseport(@RequestBody Passeport passeport) {
        Passeport saved = passeportService.createPasseport(passeport);
        return ResponseEntity.ok(saved);
    }
}
