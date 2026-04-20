package com.visa.transformable.repository;

import com.visa.transformable.model.TypeDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TypeDemandeRepository extends JpaRepository<TypeDemande, Long> {
    Optional<TypeDemande> findByLibelle(String libelle);
}
