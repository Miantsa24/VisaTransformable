package com.visa.transformable.repository;

import com.visa.transformable.model.CarteResident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarteResidentRepository extends JpaRepository<CarteResident, Long> {
    Optional<CarteResident> findByNumeroCarteResident(String numeroCarteResident);
    List<CarteResident> findByDemandeurId(Long idDemandeur);
}
