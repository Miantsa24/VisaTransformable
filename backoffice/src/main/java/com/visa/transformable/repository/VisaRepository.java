package com.visa.transformable.repository;

import com.visa.transformable.model.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VisaRepository extends JpaRepository<Visa, Long> {
    Optional<Visa> findByReference(String reference);
    List<Visa> findByPasseportId(Long idPasseport);
    Optional<Visa> findByPasseportIdAndReference(Long idPasseport, String reference);
    Optional<Visa> findByPasseportIdAndDateDebutAndDateFin(Long idPasseport, java.sql.Date dateDebut, java.sql.Date dateFin);
}
