package com.epam.training.ticketservice.core.dto;

import com.epam.training.ticketservice.core.model.Room;
import lombok.Getter;

@Getter
public class RoomDto {

    private final String name;
    private final int rows;
    private final int cols;

    public RoomDto(Room room) {
        name = room.getName();
        rows = room.getRows();
        cols = room.getCols();
    }

    @Override
    public String toString() {
        return "Room "
                + name
                + " with "
                + (rows * cols)
                + " seats, "
                + rows
                + " rows and "
                + cols
                + " columns";
    }
}
