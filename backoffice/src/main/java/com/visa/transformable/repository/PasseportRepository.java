package com.visa.transformable.repository;

import com.visa.transformable.model.Passeport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PasseportRepository extends JpaRepository<Passeport, Long> {
    Optional<Passeport> findByNumeroPasseport(String numeroPasseport);
    List<Passeport> findByIdDemandeur(Long idDemandeur);
}
