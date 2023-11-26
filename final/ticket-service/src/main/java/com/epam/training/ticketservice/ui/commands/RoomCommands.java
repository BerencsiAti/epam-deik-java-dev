package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.dto.RoomDto;
import com.epam.training.ticketservice.core.model.User;
import com.epam.training.ticketservice.core.service.RoomService;
import com.epam.training.ticketservice.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.StringJoiner;

@ShellComponent
@RequiredArgsConstructor
public class RoomCommands {

    private final RoomService roomService;
    private final UserService userService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Creates a room")
    public String createRoom(String name, int row, int col) {
        try {
            roomService.createRoom(name, row, col);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "The room is created";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Updates an existing room")
    public String updateRoom(String name, int row, int col) {
        try {
            roomService.updateRoom(name, row, col);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "The room is updated";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Deletes an existing room")
    public String deleteRoom(String name) {
        try {
            roomService.deleteRoom(name);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "The room is deleted";
    }

    @ShellMethod(key = "list rooms", value = "Lists all rooms")
    public String listRooms() {
        List<RoomDto> roomDtoList = roomService.roomList();
        if (!roomDtoList.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            StringJoiner joiner = new StringJoiner("\n");

            for (RoomDto roomDto : roomDtoList) {
                joiner.add(roomDto.toString());
            }

            return builder.append(joiner).toString();
        }

        return "There are no rooms at the moment";
    }

    public Availability isAvailable() {
        if (userService.describeAccount().isPresent()
                && userService.describeAccount().get().role()
                .equals(User.Role.ADMIN)) {
            return Availability.available();
        }

        return Availability.unavailable("You are not authorized");
    }
}
