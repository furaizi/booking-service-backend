package com.example.trainservice.mapper;

import com.example.trainservice.dto.TrainDTO;
import com.example.trainservice.model.Train;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainMapper {
    TrainDTO toTrainDTO(Train train);
    Train toTrain(TrainDTO trainDTO);
}
