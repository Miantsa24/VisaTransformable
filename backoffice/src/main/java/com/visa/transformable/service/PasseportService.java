package com.visa.transformable.service;

import com.visa.transformable.model.Passeport;
import com.visa.transformable.repository.PasseportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasseportService {
    @Autowired
    private PasseportRepository passeportRepository;

    public Passeport createPasseport(Passeport passeport) {
        return passeportRepository.save(passeport);
    }
}
