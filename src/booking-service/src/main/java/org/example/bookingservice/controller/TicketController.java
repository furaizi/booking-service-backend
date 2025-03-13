package org.example.bookingservice.controller;

import org.example.bookingservice.dto.TicketDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TicketController {
    ResponseEntity<List<TicketDTO>> getAllTicketsOfUser(Long userId);
    ResponseEntity<List<TicketDTO>> getAllTicketsOfTrain(String trainNumber);
    ResponseEntity<TicketDTO> getTicket(Long id);
    ResponseEntity<TicketDTO> createTicket(TicketDTO ticketDTO);
    ResponseEntity<TicketDTO> updateTicket(Long id, TicketDTO ticketDTO);
    ResponseEntity<Void> deleteTicket(Long id);
}
