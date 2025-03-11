package com.example.trainservice.service;

import com.example.trainservice.dto.TrainDTO;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TrainService {
    List<TrainDTO> getAllTrains();
    List<TrainDTO> getAllTrainsBetweenStations(String startStationName, String endStationName);
    @Nullable
    TrainDTO getTrainById(Long id);
    @Nullable
    TrainDTO getTrainByNumber(String number);
    TrainDTO addTrain(TrainDTO trainDTO);
    @Nullable
    TrainDTO updateTrain(Long trainId, TrainDTO trainDTO);
    void deleteTrain(Long trainId);
}
