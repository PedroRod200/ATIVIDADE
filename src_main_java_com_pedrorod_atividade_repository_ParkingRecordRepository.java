package com.pedrorod.atividade.repository;

import com.pedrorod.atividade.model.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    Optional<ParkingRecord> findByVehicle_PlateAndExitTimeIsNull(String plate);
}