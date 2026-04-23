package com.visa.transformable.repository;

import com.visa.transformable.model.TypeVisa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeVisaRepository extends JpaRepository<TypeVisa, Long> {
    Optional<TypeVisa> findByLibelle(String libelle);
}
