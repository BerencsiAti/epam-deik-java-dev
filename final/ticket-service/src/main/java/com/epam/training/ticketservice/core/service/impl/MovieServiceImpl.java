package com.epam.training.ticketservice.core.service.impl;

import com.epam.training.ticketservice.core.dto.MovieDto;
import com.epam.training.ticketservice.core.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.core.exceptions.NotFoundException;
import com.epam.training.ticketservice.core.model.Movie;
import com.epam.training.ticketservice.core.repository.MovieRepo;
import com.epam.training.ticketservice.core.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepo movieRepo;

    @Override
    public void createMovie(String name, String genre, int length)
            throws AlreadyExistsException {
        Optional<Movie> existingMovie = movieRepo.findByName(name);
        if (existingMovie.isPresent()) {
            throw new AlreadyExistsException("The movie already exists.");
        } else {
            Movie movie = new Movie(name, genre, length);
            movieRepo.save(movie);
        }
    }

    @Override
    public void updateMovie(String name, String genre, int length)
            throws NotFoundException {
        Optional<Movie> existingMovie = movieRepo.findByName(name);
        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            movie.setGenre(genre);
            movie.setLength(length);
            movieRepo.save(movie);
        } else {
            throw new NotFoundException("The movie does not found.");
        }
    }

    @Override
    public void deleteMovie(String name)
            throws NotFoundException {
        Optional<Movie> existingMovie = movieRepo.findByName(name);
        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            movieRepo.delete(movie);
        } else {
            throw new NotFoundException("The movie does not found.");
        }
    }

    @Override
    public List<MovieDto> movieList() {
        return movieRepo.findAll().stream()
                .map(MovieDto::new)
                .collect(Collectors.toList());
    }
}
