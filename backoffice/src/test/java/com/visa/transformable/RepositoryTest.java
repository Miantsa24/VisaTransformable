package com.visa.transformable;

import com.visa.transformable.model.SituationFamiliale;
import com.visa.transformable.repository.NationaliteRepository;
import com.visa.transformable.repository.SituationFamilialeRepository;
import com.visa.transformable.model.Nationalite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    SituationFamilialeRepository situationFamilialeRepository;

    @Autowired
    NationaliteRepository nationaliteRepository;

    @Test
    void testSituationFamilialeFindAll() {
        List<SituationFamiliale> situations = situationFamilialeRepository.findAll();
        System.out.println("Situations: " + situations);
        assertFalse(situations.isEmpty(), "La table situation_familiale est vide !");
    }

    @Test
    void testNationaliteFindAll() {
        List<Nationalite> nationalites = nationaliteRepository.findAll();
        System.out.println("Nationalités: " + nationalites);
        assertFalse(nationalites.isEmpty(), "La table nationalite est vide !");
    }
}