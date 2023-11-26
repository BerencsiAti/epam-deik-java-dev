package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.dto.RoomDto;
import com.epam.training.ticketservice.core.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;

import java.util.List;

public interface RoomService {

    void createRoom(String name, int row, int col)
            throws AlreadyExistsException;


    void updateRoom(String name, int row, int col)
            throws NotFoundException;


    void deleteRoom(String name)
            throws NotFoundException;


    List<RoomDto> roomList();
}
