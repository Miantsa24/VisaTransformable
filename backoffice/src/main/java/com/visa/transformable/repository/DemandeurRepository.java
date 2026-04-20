package com.visa.transformable.repository;

import com.visa.transformable.model.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DemandeurRepository extends JpaRepository<Demandeur, Long> {
    Optional<Demandeur> findByEmail(String email);
    Optional<Demandeur> findByTelephone(String telephone);
}
