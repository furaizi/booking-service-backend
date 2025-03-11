package com.example.trainservice.service;

import com.example.trainservice.dto.TrainDTO;
import com.example.trainservice.mapper.TrainMapper;
import com.example.trainservice.repository.TrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {

    private final TrainRepository trainRepository;
    private final TrainMapper trainMapper;

    public TrainServiceImpl(TrainRepository trainRepository, TrainMapper trainMapper) {
        this.trainRepository = trainRepository;
        this.trainMapper = trainMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainDTO> getAllTrains() {
        var trains = trainRepository.findAll();
        return trains.stream()
                .map(trainMapper::toTrainDTO)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<TrainDTO> getAllTrainsBetweenStations(String startStationName, String endStationName) {
        var trains = trainRepository.findAllTrainsBetweenStations(startStationName, endStationName);
        return trains.stream()
                .map(trainMapper::toTrainDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrainDTO getTrainById(Long id) {
        var train = trainRepository.findById(id)
                .orElse(null);
        return trainMapper.toTrainDTO(train);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainDTO getTrainByNumber(String number) {
        var train = trainRepository.findByNumber(number)
                .orElse(null);
        return trainMapper.toTrainDTO(train);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public TrainDTO addTrain(TrainDTO trainDTO) {
        var train = trainRepository.save(trainMapper.toTrain(trainDTO));
        return trainMapper.toTrainDTO(train);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public TrainDTO updateTrain(String trainNumber, TrainDTO trainDTO) {
        if (trainNumber.equals(trainDTO.getNumber()) && trainRepository.existsByNumber(trainNumber)) {
            var train = trainRepository.save(trainMapper.toTrain(trainDTO));
            return trainMapper.toTrainDTO(train);
        }

        return null;
    }

    @Override
    @Transactional
    public void deleteTrain(String trainNumber) {
        trainRepository.deleteByNumber(trainNumber);
    }
}
