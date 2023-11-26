package core.service.impl;

import com.epam.training.ticketservice.core.dto.ScreeningDto;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;
import com.epam.training.ticketservice.core.model.Movie;
import com.epam.training.ticketservice.core.model.Room;
import com.epam.training.ticketservice.core.model.Screening;
import com.epam.training.ticketservice.core.repository.MovieRepo;
import com.epam.training.ticketservice.core.repository.RoomRepo;
import com.epam.training.ticketservice.core.repository.ScreeningRepo;
import com.epam.training.ticketservice.core.service.impl.ScreeningServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ScreeningServiceImplTests {

    private final MovieRepo movieRepo = Mockito.mock(MovieRepo.class);
    private final RoomRepo roomRepo = Mockito.mock(RoomRepo.class);
    private final ScreeningRepo screeningRepo = Mockito.mock(ScreeningRepo.class);
    private final ScreeningServiceImpl underTest = new ScreeningServiceImpl(
            screeningRepo,
            movieRepo,
            roomRepo
    );
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Movie movie = new Movie("Cars", "Animation", 130);
    private final Room room = new Room("Star Wars", 11, 13);
    private final Screening screening = new Screening(
            movie,
            room,
            LocalDateTime.parse(
                    "2023-11-26 20:00",
                    dateTimeFormatter
            )
    );

    @Test
    public void testDeleteScreeningShouldDeleteScreeningWhenScreeningExists() throws NotFoundException {
        //Given
        when(movieRepo.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepo.findScreeningByMovieAndRoomAndScreeningTime(
                screening.getMovie(),
                screening.getRoom(),
                screening.getScreeningTime())
        ).thenReturn(Optional.of(screening));
        doNothing().when(screeningRepo).delete(screening);

        //When
        underTest.deleteScreening(
                screening.getMovie().getName(),
                screening.getRoom().getName(),
                screening.getScreeningTime()
        );

        //Then
        verify(screeningRepo, never()).save(screening);
        verify(screeningRepo).delete(screening);
    }

    @Test
    public void testDeleteScreeningShouldNotDeleteScreeningWhenScreeningDoesNotExist() {
        //Given
        when(movieRepo.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepo.findScreeningByMovieAndRoomAndScreeningTime(
                screening.getMovie(),
                screening.getRoom(),
                screening.getScreeningTime())
        ).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.deleteScreening(
                        screening.getMovie().getName(),
                        screening.getRoom().getName(),
                        screening.getScreeningTime())
        );

        //Then
        verify(screeningRepo, never()).save(screening);
        verify(screeningRepo, never()).delete(screening);
    }

    @Test
    public void testDeleteScreeningShouldReturnDoesNotExistExceptionWhenMovieAndRoomDoNotExist() {
        //Given
        when(screeningRepo.findScreeningByMovieAndRoomAndScreeningTime(
                screening.getMovie(),
                screening.getRoom(),
                screening.getScreeningTime()
        )).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.deleteScreening(
                        screening.getMovie().getName(),
                        screening.getRoom().getName(),
                        screening.getScreeningTime())
        );

        //Then
        verify(screeningRepo, never()).save(screening);
        verify(screeningRepo, never()).delete(screening);
    }

    @Test
    public void testDeleteScreeningShouldReturnDoesNotExistExceptionWhenRoomDoesNotExist() {
        //Given
        when(movieRepo.findByName(movie.getName())).thenReturn(Optional.of(movie));
        when(screeningRepo.findScreeningByMovieAndRoomAndScreeningTime(
                screening.getMovie(),
                screening.getRoom(),
                screening.getScreeningTime()
        )).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.deleteScreening(
                        screening.getMovie().getName(),
                        screening.getRoom().getName(),
                        screening.getScreeningTime())
        );

        //Then
        verify(screeningRepo, never()).save(screening);
        verify(screeningRepo, never()).delete(screening);
    }

    @Test
    public void testDeleteScreeningShouldReturnDoesNotExistExceptionWhenAMovieDoesNotExist() {
        //Given
        when(roomRepo.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepo.findScreeningByMovieAndRoomAndScreeningTime(screening.getMovie(), screening.getRoom(),
                screening.getScreeningTime())
        ).thenReturn(Optional.empty());

        //When
        assertThrows(NotFoundException.class,
                () -> underTest.deleteScreening(
                        screening.getMovie().getName(),
                        screening.getRoom().getName(), screening.getScreeningTime())
        );

        //Then
        verify(screeningRepo, never()).save(screening);
        verify(screeningRepo, never()).delete(screening);
    }

    @Test
    public void testScreeningListShouldReturnScreeningListWhenThereAreScreenings() {
        //Given
        Mockito.when(screeningRepo.findAll()).thenReturn(Collections.singletonList(screening));

        //When
        List<ScreeningDto> screeningDtoList = underTest.screeningList();

        //Then
        Mockito.verify(screeningRepo).findAll();
        assertEquals(1, screeningDtoList.size());
        assertEquals(screening.getMovie().getName(), screeningDtoList.get(0).getMovieDto().getName());
        assertEquals(screening.getRoom().getName(), screeningDtoList.get(0).getRoomDto().getName());
        assertEquals(screening.getScreeningTime(), screeningDtoList.get(0).getScreeningTime());
    }

    @Test
    public void testRoomListShouldReturnEmptyListWhenRoomListIsEmpty() {
        // Given

        // When
        List<ScreeningDto> screeningDtoList = underTest.screeningList();

        // Then
        assertEquals(emptyList(), screeningDtoList);
    }
}
