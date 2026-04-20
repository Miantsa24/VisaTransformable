package com.visa.transformable.repository;

import com.visa.transformable.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTypeCible(Document.TypeCible typeCible);
    List<Document> findByObligatoireTrueAndTypeCible(Document.TypeCible typeCible);
}
