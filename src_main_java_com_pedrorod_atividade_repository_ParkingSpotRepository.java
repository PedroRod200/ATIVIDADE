package com.pedrorod.atividade.repository;

import com.pedrorod.atividade.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findByNumber(Integer number);
    long countByOccupiedTrue();
    long countByOccupiedFalse();
}