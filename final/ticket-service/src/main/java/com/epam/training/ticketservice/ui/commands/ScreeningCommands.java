package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.dto.ScreeningDto;
import com.epam.training.ticketservice.core.model.User;
import com.epam.training.ticketservice.core.service.ScreeningService;
import com.epam.training.ticketservice.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

@ShellComponent
@RequiredArgsConstructor
public class ScreeningCommands {

    private final ScreeningService screeningService;
    private final UserService userService;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Creates a screening")
    public String createScreening(String movie, String room, String screeningTime) {
        try {
            screeningService.createScreening(movie, room,
                    LocalDateTime.parse(screeningTime, dateTimeFormatter));
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Screening is created";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Deletes a screening")
    public String deleteScreening(String movie, String room, String screeningTime) {
        try {
            screeningService.deleteScreening(movie, room, LocalDateTime.parse(screeningTime, dateTimeFormatter));
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Screening is deleted";
    }

    @ShellMethod(key = "list screenings", value = "Lists all screenings")
    public String listScreenings() {
        List<ScreeningDto> screeningDtoList = screeningService.screeningList();
        if (!screeningDtoList.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            StringJoiner joiner = new StringJoiner("\n");

            for (ScreeningDto screeningDto : screeningDtoList) {
                joiner.add(screeningDto.toString());
            }

            return builder.append(joiner).toString();
        }

        return "There are no screenings";
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
