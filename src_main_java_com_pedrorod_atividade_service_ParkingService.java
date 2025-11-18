package com.pedrorod.atividade.service;

import com.pedrorod.atividade.dto.EntryRequest;
import com.pedrorod.atividade.dto.ExitResponse;
import com.pedrorod.atividade.model.*;
import com.pedrorod.atividade.repository.*;
import com.pedrorod.atividade.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository spotRepository;
    private final ParkingRecordRepository recordRepository;
    private final UserAccountRepository userRepository;

    private static final BigDecimal RATE_SMALL = BigDecimal.valueOf(16);
    private static final BigDecimal RATE_LARGE = BigDecimal.valueOf(25);
    private static final BigDecimal RATE_MOTO = BigDecimal.valueOf(8);

    public ParkingService(VehicleRepository vehicleRepository,
                          ParkingSpotRepository spotRepository,
                          ParkingRecordRepository recordRepository,
                          UserAccountRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.spotRepository = spotRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void registerEntry(EntryRequest req) {
        if (req.getSpotNumber() == null) throw new BadRequestException("spotNumber obrigatório");

        ParkingSpot spot = spotRepository.findByNumber(req.getSpotNumber())
                .orElseGet(() -> {
                    ParkingSpot s = new ParkingSpot();
                    s.setNumber(req.getSpotNumber());
                    return s;
                });

        if (spot.isOccupied()) {
            throw new ConflictException("Vaga " + req.getSpotNumber() + " já ocupada");
        }

        Vehicle vehicle = vehicleRepository.findByPlate(req.getPlate())
                .orElseGet(() -> {
                    Vehicle v = new Vehicle();
                    v.setPlate(req.getPlate());
                    v.setBrand(req.getBrand());
                    v.setModel(req.getModel());
                    v.setType(req.getType());
                    return v;
                });

        vehicle.setBrand(req.getBrand());
        vehicle.setModel(req.getModel());
        vehicle.setType(req.getType());
        vehicleRepository.save(vehicle);

        UserAccount user = userRepository.findByUsername(req.getRegisteredBy())
                .orElseGet(() -> {
                    UserAccount u = new UserAccount();
                    u.setUsername(req.getRegisteredBy());
                    return userRepository.save(u);
                });

        ParkingRecord record = new ParkingRecord();
        record.setVehicle(vehicle);
        record.setSpot(spot);
        record.setRegisteredBy(user);
        record.setEntryTime(LocalDateTime.now());
        recordRepository.save(record);

        spot.setOccupied(true);
        spotRepository.save(spot);
    }

    @Transactional
    public ExitResponse exitByPlate(String plate) {
        ParkingRecord active = recordRepository.findByVehicle_PlateAndExitTimeIsNull(plate)
                .orElseThrow(() -> new NotFoundException("Nenhum veículo em estacionamento com a placa: " + plate));

        LocalDateTime exit = LocalDateTime.now();
        active.setExitTime(exit);

        long hours = calculateHours(active.getEntryTime(), exit);
        BigDecimal rate = rateFor(active.getVehicle().getType());
        BigDecimal total = rate.multiply(BigDecimal.valueOf(hours));

        active.setTotalPaid(total);
        recordRepository.save(active);

        ParkingSpot spot = active.getSpot();
        spot.setOccupied(false);
        spotRepository.save(spot);

        ExitResponse resp = new ExitResponse();
        resp.setPlate(plate);
        resp.setEntryTime(active.getEntryTime());
        resp.setExitTime(exit);
        resp.setHours(hours);
        resp.setRatePerHour(rate);
        resp.setTotal(total);
        return resp;
    }

    private long calculateHours(LocalDateTime entry, LocalDateTime exit) {
        long minutes = Duration.between(entry, exit).toMinutes();
        if (minutes <= 0) return 1;
        long hours = minutes / 60;
        if (minutes % 60 != 0) hours++;
        return Math.max(1, hours);
    }

    private BigDecimal rateFor(VehicleType type) {
        return switch (type) {
            case SMALL -> RATE_SMALL;
            case LARGE -> RATE_LARGE;
            case MOTORCYCLE -> RATE_MOTO;
        };
    }
}