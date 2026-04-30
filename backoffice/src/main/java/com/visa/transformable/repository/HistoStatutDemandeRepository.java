package com.visa.transformable.repository;

import com.visa.transformable.model.HistoStatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoStatutDemandeRepository extends JpaRepository<HistoStatutDemande, Long> {
    List<HistoStatutDemande> findByDemandeIdOrderByDateChangementDesc(Long idDemande);
}