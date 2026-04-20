package com.visa.transformable.service;

import com.visa.transformable.model.Visa;
import com.visa.transformable.repository.VisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisaService {
    @Autowired
    private VisaRepository visaRepository;

    public Visa createVisa(Visa visa) {
        return visaRepository.save(visa);
    }
}
