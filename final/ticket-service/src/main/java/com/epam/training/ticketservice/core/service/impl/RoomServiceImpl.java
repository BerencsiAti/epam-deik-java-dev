package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.dto.RoomDto;
import com.epam.training.ticketservice.core.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;
import com.epam.training.ticketservice.core.model.Room;
import com.epam.training.ticketservice.core.repository.RoomRepo;
import com.epam.training.ticketservice.core.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepo roomRepo;

    @Override
    public void createRoom(String name, int row, int col) throws AlreadyExistsException {
        Optional<Room> existingRoom = roomRepo.findByName(name);
        if (existingRoom.isPresent()) {
            throw new AlreadyExistsException("The room already exists.");
        } else {
            Room room = new Room(name, row, col);
            roomRepo.save(room);
        }
    }

    @Override
    public void updateRoom(String name, int row, int col) throws NotFoundException {
        Optional<Room> existingRoom = roomRepo.findByName(name);
        if (existingRoom.isPresent()) {
            Room room = existingRoom.get();
            room.setRows(row);
            room.setCols(col);
            roomRepo.save(room);
        } else {
            throw new NotFoundException("The room does not found.");
        }
    }

    @Override
    public void deleteRoom(String name) throws NotFoundException {
        Optional<Room> existingRoom = roomRepo.findByName(name);
        if (existingRoom.isPresent()) {
            Room room = existingRoom.get();
            roomRepo.delete(room);
        } else {
            throw new NotFoundException("The room does not found.");
        }
    }

    @Override
    public List<RoomDto> roomList() {
        return roomRepo.findAll().stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());
    }
}
