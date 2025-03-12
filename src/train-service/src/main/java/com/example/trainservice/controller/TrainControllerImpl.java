package com.example.trainservice.controller;

import com.example.trainservice.dto.TrainDTO;
import com.example.trainservice.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainControllerImpl implements TrainController {

    private final TrainService trainService;

    public TrainControllerImpl(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAllTrains());
    }

    @GetMapping("/search")
    @Override
    public ResponseEntity<?> getAllTrainsBetweenStations(@RequestParam String startStation,
                                                         @RequestParam String endStation) {
        if (startStation == null || endStation == null)
            return ResponseEntity.badRequest().body("Provide both start station and end station");
        return ResponseEntity.ok(trainService.getAllTrainsBetweenStations(startStation, endStation));
    }

    @GetMapping("/{number}")
    @Override
    public ResponseEntity<TrainDTO> getTrain(@PathVariable String number) {
        final var train = trainService.getTrainByNumber(number);
        if (train == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(train);
    }

    @PostMapping
    @Override
    public ResponseEntity<TrainDTO> createTrain(@RequestBody TrainDTO train) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trainService.addTrain(train));
    }

    @PutMapping("/{number}")
    @Override
    public ResponseEntity<TrainDTO> updateTrain(@PathVariable String number,
                                                @RequestBody TrainDTO trainDTO) {
        final var updated = trainService.updateTrain(number, trainDTO);
        if (updated == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{number}")
    @Override
    public ResponseEntity<Void> deleteTrain(@PathVariable String number) {
        trainService.deleteTrain(number);
        return ResponseEntity.noContent().build();
    }
}
