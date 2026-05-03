package com.visa.transformable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.transformable.model.HistoriqueModification;

public interface HistoriqueModificationRepository extends JpaRepository<HistoriqueModification, Long> {

    List<HistoriqueModification> findByDemandeIdOrderByDateModificationDesc(Long demandeId);
}