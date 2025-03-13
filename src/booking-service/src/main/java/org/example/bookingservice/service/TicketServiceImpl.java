package org.example.bookingservice.service;

import org.example.bookingservice.dto.TicketDTO;
import org.example.bookingservice.mapper.TicketMapper;
import org.example.bookingservice.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTickets() {
        var tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticketMapper::toTicketDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTicketsOfUser(Long userId) {
        var tickets = ticketRepository.findAllTicketsByUser_Id(userId);
        return tickets.stream()
                .map(ticketMapper::toTicketDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTicketsOfTrain(String trainNumber) {
        var tickets = ticketRepository.findAllTicketsByTrain_Number(trainNumber);
        return tickets.stream()
                .map(ticketMapper::toTicketDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDTO getTicketById(Long id) {
        var ticket = ticketRepository.findById(id)
                .orElse(null);
        return ticketMapper.toTicketDTO(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public TicketDTO addTicket(TicketDTO ticketDTO) {
        var ticket = ticketRepository.save(ticketMapper.toTicket(ticketDTO));
        return ticketMapper.toTicketDTO(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        if (id.equals(ticketDTO.getId()) && ticketRepository.existsById(id)) {
            var ticket = ticketRepository.save(ticketMapper.toTicket(ticketDTO));
            return ticketMapper.toTicketDTO(ticket);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
