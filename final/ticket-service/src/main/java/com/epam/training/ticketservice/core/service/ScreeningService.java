package com.epam.training.ticketservice.core.service;

import com.epam.training.ticketservice.core.dto.ScreeningDto;
import com.epam.training.ticketservice.core.exceptions.BreakStageException;
import com.epam.training.ticketservice.core.exceptions.ExtendingException;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    void createScreening(String movie, String room, LocalDateTime screeningTime)
            throws ExtendingException, BreakStageException, NotFoundException;
    void deleteScreening(String movie, String room, LocalDateTime screeningTime)
            throws NotFoundException;
    List<ScreeningDto> screeningList();
}
