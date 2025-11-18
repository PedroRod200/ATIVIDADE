package com.pedrorod.atividade.controller;

import com.pedrorod.atividade.dto.EntryRequest;
import com.pedrorod.atividade.dto.ExitResponse;
import com.pedrorod.atividade.repository.ParkingSpotRepository;
import com.pedrorod.atividade.service.ParkingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService service;
    private final ParkingSpotRepository spotRepository;

    public ParkingController(ParkingService service, ParkingSpotRepository spotRepository) {
        this.service = service;
        this.spotRepository = spotRepository;
    }

    @PostMapping("/entry")
    public ResponseEntity<?> entry(@RequestBody EntryRequest req) {
        service.registerEntry(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exit")
    public ResponseEntity<ExitResponse> exit(@RequestParam String plate) {
        ExitResponse resp = service.exitByPlate(plate);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/spots")
    public ResponseEntity<?> spots() {
        long occupied = spotRepository.countByOccupiedTrue();
        long free = spotRepository.countByOccupiedFalse();
        long total = spotRepository.count();
        var payload = new Object() {
            public final long occupiedSpots = occupied;
            public final long freeSpots = free;
            public final long totalSpots = total;
            public final List<Object> spots = spotRepository.findAll().stream().map(s -> {
                return new Object() {
                    public final Long id = s.getId();
                    public final Integer number = s.getNumber();
                    public final boolean occupied = s.isOccupied();
                };
            }).collect(Collectors.toList());
        };
        return ResponseEntity.ok(payload);
    }
}