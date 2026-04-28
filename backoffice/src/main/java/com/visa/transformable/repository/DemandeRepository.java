package com.visa.transformable.repository;

import com.visa.transformable.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByDemandeurId(Long idDemandeur);
    List<Demande> findByVisaId(Long idVisa);
    List<Demande> findByTypeDemandeId(Long idTypeDemande);
    List<Demande> findByStatutDemandeLibelle(String libelleStatut);
    Optional<Demande> findById(Long id);
}
