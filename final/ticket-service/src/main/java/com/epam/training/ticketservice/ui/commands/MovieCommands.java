package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.dto.MovieDto;
import com.epam.training.ticketservice.core.model.User;
import com.epam.training.ticketservice.core.service.MovieService;
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
public class MovieCommands {

    private final MovieService movieService;
    private final UserService userService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Creates a movie")
    public String createMovie(String name, String genre, int length) {
        try {
            movieService.createMovie(name, genre, length);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "The movie is created";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Updates an existing movie")
    public String updateMovie(String name, String genre, int length) {
        try {
            movieService.updateMovie(name, genre, length);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "The movie is updated";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Deletes an existing movie")
    public String deleteMovie(String name) {
        try {
            movieService.deleteMovie(name);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "The movie is deleted";
    }

    @ShellMethod(key = "list movies", value = "Lists all movies")
    public String listMovies() {
        List<MovieDto> movieDtoList = movieService.movieList();

        if (movieDtoList.isEmpty()) {
            return "There are no movies at the moment";
        }

        StringBuilder builder = new StringBuilder();
        StringJoiner joiner = new StringJoiner("\n");

        for (MovieDto movieDto : movieDtoList) {
            joiner.add(movieDto.toString());
        }

        return builder.append(joiner).toString();
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
