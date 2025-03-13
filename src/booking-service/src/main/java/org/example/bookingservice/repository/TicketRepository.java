package org.example.bookingservice.repository;

import org.example.bookingservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllTicketsByUser_Id(Long userId);
    List<Ticket> findAllTicketsByTrain_Number(String trainNumber);
}
