package com.epam.training.ticketservice.core.dto;


import com.epam.training.ticketservice.core.model.Screening;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
public class ScreeningDto {

    private final MovieDto movieDto;
    private final RoomDto roomDto;
    private final LocalDateTime screeningTime;

    public ScreeningDto(Screening screening) {
        movieDto = new MovieDto(screening.getMovie());
        roomDto = new RoomDto(screening.getRoom());
        screeningTime = screening.getScreeningTime();
    }

    @Override
    public String toString() {
        return movieDto.toString() + ", screened in room "
                + roomDto.getName() + ", at "
                + screeningTime.format(
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm"
                )
        );
    }
}
