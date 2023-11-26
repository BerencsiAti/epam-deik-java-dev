package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.dto.ScreeningDto;
import com.epam.training.ticketservice.core.exceptions.BreakStageException;
import com.epam.training.ticketservice.core.exceptions.ExtendingException;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;
import com.epam.training.ticketservice.core.model.Movie;
import com.epam.training.ticketservice.core.model.Room;
import com.epam.training.ticketservice.core.model.Screening;
import com.epam.training.ticketservice.core.repository.MovieRepo;
import com.epam.training.ticketservice.core.repository.RoomRepo;
import com.epam.training.ticketservice.core.repository.ScreeningRepo;
import com.epam.training.ticketservice.core.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepo screeningRepo;
    private final MovieRepo movieRepo;
    private final RoomRepo roomRepo;

    @Override
    public void createScreening(String movie, String room, LocalDateTime screeningTime)
            throws NotFoundException, ExtendingException, BreakStageException {
        Optional<Movie> movieOptional = movieRepo.findByName(movie);
        Optional<Room> roomOptional = roomRepo.findByName(room);
        checkMovieAndRoomExistence(movieOptional, roomOptional);
        Screening returnScreening = new Screening(movieOptional.get(), roomOptional.get(), screeningTime);
        canCreateScreening(returnScreening);
        screeningRepo.save(returnScreening);
    }

    private void canCreateScreening(Screening returnScreening)
            throws ExtendingException, BreakStageException {
        Optional<Screening> screeningList = screeningRepo.findScreeningByRoom(returnScreening.getRoom());

        if (screeningList.isEmpty()) {
            return;
        }

        for (Screening iterator : screeningList.stream().toList()) {
            LocalDateTime screeningStart = returnScreening.getScreeningTime();
            LocalDateTime screeningEnd = screeningStart.plusMinutes(returnScreening.getMovie().getLength());
            LocalDateTime screeningBreakPeriod = screeningEnd.plusMinutes(10);

            LocalDateTime iteratorStart = iterator.getScreeningTime();
            LocalDateTime iteratorEnd = iteratorStart.plusMinutes(iterator.getMovie().getLength());
            LocalDateTime iteratorBreakPeriod = iteratorEnd.plusMinutes(10);

            boolean isScreeningBetweenIterators = isBetween(screeningStart, iteratorStart, iteratorEnd)
                    || isBetween(screeningEnd, iteratorStart, iteratorEnd)
                    || isInside(iteratorStart, iteratorEnd, screeningStart, screeningEnd);
            boolean isScreeningBeforeIteratorBreak = isBefore(screeningEnd, iteratorStart)
                    && isAfter(screeningBreakPeriod, iteratorStart);
            boolean isScreeningAfterIteratorEnd = isAfter(screeningStart, iteratorEnd)
                    && isBefore(screeningStart, iteratorBreakPeriod);

            if (isScreeningBetweenIterators || isEqual(screeningStart, iteratorStart)
                    || isEqual(screeningEnd, iteratorEnd)) {
                throw new ExtendingException("There is an extending screening");
            } else if (isScreeningBeforeIteratorBreak || isScreeningAfterIteratorEnd) {
                throw new BreakStageException(
                        "This would start in the break period after another screening in this room"
                );
            }
        }
    }

    private static boolean isBetween(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        return dateTime.isAfter(start) && dateTime.isBefore(end);
    }

    private static boolean isInside(LocalDateTime start, LocalDateTime end,
                                    LocalDateTime innerStart, LocalDateTime innerEnd) {
        return innerStart.isAfter(start) && innerEnd.isBefore(end);
    }

    private static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isEqual(dateTime2);
    }

    private static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    private static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2);
    }

    private void checkMovieAndRoomExistence(Optional<Movie> movie, Optional<Room> room) throws NotFoundException {
        if (movie.isEmpty() && room.isEmpty()) {
            throw new NotFoundException("The given movie and room do not found");
        } else if (room.isEmpty()) {
            throw new NotFoundException("The given room does not found");
        } else if (movie.isEmpty()) {
            throw new NotFoundException("The given movie does not found");
        }
    }

    @Override
    public void deleteScreening(String movie, String room, LocalDateTime screeningTime) throws NotFoundException {
        Optional<Movie> movieOptional = movieRepo.findByName(movie);
        Optional<Room> roomOptional = roomRepo.findByName(room);
        checkMovieAndRoomExistence(movieOptional, roomOptional);
        if (screeningRepo.findScreeningByMovieAndRoomAndScreeningTime(
                movieOptional.get(),
                roomOptional.get(),
                screeningTime
        ).isPresent()) {
            Screening screening = screeningRepo.findScreeningByMovieAndRoomAndScreeningTime(
                    movieOptional.get(),
                    roomOptional.get(),
                    screeningTime
            ).get();
            screeningRepo.delete(screening);
        } else {
            throw new NotFoundException("The given screening does not found.");
        }
    }

    @Override
    public List<ScreeningDto> screeningList() {
        return screeningRepo.findAll().stream()
                .map(ScreeningDto::new)
                .toList();
    }
}
