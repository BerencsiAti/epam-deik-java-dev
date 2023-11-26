package com.epam.training.ticketservice.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;
    private LocalDateTime screeningTime;

    public Screening(Movie movie, Room room, LocalDateTime screeningTime) {
        this.movie = movie;
        this.room = room;
        this.screeningTime = screeningTime;
    }
}
