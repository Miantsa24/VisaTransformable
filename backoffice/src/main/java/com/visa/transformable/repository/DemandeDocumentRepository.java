package com.visa.transformable.repository;

import com.visa.transformable.model.DemandeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeDocumentRepository extends JpaRepository<DemandeDocument, Long> {
    List<DemandeDocument> findByDemandeId(Long idDemande);
    boolean existsByDemandeIdAndDocumentId(Long idDemande, Long idDocument);
}
