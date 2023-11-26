package com.epam.training.ticketservice.core.repository;

import com.epam.training.ticketservice.core.model.Movie;
import com.epam.training.ticketservice.core.model.Room;
import com.epam.training.ticketservice.core.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ScreeningRepo extends JpaRepository<Screening, Integer> {

    Optional<Screening> findScreeningByMovieAndRoomAndScreeningTime(
            Movie movie,
            Room room,
            LocalDateTime screeningTime
    );

    Optional<Screening> findScreeningByRoom(Room room);
}