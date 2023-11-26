package com.epam.training.ticketservice.core.dto;

import com.epam.training.ticketservice.core.model.Movie;
import lombok.Getter;

@Getter
public class MovieDto {

    private final String name;
    private final String genre;
    private final int length;

    public MovieDto(Movie movie) {
        name = movie.getName();
        genre = movie.getGenre();
        length = movie.getLength();
    }

    @Override
    public String toString() {
        return name
                + " ("
                + genre
                + ", "
                + length
                + " minutes)";
    }
}
