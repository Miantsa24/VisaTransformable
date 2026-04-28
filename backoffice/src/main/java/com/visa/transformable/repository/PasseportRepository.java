package com.visa.transformable.repository;

import com.visa.transformable.model.Passeport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PasseportRepository extends JpaRepository<Passeport, Long> {
    Optional<Passeport> findByNumeroPasseport(String numeroPasseport);
    List<Passeport> findByDemandeurId(Long idDemandeur);
    Optional<Passeport> findByDemandeurIdAndNumeroPasseport(Long idDemandeur, String numeroPasseport);
    Optional<Passeport> findByDemandeurIdAndDateDelivranceAndDateExpirationAndPaysDelivrance(Long idDemandeur, java.sql.Date dateDelivrance, java.sql.Date dateExpiration, String paysDelivrance);
}
