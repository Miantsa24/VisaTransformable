package com.visa.transformable.repository;

import com.visa.transformable.model.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Long> {
    Optional<StatutDemande> findByLibelle(String libelle);
}
