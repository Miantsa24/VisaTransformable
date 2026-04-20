package com.visa.transformable.repository;

import com.visa.transformable.model.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VisaRepository extends JpaRepository<Visa, Long> {
    Optional<Visa> findByReference(String reference);
    List<Visa> findByPasseportId(Long idPasseport);
}
