package com.example.trainservice.repository;

import com.example.trainservice.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    boolean existsByNumber(String number);
    void deleteByNumber(String number);
    Optional<Train> findByNumber(String number);

    @Query("SELECT DISTINCT t " +
            "FROM Train t " +
            "JOIN t.routes r1 " +
            "JOIN t.routes r2 " +
            "WHERE r1.station.name = :startStation " +
            "  AND r2.station.name = :endStation " +
            "  AND r1.stopOrder < r2.stopOrder")
    List<Train> findAllTrainsBetweenStations(@Param("startStation") String startStation,
                                              @Param("endStation") String endStation);
}
