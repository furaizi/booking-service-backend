package com.example.trainservice.controller;

import com.example.trainservice.dto.TrainDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainController {
    ResponseEntity<List<TrainDTO>> getAllTrains();
    ResponseEntity<List<TrainDTO>> getAllTrainsBetweenStations(String startStation, String endStation);
    ResponseEntity<TrainDTO> getTrain(String number);
    ResponseEntity<TrainDTO> createTrain(TrainDTO train);
    ResponseEntity<TrainDTO> updateTrain(String trainNumber, TrainDTO trainDTO);
    ResponseEntity<Void> deleteTrain(String trainNumber);
}
