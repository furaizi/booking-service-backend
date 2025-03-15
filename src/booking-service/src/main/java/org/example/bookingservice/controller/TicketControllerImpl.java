package org.example.bookingservice.controller;

import org.example.bookingservice.dto.TicketDTO;
import org.example.bookingservice.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketControllerImpl implements TicketController {

    private final TicketService ticketService;

    public TicketControllerImpl(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<TicketDTO>> getAllTickets(@RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) String trainNumber) {
        if (userId != null)
            return ResponseEntity.ok(ticketService.getAllTicketsOfUser(userId));
        else if (trainNumber != null)
            return ResponseEntity.ok(ticketService.getAllTicketsOfTrain(trainNumber));
        else
            return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        final var ticket = ticketService.getTicketById(id);
        if (ticket == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    @Override
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.addTicket(ticketDTO));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id,
                                                  @RequestBody TicketDTO ticketDTO) {
        final var updated = ticketService.updateTicket(id, ticketDTO);
        if (updated == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
