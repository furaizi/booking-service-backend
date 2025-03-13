package org.example.bookingservice.service;

import org.example.bookingservice.dto.TicketDTO;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TicketService {
    List<TicketDTO> getAllTickets();
    List<TicketDTO> getAllTicketsOfUser(Long userId);
    List<TicketDTO> getAllTicketsOfTrain(String trainNumber);
    @Nullable
    TicketDTO getTicketById(Long id);
    TicketDTO addTicket(TicketDTO ticketDTO);
    @Nullable
    TicketDTO updateTicket(Long id, TicketDTO ticketDTO);
    void deleteTicket(Long id);
}
