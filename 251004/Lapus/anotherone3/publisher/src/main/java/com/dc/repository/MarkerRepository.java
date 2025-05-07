package com.dc.repository;

import com.dc.model.blo.Marker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkerRepository extends JpaRepository<Marker, Long> {
    Page<Marker> findAll(Pageable pageable);
}
